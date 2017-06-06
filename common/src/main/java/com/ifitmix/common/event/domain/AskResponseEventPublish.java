package com.ifitmix.common.event.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zhangtao on 2017/4/28.
 */
@Document(collection = "AskResponseEventPublish")
public class AskResponseEventPublish extends EventPublish {

    private boolean success;

    private String askEventId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAskEventId() {
        return askEventId;
    }

    public void setAskEventId(String askEventId) {
        this.askEventId = askEventId;
    }
}
