package com.ifitmix.common.exception;

import com.ifitmix.base.exception.BaseException;

/**
 * Created by zhangtao on 2017/4/21.
 */
public class EventException extends BaseException {

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventException(Throwable cause) {
        super(cause);
    }

    protected EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
