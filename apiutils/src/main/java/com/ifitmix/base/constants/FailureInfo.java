package com.ifitmix.base.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Created by zhangtao on 2017/4/21.
 */
public class FailureInfo {

    private FailureReason failureReason;

    private Long failureTime;

    private String message;

    public FailureInfo(FailureReason failureReason, Long failureTime) {
        this(failureReason, failureTime, null);
    }

    @JsonCreator
    public FailureInfo(@JsonProperty("reason") FailureReason failureReason,
                       @JsonProperty("failureTime") Long failureTime,
                       @JsonProperty("message") String message) {
        this.failureReason = failureReason;
        this.failureTime = failureTime;
        this.message = message;
    }


    public FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public Long getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(Long failureTime) {
        this.failureTime = failureTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FailureInfo{" +
                "reason=" + failureReason +
                ", failureTime=" + failureTime +
                ", message='" + message + "\'" +
                "}";
    }

}
