package com.ifitmix.base.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zhangtao on 2017/4/21.
 */
public class BooleanWrapper {

    private boolean success;

    private String message;

    public BooleanWrapper(boolean success) {
        this(success, null);
    }

    @JsonCreator
    public BooleanWrapper(@JsonProperty("success") boolean success,
                          @JsonProperty("message") String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
