package com.ifitmix.sports.handler;

import com.ifitmix.common.event.handler.NotifyEventHandler;
import com.ifitmix.sports.api.event.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangtao on 2017/4/24.
 */
public class TestEventHandler implements NotifyEventHandler<TestEvent> {

    protected Logger logger = LoggerFactory.getLogger(TestEventHandler.class);

    @Override
    public void notify(TestEvent event) {
        logger.info("正在处理" + event.toString());
    }
}
