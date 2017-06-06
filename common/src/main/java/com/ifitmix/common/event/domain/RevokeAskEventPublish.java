package com.ifitmix.common.event.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zhangtao on 2017/4/28.
 */
@Document(collection = "RevokeAskEventPublish")
public class RevokeAskEventPublish extends EventPublish {

    private String askEventId;

    public String getAskEventId() {
        return askEventId;
    }

    public void setAskEventId(String askEventId) {
        this.askEventId = askEventId;
    }
}
