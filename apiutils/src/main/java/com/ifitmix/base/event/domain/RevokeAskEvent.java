package com.ifitmix.base.event.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.constants.FailureInfo;

/**
 * Created by zhangtao on 2017/4/21.
 */
public class RevokeAskEvent extends BaseEvent {

    public static final EventType EVENT_TYPE = EventType.REVOKE_ASK;

    @Override
    public EventType getType() {
        return EVENT_TYPE;
    }

    private FailureInfo failureInfo;

    private String askEventId;

    @JsonCreator
    public RevokeAskEvent(@JsonProperty("failureInfo") FailureInfo failureInfo,
                          @JsonProperty("askEventId") String askEventId) {
        this.failureInfo = failureInfo;
        this.askEventId = askEventId;
    }

    public FailureInfo getFailureInfo() {
        return failureInfo;
    }

    public String getAskEventId() {
        return askEventId;
    }
}
