package com.ifitmix.user.handler;

import com.ifitmix.common.event.handler.NotifyEventHandler;
import com.ifitmix.sports.api.event.UserCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangtao on 2017/5/3.
 */
public class UserCreateHandler implements NotifyEventHandler<UserCreate> {

    private static Logger logger = LoggerFactory.getLogger(UserCreateHandler.class);

    @Override
    public void notify(UserCreate event) {
        logger.info(" 正在处理 USER_CREATE " + event.toString());
    }
}
