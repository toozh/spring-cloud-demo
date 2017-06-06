package com.ifitmix.base.exception;

import com.ifitmix.base.api.Error;

/**
 * Created by zhangtao on 2017/3/19.
 */
public class RemoteCallException extends AppBusinessException {

    private Error originError;

    public RemoteCallException(Error error, int httpStatus) {
        super(error.getCode(), httpStatus, "调用远程服务异常, cause: " + error.getMessage());
        this.originError = error;
    }

    public Error getOriginError() {
        return originError;
    }
}
