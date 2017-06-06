package com.ifitmix.base.exception;

import com.ifitmix.base.api.CommonErrorCode;
import com.ifitmix.base.api.ErrorCode;

/**
 * Created by zhangtao on 2017/3/19.
 */
public class AppBusinessException extends BaseException {

    private static final ErrorCode DEFAULT_CODE = CommonErrorCode.INTERNAL_ERROR;

    private Integer code = DEFAULT_CODE.getCode();

    //类似Http状态码
    private int httpStatus = DEFAULT_CODE.getStatus();

    public AppBusinessException(Integer code, int httpStatus, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public AppBusinessException(String message) {
        super(message);
    }

    /**
     * @param errorCode 状态码, 这个字段会在错误信息里返回给客户端.
     * @param message
     */
    public AppBusinessException(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), errorCode.getStatus(), message);
    }

    public AppBusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public AppBusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public Integer getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

}
