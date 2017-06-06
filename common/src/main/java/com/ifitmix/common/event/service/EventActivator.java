package com.ifitmix.common.event.service;

import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.event.domain.AskResponseEvent;
import com.ifitmix.common.event.EventRegistry;
import com.ifitmix.common.event.EventUtils;
import com.ifitmix.common.event.constant.EventCategory;
import com.ifitmix.common.exception.EventException;
import com.ifitmix.common.scheduler.ZkSchedulerCoordinator;
import com.ifitmix.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by zhangtao on 2017/4/24.
 */
@EnableBinding(Processor.class)
@DependsOn("bindingService")
@EnableIntegration
public class EventActivator {

    private static Logger logger = LoggerFactory.getLogger(EventActivator.class);

    @Autowired
    private EventBus eventBus;

    @Autowired
    private BinderAwareChannelResolver binderAwareChannelResolver;

    @ServiceActivator(inputChannel = Processor.INPUT)
    public void receiveMessage(Object payload) {
        byte[] bytes = (byte[]) payload;
        String message = new String(bytes, Charset.forName("UTF-8"));
        System.out.println(" 接收到的消息 ： " + message);
        try {
            // 记录消息事件
            eventBus.recordEvent(message);
        } catch (DataIntegrityViolationException e) {
            logger.warn(String.format("event[%s]在数据库已存在，errorMsg: %s", message, e.getMessage()));
        } catch (EventException e) {
            logger.error("receiveMessage在保存event的时候发现payload格式不正确: " + e.getMessage());
        } catch (Exception e) {
            logger.error("receiveMessage在保存event的时候发生错误", e);
            throw e;
        }


    }

    /**
     * 发送消息
     * @param message
     * @param destination
     * @return
     */
    public boolean sendMessage(String message, String destination) {
        if(logger.isDebugEnabled()) {
            logger.debug("send message to kafka topic: {}, message: {}", destination, message);
        }
        MessageChannel messageChannel = binderAwareChannelResolver.resolveDestination(destination);
        byte[] payload = message.getBytes(Charset.forName("UTF-8"));
        return messageChannel.send(MessageBuilder.withPayload(payload).build(), 1000L);
    }



}
