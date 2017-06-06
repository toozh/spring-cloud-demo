package com.ifitmix.common.event.config;

import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.event.domain.AskResponseEvent;
import com.ifitmix.base.event.domain.RevokeAskEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangtao on 2017/4/24.
 */
public class InitBindProducer implements InitializingBean {

    @Autowired
    private BinderAwareChannelResolver binderAwareChannelResolver;

    private Set<EventType> preInitializeProducers = new HashSet<>();

    public InitBindProducer() {
        preInitializeProducers.add(AskResponseEvent.EVENT_TYPE);
        preInitializeProducers.add(RevokeAskEvent.EVENT_TYPE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        preInitializeProducers.stream().forEach(x -> binderAwareChannelResolver.resolveDestination(x.name()));
    }

    public void addProInitializeProducers(EventType eventType) {
        preInitializeProducers.add(eventType);
    }

}
