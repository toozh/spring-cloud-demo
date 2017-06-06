package com.ifitmix.common.scheduler.config;

import com.ifitmix.common.event.scheduler.EventScheduler;
import com.ifitmix.common.event.service.EventBus;
import com.ifitmix.common.scheduler.ZkCoordinateScheduledExecutor;
import com.ifitmix.common.scheduler.ZkSchedulerCoordinator;
import com.ifitmix.common.spring.ApplicationConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Created by zhangtao on 2017/4/18.
 */
@EnableScheduling
public class SchedulerConfiguration implements SchedulingConfigurer {


    @Bean
    public ZkSchedulerCoordinator zkSchedulerCoordinator(ApplicationConstant applicationConstant){

        return new ZkSchedulerCoordinator(applicationConstant);

    }

    @Bean
    public EventScheduler eventScheduler(EventBus eventBus, ZkSchedulerCoordinator zkSchedulerCoordinator) {
        return new EventScheduler(eventBus, zkSchedulerCoordinator);
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        TaskScheduler taskScheduler = new ConcurrentTaskScheduler(new ZkCoordinateScheduledExecutor(5));
        taskRegistrar.setTaskScheduler(taskScheduler);

    }


}
