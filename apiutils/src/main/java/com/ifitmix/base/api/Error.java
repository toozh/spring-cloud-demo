package com.ifitmix.base.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zhangtao on 2017/3/19.
 */
public class Error {

    private Integer code;

    private String message;

    private String requestUri;

    @JsonCreator
    public Error(@JsonProperty("code") Integer code,
                 @JsonProperty("requestUri") String requestUri,
                 @JsonProperty(value = "message", defaultValue = "") String message) {
        this.code = code;
        this.requestUri = requestUri;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestUri() {
        return requestUri;
    }

    @Override
    public String toString() {
        return  "Error{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", requestUri='" + requestUri + '\'' +
                '}';
    }
}
