package com.ifitmix.common.event.service;

import com.ifitmix.common.event.constant.ProcessStatus;
import com.ifitmix.common.event.dao.AskRequestEventPublishRepository;
import com.ifitmix.common.event.dao.AskResponseEventPublishRepository;
import com.ifitmix.common.event.dao.NotifyEventPublishRepository;
import com.ifitmix.common.event.dao.RevokeAskEventPublishRepository;
import com.ifitmix.common.event.domain.AskRequestEventPublish;
import com.ifitmix.common.event.domain.EventPublish;
import com.ifitmix.common.exception.EventException;
import com.ifitmix.utils.CustomPreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/4/28.
 */
@Service
public class EventPublishService {

    @Autowired
    private AskRequestEventPublishRepository askRequestEvnetPulishRepository;

    @Autowired
    private AskResponseEventPublishRepository askResponseEventPublishRepository;

    @Autowired
    private NotifyEventPublishRepository notifyEventPublishRepository;

    @Autowired
    private RevokeAskEventPublishRepository revokeAskEventPublishRepository;

    /**
     * 获取 待发布事件
     * @return
     */
    public List<EventPublish> findUnpublishedEvent() {
        List<EventPublish> unpublishedEvents = new ArrayList<>();
        unpublishedEvents.addAll(askRequestEvnetPulishRepository.findByProcessStatus(ProcessStatus.NEW));
        unpublishedEvents.addAll(askResponseEventPublishRepository.findByProcessStatus(ProcessStatus.NEW));
        unpublishedEvents.addAll(notifyEventPublishRepository.findByProcessStatus(ProcessStatus.NEW));
        unpublishedEvents.addAll(revokeAskEventPublishRepository.findByProcessStatus(ProcessStatus.NEW));
        return unpublishedEvents;
    }

    public AskRequestEventPublish getAskRequestEventByEventId(String eventId) {
        AskRequestEventPublish askRequestEventPublish = askRequestEvnetPulishRepository.findByEventId(eventId);
        if(askRequestEventPublish == null) {
            throw new EventException(String.format("根据事件ID[%s]没有找到AskRequestEventPublish", eventId));
        }
        return askRequestEventPublish;
    }

    public List<AskRequestEventPublish> findAskRequestEventByEventId(List<String> askEventIds) {
        CustomPreconditions.assertNotGreaterThanMaxQueryBatchSize(askEventIds.size());
        Map<String, AskRequestEventPublish> map = askRequestEvnetPulishRepository.findByEventIdIn(askEventIds)
                .stream().collect(Collectors.toMap(AskRequestEventPublish::getEventId, Function.identity()));
        Set<String> eventNotExistIdSet = new HashSet<>();

        List<AskRequestEventPublish> askRequestEventPublisheList = askEventIds.stream().map(eventId -> {
            AskRequestEventPublish p = map.get(eventId);
            if(p == null) {
                eventNotExistIdSet.add(eventId);
            }
            return p;
        }).collect(Collectors.toList());
        if(!eventNotExistIdSet.isEmpty()) {
            throw new EventException(String.format("根据事件ID[%s]没有找到AskRequestEventPublish", eventNotExistIdSet));
        }
        return askRequestEventPublisheList;
    }
}
