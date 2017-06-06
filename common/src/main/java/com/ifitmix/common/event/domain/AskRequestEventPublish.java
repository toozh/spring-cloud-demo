package com.ifitmix.common.event.domain;

import com.ifitmix.common.event.constant.AskEventStatus;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zhangtao on 2017/4/28.
 */
@Document(collection = "AskRequestEventPublish")
public class AskRequestEventPublish extends EventPublish {

    private AskEventStatus askEventStatus;

    private Long watchId;

    public AskEventStatus getAskEventStatus() {
        return askEventStatus;
    }

    public void setAskEventStatus(AskEventStatus askEventStatus) {
        this.askEventStatus = askEventStatus;
    }

    public Long getWatchId() {
        return watchId;
    }

    public void setWatchId(Long watchId) {
        this.watchId = watchId;
    }
}
