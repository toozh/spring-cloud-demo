package com.ifitmix.sports.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.event.domain.NotifyEvent;

/**
 * Created by zhangtao on 2017/5/3.
 */
public class UserCreate extends NotifyEvent {

    public static final EventType EVENT_TYPE = EventType.USER_CREATE;

    @Override
    public EventType getType() {
        return EVENT_TYPE;
    }

    private Long uid;

    private String message;

    @JsonCreator
    public UserCreate(@JsonProperty("uid") Long uid,
                      @JsonProperty("message") String message) {
        this.uid = uid;
        this.message = message;
    }

    public Long getUid() {
        return uid;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return " UserCreate {" +
                " uid = " + uid +
                ", message = " + message +
                "} " + super.toString();
    }
}
