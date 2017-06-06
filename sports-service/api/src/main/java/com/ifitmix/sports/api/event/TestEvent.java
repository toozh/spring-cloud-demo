package com.ifitmix.sports.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.event.domain.NotifyEvent;

/**
 * Created by zhangtao on 2017/4/24.
 */
public class TestEvent extends NotifyEvent {

    public static final EventType EVENT_TYPE = EventType.TEST_EVENT;

    @Override
    public EventType getType() {
        return EVENT_TYPE;
    }

    private Long userId;

    private String username;

    @JsonCreator
    public TestEvent(@JsonProperty("userId") Long userId,
                     @JsonProperty("username") String username) {
        this.userId = userId;
        this.username = username;
    }


    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
                "userId=" + userId +
                ", username=\'" + username + "\'" +
                "} " + super.toString();
    }

}
