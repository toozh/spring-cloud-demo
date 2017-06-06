package com.ifitmix.common.event.service;

import com.google.common.base.Stopwatch;
import com.ifitmix.base.api.BooleanWrapper;
import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.constants.FailureInfo;
import com.ifitmix.base.constants.FailureReason;
import com.ifitmix.base.event.domain.*;
import com.ifitmix.base.exception.AppBusinessException;
import com.ifitmix.common.event.AskParameter;
import com.ifitmix.common.event.EventRegistry;
import com.ifitmix.common.event.EventUtils;
import com.ifitmix.common.event.constant.AskEventStatus;
import com.ifitmix.common.event.constant.EventCategory;
import com.ifitmix.common.event.constant.ProcessStatus;
import com.ifitmix.common.event.dao.*;
import com.ifitmix.common.event.domain.*;
import com.ifitmix.common.event.handler.AskEventHandler;
import com.ifitmix.common.event.handler.NotifyEventHandler;
import com.ifitmix.common.event.handler.RevokableAskEventHandler;
import com.ifitmix.common.exception.EventException;
import com.ifitmix.common.spring.ApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/4/24.
 */
@Service
public class EventBus {

    private static Logger logger = LoggerFactory.getLogger(EventBus.class);

    @Autowired
    private EventRegistry eventRegistry;

    @Autowired
    private EventActivator eventActivator;

    @Autowired
    private AskRequestEventPublishRepository askRequestEventPublishRepository;

    @Autowired
    private AskResponseEventPublishRepository askResponseEventPublishRepository;

    @Autowired
    private NotifyEventPublishRepository notifyEventPublishRepository;

    @Autowired
    private RevokeAskEventPublishRepository revokeAskEventPublishRepository;

    @Autowired
    private EventProcessRepository eventProcessRepository;

    @Autowired
    private EventWatchRepository eventWatchRepository;

    @Autowired
    private EventWatchProcessRepository eventWatchProcessRepository;

    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private EventWatchService eventWatchService;

    @Autowired
    private MongoTemplate mongoTemplate;



    public String generateEventId() {
        // TODO 重写生成ID 算法
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void fillEventId(BaseEvent baseEvent) {
        if(baseEvent.getId() != null) {
            throw new EventException("event id不为空， id:" + baseEvent.getId());
        }
        baseEvent.setId(generateEventId());
    }






    private <T> EventHandlerResponse<T> executeEventHandler(String eventProcessId, Supplier<T> supplier, T defaultValue) {
        T value = defaultValue;
        String errorMessage = null;
        Stopwatch stopwatch = null;
        try {
            if(logger.isDebugEnabled()) {
                stopwatch = Stopwatch.createStarted();
            }
            // 执行
            value = supplier.get();
        } catch (TransactionSystemException ignore) {

        } catch (AppBusinessException e) {
            errorMessage = e.getMessage();
        } catch (Exception e) {
            logger.error("", e);
            errorMessage = e.getMessage();
        } finally {
            if(logger.isDebugEnabled() && stopwatch != null) {
                stopwatch.stop();
                logger.debug(String.format("执行事件回调结束耗时%dms，EventProcess[eventId=%d]", stopwatch.elapsed(TimeUnit.MILLISECONDS), eventProcessId));
            }
        }
        return new EventHandlerResponse<>(value, errorMessage);
    }

    /**
     * 记录新 事件
     * @param message
     * @return
     */
    public EventProcess recordEvent(String message) {
        Map<String, Object> eventMap = EventUtils.retrieveEventMapFromJson(message);
        EventType eventType = EventType.valueOfIgnoreCase((String) eventMap.get("type"));
        EventCategory eventCategory = eventRegistry.getEventCategoryByType(eventType);
        if(eventCategory.equals(EventCategory.ASKRESP) || eventCategory.equals(EventCategory.REVOKE)) {
            String askEventId = (String) eventMap.get("askEventId");
            if(askEventId == null) {
                throw new EventException("EventCategory为ASKRESP或REVOKE的事件， askEventId为null，payload: " + message);
            }
            boolean eventPublishNotExist = true;
            if(eventCategory.equals(EventCategory.ASKRESP)) {
                eventPublishNotExist = askRequestEventPublishRepository.findByEventId(askEventId) == null;
            } else if(eventCategory.equals(EventCategory.REVOKE)) {
                eventPublishNotExist = askResponseEventPublishRepository.countByAskEventId(askEventId) == 0L;
            }

            if(eventPublishNotExist) {
                return null;
            }

        }
        if(logger.isDebugEnabled()) {
            logger.debug("receive message from kafka: {}", message);
        }
        EventProcess eventProcess = new EventProcess();
        eventProcess.setPayload(message);
        eventProcess.setEventId((String) eventMap.get("id"));
        eventProcess.setEventType(eventType);
        eventProcess.setEventCategory(eventCategory);
        eventProcess.setAddTime(new Date().getTime());
        eventProcess.setModifyTime(new Date().getTime());

        eventProcessRepository.save(eventProcess);
        return eventProcess;
    }

    /**
     * 发送为处理事件
     */
    public void sendUnpublishedEvent() {
        List<EventPublish> eventPublishList = eventPublishService.findUnpublishedEvent();

        for (EventPublish eventPublish : eventPublishList) {
            try {
                if(eventActivator.sendMessage(eventPublish.getPayload(), eventPublish.getEventType().name())) {
                    eventPublish.setProcessStatus(ProcessStatus.PROCESSED);
                    eventPublish.setModifyTime(new Date().getTime());
                    saveEventPublish(eventPublish);
                }
            } catch (EventException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                logger.error(String.format("发送消息到队列时候发送异常，EventPublish[id=%d, payload=[%s]", eventPublish.getId(),
                        eventPublish.getPayload(), e));
            }
        }

    }

    /**
     * 搜索并处理为处理事件
     */
    public void searchAndHandlerUnprocessedEvent() {
        List<EventProcess> events = eventProcessRepository.findByProcessStatus(ProcessStatus.NEW);

        CountDownLatch latch = new CountDownLatch(events.size());

        for(EventProcess event : events) {
            final Long eventProcessId = event.getId();
            taskExecutor.execute(() -> {
                try {

                    EventBus eventBus = ApplicationContextHolder.context.getBean(EventBus.class);
                    eventBus.handleEventProcess(eventProcessId)
                            .map(eventWatchProcess -> eventWatchService.addToQueue(eventWatchProcess));

                } catch (EventException e) {
                    logger.error(e.getMessage());
                } catch (Exception e) {
                    logger.error(String.format("处理事件的时候发送异常，EventProcess[id=%d]", eventProcessId), e);
                } finally {
                    latch.countDown();
                }
            });
        }

    }

    /**
     * 执行事件
     * @param eventProcessId
     * @return
     */
    private Optional<EventWatchProcess> handleEventProcess(Long eventProcessId) {
        Optional<EventWatchProcess> eventWatchProcessOptional = Optional.empty();

        // 将事件状态置成处理中
        Query query = new Query(Criteria.where("_id").is(eventProcessId).and("processStatus").is(ProcessStatus.NEW));
        Update update = new Update().set("processStatus", ProcessStatus.PROCESSING);
        EventProcess eventProcess = mongoTemplate.findAndModify(
                query,
                update,
                new FindAndModifyOptions().returnNew(true),
                EventProcess.class,
                "EventProcess");

        if(eventProcess == null) {
            // 已经处理过了，忽略
            return eventWatchProcessOptional;
        }
        logger.debug(String.format("handle event process, id: %d, event category: %s", eventProcessId, eventProcess.getEventCategory()));

        switch (eventProcess.getEventCategory()) {
            case NOTIFY:
                processNotifyEvent(eventProcess);
                break;
            case ASK:
                processAskEvent(eventProcess);
                break;
            case REVOKE:
                processRevokeEvent(eventProcess);
                break;
            case ASKRESP:
                eventWatchProcessOptional = processAskResponseEvent(eventProcess);
                break;
            default:
                throw new EventException(String.format("unknown evnet category, process id: %d, event category: %s",
                        eventProcessId, eventProcess.getEventCategory()));
        }

        eventProcess.setProcessStatus(ProcessStatus.PROCESSED);
        eventProcessRepository.save(eventProcess);

        return eventWatchProcessOptional;
    }

    private Optional<EventWatchProcess> processAskResponseEvent(EventProcess eventProcess) {

        AskResponseEvent askResponseEvent = eventRegistry.deserializeAskResponseEvent(eventProcess.getPayload());
        String askEventId = askResponseEvent.getAskEventId();
        AskRequestEventPublish askRequestEventPublish = eventPublishService.getAskRequestEventByEventId(askEventId);
        if(!askRequestEventPublish.getAskEventStatus().equals(AskEventStatus.PENDING)) {
            return Optional.empty();
        }
        AskEventStatus askEventStatus;
        FailureInfo failureInfo = null;
        if(askResponseEvent.isSuccess()) {
            askEventStatus = AskEventStatus.SUCCESS;
        } else {
            askEventStatus = AskEventStatus.FAILED;
            failureInfo = new FailureInfo(FailureReason.FAILED, new Date().getTime());
        }

        askRequestEventPublish.setAskEventStatus(askEventStatus);
        askRequestEventPublishRepository.save(askRequestEventPublish);

        return eventWatchService.processEventWatch(askRequestEventPublish.getWatchId(), askEventStatus, failureInfo);
    }

    private void processRevokeEvent(EventProcess eventProcess) {
        RevokeAskEvent revokeAskEvent = (RevokeAskEvent) eventRegistry.deserializeEvent(RevokeAskEvent.EVENT_TYPE, eventProcess.getPayload());
        EventProcess askEventProcess = eventProcessRepository.getByEventId(revokeAskEvent.getAskEventId());
        if(askEventProcess == null) {
            throw new EventException(String.format("根据事件ID[%s]没有找到EventProcess", revokeAskEvent.getAskEventId()));
        }

        EventType type = askEventProcess.getEventType();
        Set<RevokableAskEventHandler> eventHandlers = eventRegistry.getRevokableAskEventHandlers(type);
        if(eventHandlers == null || eventHandlers.isEmpty()) {
            logger.error(String.format("EventProcess[id=%d, type=%s, payload=%s] 的eventHandlers 列表为空", eventProcess.getId(), type, eventProcess.getPayload()));
            return;
        }

        AskEvent originEvent = (AskEvent) eventRegistry.deserializeEvent(type, askEventProcess.getPayload());

        eventHandlers.forEach(handler ->
                executeEventHandler(
                    eventProcess.getEventId(),
                    () -> {
                        handler.processRevoke(originEvent, revokeAskEvent.getFailureInfo());
                        return null;
                    },
                    null
                ));
    }

    /**
     * 处理 通知事件回调
     * @param eventProcess
     */
    public void processNotifyEvent(EventProcess eventProcess) {
        EventType eventType = eventProcess.getEventType();

        Set<NotifyEventHandler> eventHandlers = eventRegistry.getNotifyEventHandlers(eventType);
        if(eventHandlers == null || eventHandlers.isEmpty()) {
            logger.error(String.format("EventProcess[id=%d, type=%s, payload=%s] 的eventHandlers 列表为空", eventProcess.getId(), eventType, eventProcess.getPayload()));
            return;
        }

        NotifyEvent notifyEvent = (NotifyEvent) eventRegistry.deserializeEvent(eventType, eventProcess.getPayload());

        eventHandlers.forEach(handler -> executeEventHandler(notifyEvent.getId(), () -> { handler.notify(notifyEvent); return null;}, null));


    }

    /**
     * 处理 请求回调事件
     * @param eventProcess
     */
    private void processAskEvent(EventProcess eventProcess) {
        EventType eventType = eventProcess.getEventType();

        Set<AskEventHandler> eventHandlers = eventRegistry.getAskEventHandlers(eventType);
        if(eventHandlers == null || eventHandlers.isEmpty()) {
            logger.error(String.format("EventProcess[id=%d, type=%s, payload=%s] 的eventHandlers 列表为空", eventProcess.getId(), eventType, eventProcess.getPayload()));
            return;
        }
        AskEvent askEvent = (AskEvent) eventRegistry.deserializeEvent(eventType, eventProcess.getPayload());
        eventHandlers.forEach(handler -> {
            EventHandlerResponse<BooleanWrapper> result = executeEventHandler(eventProcess.getEventId(),
                    () -> handler.processRequest(askEvent), new BooleanWrapper(false));
            createAskResponse(askEvent, result.getValue());
        });
    }

    /**
     * 发送Ask 结果
     * @param askEvent
     * @param result
     * @return
     */
    private AskResponseEventPublish createAskResponse(AskEvent askEvent, BooleanWrapper result) {
        AskResponseEvent askResponseEvent = new AskResponseEvent(result.isSuccess(), result.getMessage(), askEvent.getId());
        fillEventId(askResponseEvent);
        AskResponseEventPublish eventPublish = new AskResponseEventPublish();
        eventPublish.setSuccess(result.isSuccess());
        eventPublish.setAskEventId(askEvent.getId());
        eventPublish.setEventType(AskResponseEvent.EVENT_TYPE);
        eventPublish.setEventId(askResponseEvent.getId());
        eventPublish.setPayload(EventUtils.serializeEvent(askResponseEvent));
        eventPublish.setAddTime(new Date().getTime());
        eventPublish.setModifyTime(new Date().getTime());

        askResponseEventPublishRepository.save(eventPublish);

        return eventPublish;
    }

    /**
     * 保存或更新事件状态
     * @param eventPublish
     */
    private void saveEventPublish(EventPublish eventPublish) {
        if(eventPublish instanceof NotifyEventPublish) {
            notifyEventPublishRepository.save((NotifyEventPublish) eventPublish);
        } else if(eventPublish instanceof AskRequestEventPublish) {
            askRequestEventPublishRepository.save((AskRequestEventPublish) eventPublish);
        } else if(eventPublish instanceof AskResponseEventPublish) {
            askResponseEventPublishRepository.save((AskResponseEventPublish) eventPublish);
        } else if(eventPublish instanceof RevokeAskEventPublish) {
            revokeAskEventPublishRepository.save((RevokeAskEventPublish) eventPublish);
        } else {
            throw new EventException(String.format("unknown eventPublish class: %s, id: %d",
                    eventPublish.getClass(), eventPublish.getId()));
        }
    }

    /**
     * 处理为完成的 watch 事件
     */
    public void handleUnprocessedEventWatchProcess() {
        List<EventWatchProcess> eventWatchProcesseList = eventWatchService.findUnprocessedEventWatchProcess();
        Set<Long> successIdSet = new HashSet<>();
        Set<Long> watchIdSet = new HashSet<>();

        for(EventWatchProcess eventWatchProcess : eventWatchProcesseList) {
            try {
                if(watchIdSet.add(eventWatchProcess.getWatchId())) {
                    eventWatchService.processUnitedEventWatch(eventWatchProcess);
                }
                successIdSet.add(eventWatchProcess.getId());
            } catch (EventException e) {
                logger.error(e.getMessage(), e);
                eventWatchService.addToQueue(eventWatchProcess);
                watchIdSet.remove(eventWatchProcess.getWatchId());
            } catch (Exception e) {
                logger.error("处理unitedEventWatch事件的时候发生异常，EventWatchProcessId：" + eventWatchProcess.getId(), e);
                eventWatchService.addToQueue(eventWatchProcess);
                watchIdSet.remove(eventWatchProcess.getWatchId());
            }

            if(!successIdSet.isEmpty()) {
                eventWatchService.updateProcessStatusBatchToProcessed(successIdSet.toArray(new Long[successIdSet.size()]));
            }
        }

    }

    @Transactional
    public void publishRevokeEvent(String askEventId, FailureInfo failureInfo) {
        RevokeAskEvent revokeAskEvent = new RevokeAskEvent(failureInfo, askEventId);
        fillEventId(revokeAskEvent);

        RevokeAskEventPublish revokeAskEventPublish = new RevokeAskEventPublish();
        revokeAskEventPublish.setAskEventId(askEventId);
        revokeAskEventPublish.setEventId(revokeAskEvent.getId());
        revokeAskEventPublish.setEventType(revokeAskEvent.getType());
        revokeAskEventPublish.setPayload(EventUtils.serializeEvent(revokeAskEvent));
        revokeAskEventPublish.setAddTime(new Date().getTime());
        revokeAskEventPublish.setModifyTime(new Date().getTime());

        revokeAskEventPublishRepository.save(revokeAskEventPublish);

    }

    public void handleTimeoutEventWatch() {
        Long now = new Date().getTime();
        List<EventWatch> eventWatchList = eventWatchService.findTimeoutEventWatch(now);
        FailureInfo failureInfo = new FailureInfo(FailureReason.TIMEOUT, now);
        for(EventWatch eventWatch : eventWatchList) {
            try {
                eventWatchService.processEventWatch(eventWatch.getId(), AskEventStatus.TIMEOUT, failureInfo)
                    .map(eventWatchProcess -> eventWatchService.addToQueue(eventWatchProcess));
            } catch (EventException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                logger.error(String.format("处理超时EventWatch的时候发生异常，id：%id", eventWatch.getId()), e);
            }
        }
    }

    @Transactional
    public NotifyEventPublish publish(NotifyEvent notifyEvent) {
        fillEventId(notifyEvent);
        String payload = EventUtils.serializeEvent(notifyEvent);

        NotifyEventPublish notifyEventPublish = new NotifyEventPublish();
        notifyEventPublish.setPayload(payload);
        notifyEventPublish.setEventId(notifyEvent.getId());
        notifyEventPublish.setEventType(notifyEvent.getType());
        notifyEventPublish.setAddTime(new Date().getTime());
        notifyEventPublish.setModifyTime(new Date().getTime());

        notifyEventPublishRepository.save(notifyEventPublish);

        return notifyEventPublish;
    }

    /**
     * 发布ask事件
     * @param askParameter
     * @return
     */
    @Transactional
    public List<AskRequestEventPublish> ask(AskParameter askParameter) {

        askParameter.getAskEvents().forEach(this::fillEventId);

        EventWatch eventWatch = eventWatchService.watchAskEvents(askParameter);

        return askParameter.getAskEvents().stream().map(askEvent -> {
            AskRequestEventPublish eventPublish = new AskRequestEventPublish();
            eventPublish.setEventId(askEvent.getId());
            eventPublish.setEventType(askEvent.getType());
            eventPublish.setAskEventStatus(AskEventStatus.PENDING);
            eventPublish.setWatchId(eventWatch.getId());
            eventPublish.setPayload(EventUtils.serializeEvent(askEvent));

            askRequestEventPublishRepository.save(eventPublish);

            return eventPublish;

        }).collect(Collectors.toList());
    }


    public void setEventActivator(EventActivator eventActivator) {
        this.eventActivator = eventActivator;
    }


    private static class EventHandlerResponse<T> {

        private T value;

        String errorMessage;

        public EventHandlerResponse(T value, String errorMessage) {
            this.value = value;
            this.errorMessage = errorMessage;
        }

        public T getValue() {
            return value;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

}
