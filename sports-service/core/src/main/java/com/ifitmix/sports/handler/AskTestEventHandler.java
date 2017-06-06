package com.ifitmix.sports.handler;

import com.ifitmix.base.api.BooleanWrapper;
import com.ifitmix.base.constants.FailureInfo;
import com.ifitmix.common.event.handler.RevokableAskEventHandler;
import com.ifitmix.sports.api.event.AskTestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangtao on 2017/5/2.
 */
public class AskTestEventHandler implements RevokableAskEventHandler<AskTestEvent> {

    private static Logger logger = LoggerFactory.getLogger(AskTestEventHandler.class);

    @Override
    public BooleanWrapper processRequest(AskTestEvent event) {
        logger.info(" 正在处理 AskTestEventHandler " + event.toString());
        if (event.getUid() == 100L) {
            return new BooleanWrapper(false, "uid = 100");
        }
        return new BooleanWrapper(true);
    }


    @Override
    public void processRevoke(AskTestEvent originEvent, FailureInfo failureInfo) {

    }
}
