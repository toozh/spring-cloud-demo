package com.ifitmix.user.callback;

import com.ifitmix.base.constants.FailureInfo;
import com.ifitmix.sports.api.event.AskTestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by zhangtao on 2017/5/2.
 */
public class AskTestEventCallback {

    private static Logger logger = LoggerFactory.getLogger(AskTestEventCallback.class);

    public void onSuccess(AskTestEvent askTestEvent, String testId) {
        logger.info(" 正在执行 AskTestEventCallback onSuccess " + askTestEvent.toString() + " id = " + testId);
    }

    public void onFailure(AskTestEvent askTestEvent, String testId, FailureInfo failureInfo) {
        logger.info(" 正在执行 AskTestEventCallback onFailure " + askTestEvent.toString() + " id = " + testId + " FailureInfo = " + failureInfo.toString());
    }
}
