package com.ifitmix.club.api.event;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.event.domain.NotifyEvent;
import com.ifitmix.base.event.domain.PushBody;

import java.util.Set;

/**
 * Created by zhangtao on 2017/5/18.
 */
public class PushClubMessage extends NotifyEvent {

    public static final EventType EVENT_TYPE = EventType.PUSH_CLUB_MESSAGE;

    @Override
    public EventType getType() {
        return EVENT_TYPE;
    }

    private Integer fromUserId;

    private Set<Integer> toUserIds;

    private Long clubMessageId;

    private PushBody pushBody;

    @JsonCreator
    public PushClubMessage(@JsonProperty("fromUserId") Integer fromUserId,
                           @JsonProperty("toUserIds") Set<Integer> toUserIds,
                           @JsonProperty("clubMessageId") Long clubMessageId,
                           @JsonProperty("pushBody") PushBody pushBody) {
        this.fromUserId = fromUserId;
        this.toUserIds = toUserIds;
        this.clubMessageId = clubMessageId;
        this.pushBody = pushBody;
    }

    public PushBody getPushBody() {
        return pushBody;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public Long getClubMessageId() {
        return clubMessageId;
    }

    public Set<Integer> getToUserIds() {
        return toUserIds;
    }

    @Override
    public String toString() {
        return "PushClubMessage {" +
                "fromUserId = " + fromUserId +
                ", toUserIds = " + JSON.toJSONString(toUserIds) +
                ", clubMessageId = " + clubMessageId +
                ", pushBody = " + JSON.toJSONString(pushBody) +
                "}" + super.toString();
    }

}
