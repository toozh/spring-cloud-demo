package com.ifitmix.sports.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.event.domain.AskEvent;
import com.ifitmix.base.event.domain.Revokable;

/**
 * Created by zhangtao on 2017/5/2.
 */
public class AskTestEvent extends AskEvent implements Revokable {

    public static final EventType EVENT_TYPE = EventType.ASK_TEST_EVENT;

    @Override
    public EventType getType() {
        return EVENT_TYPE;
    }

    private Long uid;

    private String message;

    @JsonCreator
    public AskTestEvent(@JsonProperty("uid") Long uid,
                        @JsonProperty("message") String message ) {
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
        return "TestEvent{" +
                "userId=" + uid +
                ", username=\'" + message + "\'" +
                "} " + super.toString();
    }
}
