package com.ifitmix.common.event.scheduler;

import com.ifitmix.common.event.service.EventBus;
import com.ifitmix.common.scheduler.ZkSchedulerCoordinator;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by zhangtao on 2017/4/28.
 */
public class EventScheduler {

    EventBus eventBus;

    ZkSchedulerCoordinator zkSchedulerCoordinator;

    public EventScheduler(EventBus eventBus, ZkSchedulerCoordinator zkSchedulerCoordinator) {
        this.eventBus = eventBus;
        this.zkSchedulerCoordinator = zkSchedulerCoordinator;
    }

    @Scheduled(fixedRate = 500L)
    public void sendUnpublishedEvent() {
        if(zkSchedulerCoordinator.isLeader()) {
            eventBus.sendUnpublishedEvent();
        }
    }

    @Scheduled(fixedRate = 500L)
    public void searchAndHandlerUnprocessedEvent() {
        if(zkSchedulerCoordinator.isLeader()) {
            eventBus.searchAndHandlerUnprocessedEvent();
        }
    }

    @Scheduled(fixedRate = 500L)
    public void handleUnprocessedEventWatchProcess() {
        if(zkSchedulerCoordinator.isLeader()) {
            eventBus.handleUnprocessedEventWatchProcess();
        }
    }

    @Scheduled(fixedRate = 500L)
    public void handleTimeoutEventWatch() {
        if(zkSchedulerCoordinator.isLeader()) {
            eventBus.handleTimeoutEventWatch();
        }
    }


}
