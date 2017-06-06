package com.ifitmix.base.event.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.base.constants.EventType;

/**
 * Created by zhangtao on 2017/4/21.
 */
public class AskResponseEvent extends BaseEvent {

    public static final EventType EVENT_TYPE = EventType.ASK_RESPONSE;

    @Override
    public EventType getType() {
        return EVENT_TYPE;
    }

    private boolean success;

    private String message;

    private String askEventId;

    @JsonCreator
    public AskResponseEvent(@JsonProperty("success") boolean success,
                            @JsonProperty("message") String message,
                            @JsonProperty("askEventId") String askEventId) {
        this.success = success;
        this.message = message;
        this.askEventId = askEventId;

    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getAskEventId() {
        return askEventId;
    }
}
