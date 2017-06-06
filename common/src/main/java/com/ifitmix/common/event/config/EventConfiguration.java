package com.ifitmix.common.event.config;

import com.ifitmix.common.event.EventRegistry;
import com.ifitmix.common.spring.cloud.stream.CustomChannelBindingService;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.cloud.stream.binding.*;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by zhangtao on 2017/4/24.
 */
@EnableAsync
public class EventConfiguration extends AsyncConfigurerSupport {

    @Bean
    public EventRegistry eventRegistry() {
        return new EventRegistry();
    }

    @Bean
    public BindingService bindingService(BindingServiceProperties bindingServiceProperties,
                                         BinderFactory binderFactory, EventRegistry eventRegistry) {
        return new CustomChannelBindingService(bindingServiceProperties, binderFactory, eventRegistry);
    }

    @Bean
    public InitBindProducer initBindProducer() {
        return new InitBindProducer();
    }


    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10000);
        executor.setThreadNamePrefix("EventExecutor-");
        executor.initialize();
        return executor;
    }
}
