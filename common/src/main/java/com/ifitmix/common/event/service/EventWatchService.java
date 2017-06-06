package com.ifitmix.common.event.service;

import com.ifitmix.base.constants.FailureInfo;
import com.ifitmix.base.constants.FailureReason;
import com.ifitmix.base.event.domain.AskEvent;
import com.ifitmix.common.event.AskEventCallback;
import com.ifitmix.common.event.AskParameter;
import com.ifitmix.common.event.EventRegistry;
import com.ifitmix.common.event.EventUtils;
import com.ifitmix.common.event.constant.AskEventStatus;
import com.ifitmix.common.event.constant.ProcessStatus;
import com.ifitmix.common.event.dao.AskRequestEventPublishRepository;
import com.ifitmix.common.event.dao.EventWatchProcessRepository;
import com.ifitmix.common.event.dao.EventWatchRepository;
import com.ifitmix.common.event.domain.AskRequestEventPublish;
import com.ifitmix.common.event.domain.EventWatch;
import com.ifitmix.common.event.domain.EventWatchProcess;
import com.ifitmix.common.exception.EventException;
import com.ifitmix.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/**
 * Created by zhangtao on 2017/4/28.
 */
@Service
public class EventWatchService {

    private static Logger logger = LoggerFactory.getLogger(EventWatchService.class);

    @Autowired
    private EventWatchRepository eventWatchRepository;

    @Autowired
    private EventWatchProcessRepository eventWatchProcessRepository;

    @Autowired
    private EventPublishService eventPublishService;

    @Autowired
    private EventRegistry eventRegistry;

    @Autowired
    private AskRequestEventPublishRepository askRequestEventPublishRepository;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private MongoTemplate mongoTemplate;

    public static final int MAX_MESSAGE_COUNTS = 10000;

    private BlockingQueue<EventWatchProcess> queue = new LinkedBlockingDeque<>(MAX_MESSAGE_COUNTS);

    private AtomicBoolean firstTime = new AtomicBoolean(true);

    @Transactional
    public Optional<EventWatchProcess> processEventWatch(Long watchId, AskEventStatus askEventStatus, FailureInfo failureInfo) {
        EventWatch eventWatch = eventWatchRepository.findOne(watchId);
        if(eventWatch == null) {
            throw new EventException("根据ID没有找到EventWatch，watchId: " + watchId);
        }
        if(!eventWatch.getAskEventStatus().equals(AskEventStatus.PENDING)) {
            return Optional.empty();
        }

        if(!eventWatch.isUnited()) {
            String callbackClassName = eventWatch.getCallbackClass();
            String extraParams = eventWatch.getExtraParams();
            List<String> askEventIds = eventWatch.getAskEventIds();
            List<AskRequestEventPublish> askEvents = eventPublishService.findAskRequestEventByEventId(askEventIds);

            if(askEventIds.size() != 1) {
                throw new EventException("EventWatch united 为 true，但是askEventIds的size不为1，watchId：" + watchId);
            }

            eventWatch.setAskEventStatus(askEventStatus);
            executeCallback(askEventStatus.equals(AskEventStatus.SUCCESS), callbackClassName, extraParams,
                    askEvents, failureInfo);

            eventWatchRepository.save(eventWatch);
        } else {
            EventWatchProcess eventWatchProcess = new EventWatchProcess();
            eventWatchProcess.setWatchId(watchId);
            eventWatchProcess.setProcessStatus(ProcessStatus.NEW);
            if(failureInfo != null) {
                eventWatchProcess.setFailureInfo(JsonUtils.object2Json(failureInfo));
            }
            eventWatchProcessRepository.save(eventWatchProcess);
            return Optional.of(eventWatchProcess);
        }

        return Optional.empty();
    }

    /**
     * 执行回调函数
     * @param success
     * @param callbackClassName
     * @param extraParams
     * @param askEvents
     * @param failureInfo
     */
    private void executeCallback(boolean success, String callbackClassName, String extraParams, List<AskRequestEventPublish> askEvents, FailureInfo failureInfo) {
        if(StringUtils.isBlank(callbackClassName)) {
            return;
        }
        AskEventCallback askEventCallback = EventRegistry.getAskEventCallback(callbackClassName);
        if(logger.isDebugEnabled()) {
            logger.debug("execute callback method, askEventCallback: {}, success: {}, askEvents size: {}",
                    askEventCallback, success, askEvents.size());
        }
        askEventCallback.call(eventRegistry, success, askEvents, extraParams, failureInfo);
    }

    /**
     * 获取为完成的 watch 事件
     * @return
     */
    public List<EventWatchProcess> findUnprocessedEventWatchProcess() {
        List<EventWatchProcess> eventWatchProcessList = fetchAllFromQueue();
        if(firstTime.compareAndSet(true, false)) {
            List<EventWatchProcess> list = eventWatchProcessRepository.findByProcessStatus(ProcessStatus.NEW);
            logger.debug("first time to findUnprocessedEventWatchProcess, " +
                        "search unprocessed EventWatchProcess from db, size: " + list.size());
            eventWatchProcessList.addAll(list);
        }
        // 按 addTime 降序排序
        return eventWatchProcessList.stream().sorted((p1, p2) ->  p1.getAddTime() >= p2.getAddTime() ? 1 : -1)
                .collect(Collectors.toList());
    }

    /**
     * 去出队列中的所有元素
     * @return
     */
    private List<EventWatchProcess> fetchAllFromQueue() {
        List<EventWatchProcess> allMessages = new ArrayList<>();
        queue.drainTo(allMessages);
        return allMessages;
    }

    @Transactional
    public void processUnitedEventWatch(EventWatchProcess eventWatchProcess) {
        /**
         * 如果 不为 PENDING， 不做处理。
         * 如果 为 PENDING， 则根据AskResponseEvent 的 success 是 true 还是 false，设置成SUCCESS 或 FAILED。然后根据watchId, 找到UnitedEventWatch。
         * 首先判断UnitedEventWatch的askEventStatus状态，如果不为PENDING， 不做处理
         * 如果为 PENDING，查询UnitedEventWatch的askEventIds列表，根据这些askEvents的状态重新改变UnitedEventWatch的状态
         *改变逻辑:
            根据更新时间升序排列askEvents. 查询到第一个不为PENDING也不为SUCCESS状态的askEvent, 根据这个状态设置UnionEventWatch的状态, 并且触发失败逻辑.
            如果所有askEvents都为Success, 触发成功逻辑. 如果全为PENDING, 报错.
            成功逻辑: UnitedEventWatch状态设置为SUCCESS, 调用注册的回调函数SuccessCallback.
            失败逻辑: UnitedEventWatch状态设置为TIMEOUT/FAILED/CANCELLED. 调用注册的回调函数FailureCallback. 再次查询UnionEventWatch下所有的askEvents,判断他们的状态.
            如果为TIMEOUT/FAILED/CANCELLED, 不做处理.
            如果为PENDING/SUCCESS, 设置状态为TIMEOUT/FAILED/CANCELLED, 然后判断该askEvent是否实现了Revokable接口, 如果实现了, 需要发送RevokeAskEvent事件进行撤销操作.
         */

        logger.debug(String.format("process event watch process, id: %d", eventWatchProcess.getId()));

        Long watchId = eventWatchProcess.getWatchId();
        EventWatch eventWatch = eventWatchRepository.findOne(watchId);
        if(eventWatch == null) {
            return;
        }
        if(!eventWatch.getAskEventStatus().equals(AskEventStatus.PENDING)) {
            return;
        }
        if(!eventWatch.isUnited()) {
            throw new EventException("EventWatch united为false，watchId：" + watchId);
        }
        FailureInfo failureInfo = null;
        if(StringUtils.isNotBlank(eventWatchProcess.getFailureInfo())) {
            failureInfo = JsonUtils.json2Object(eventWatchProcess.getFailureInfo(), FailureInfo.class);
        }

        String callbackClassName = eventWatch.getCallbackClass();
        String extraParams = eventWatch.getExtraParams();
        List<String> askEventIds = eventWatch.getAskEventIds();
        List<AskRequestEventPublish> askEvents = eventPublishService.findAskRequestEventByEventId(askEventIds);

        AskEventStatus failedStatus = null;
        FailureInfo unitedFailedInfo = null;

        if(askEvents.stream().allMatch(ep -> ep.getAskEventStatus().equals(AskEventStatus.SUCCESS))) {
            // 所有的askEvents都为success，触发成功逻辑
            eventWatch.setAskEventStatus(AskEventStatus.SUCCESS);
            executeCallback(true, callbackClassName, extraParams, askEvents, failureInfo);
            eventWatchRepository.save(eventWatch);
        } else if(askEvents.stream().allMatch(ep -> ep.getAskEventStatus().equals(AskEventStatus.PENDING))) {


            if(eventWatch.getTimeoutTime() != null && eventWatch.getTimeoutTime() < (new Date().getTime())) {
                failedStatus = AskEventStatus.TIMEOUT;
                unitedFailedInfo = new FailureInfo(FailureReason.TIMEOUT, new Date().getTime());
            } else {
                // 所有的askEvent都为PENDING，报错
                throw new EventException(String.format("处理united watch事件的时候发现askEvent对应的状态都为PENDING，" +
                " watchId：%d，askEventIds：%s", watchId, askEventIds.toString()));
            }
        } else {
            Optional<AskRequestEventPublish> failedEventPublish = askEvents.stream()
                    .sorted((o1, o2) -> {
                        // 按 updateTime 生序排列
                        return o1.getModifyTime() >= o2.getModifyTime() ? 1 : -1;
                    }).filter(ep -> !ep.getAskEventStatus().equals(AskEventStatus.PENDING) &&
                                    !ep.getAskEventStatus().equals(AskEventStatus.SUCCESS))
                    .findFirst();
            if(failedEventPublish.isPresent()) {
                // 查询到第一个不为PENDING也不为SUCCESS的状态askEvent，根据这个状态设置UnionEventWatch的状态
                failedStatus = failedEventPublish.get().getAskEventStatus();
                unitedFailedInfo = new FailureInfo(EventUtils.fromAskEventStatus(failedEventPublish.get().getAskEventStatus()),
                        failedEventPublish.get().getModifyTime());
            }
        }

        if(failedStatus != null) {
            eventWatch.setAskEventStatus(failedStatus);
            eventWatch.setModifyTime(new Date().getTime());
            eventWatchRepository.save(eventWatch);

            List<AskRequestEventPublish> failedEventProcessList = askEvents.stream()
                    .filter(ep -> ep.getAskEventStatus().equals(AskEventStatus.PENDING) || ep.getAskEventStatus().equals(AskEventStatus.SUCCESS))
                    .collect(Collectors.toList());
            for(AskRequestEventPublish ep : failedEventProcessList) {
                ep.setAskEventStatus(failedStatus);
                ep.setModifyTime(new Date().getTime());
                askRequestEventPublishRepository.save(ep);
                if(eventRegistry.isEventRevokable(ep.getEventType())) {
                    //撤销操作
                    eventBus.publishRevokeEvent(ep.getEventId(), unitedFailedInfo);
                }
            }

            //执行失败的回调
            executeCallback(false, callbackClassName, extraParams, askEvents, unitedFailedInfo);
        }

    }

    public boolean addToQueue(EventWatchProcess eventWatchProcess) {
        try {
            if(logger.isDebugEnabled()) {
                logger.debug("add eventWatchProcess to queue, eventWatchProcess: " + eventWatchProcess);
            }
            queue.offer(eventWatchProcess, 1, TimeUnit.SECONDS);
            return true;
        } catch (InterruptedException e) {
            logger.error("往队列放消息的阻塞过程被中断，队列已满", e);
        }
        return false;
    }

    public void updateProcessStatusBatchToProcessed(Long[] ids) {
        mongoTemplate.updateMulti(Query.query(Criteria.where("id").in(ids)), Update.update("processStatus", ProcessStatus.PROCESSED), EventWatchProcess.class);
    }

    public List<EventWatch> findTimeoutEventWatch(Long timeout) {
        return eventWatchRepository.findByAskEventStatusAndTimeoutTimeLessThan(AskEventStatus.PENDING, timeout);
    }

    public EventWatch watchAskEvents(AskParameter askParameter) {
        EventWatch eventWatch = new EventWatch();
        eventWatch.setAskEventStatus(AskEventStatus.PENDING);
        eventWatch.setAskEventIds(askParameter.getAskEvents().stream()
                .map(AskEvent::getId).collect(Collectors.toList()));
        if(askParameter.getCallbackClass() != null) {
            eventWatch.setCallbackClass(askParameter.getCallbackClass().getName());
        }
        if(!askParameter.getExtraParams().isEmpty()) {
            String json = JsonUtils.object2Json(askParameter.getExtraParams());
            eventWatch.setExtraParams(json);
        }
        if(askParameter.getTimeoutTime().isPresent()) {
            eventWatch.setTimeoutTime(Timestamp.valueOf(askParameter.getTimeoutTime().get()).getTime());
        }
        eventWatch.setUnited(askParameter.isUnited());
        eventWatch.setAddTime(new Date().getTime());
        eventWatch.setModifyTime(new Date().getTime());

        eventWatchRepository.save(eventWatch);

        return eventWatch;
    }
}
