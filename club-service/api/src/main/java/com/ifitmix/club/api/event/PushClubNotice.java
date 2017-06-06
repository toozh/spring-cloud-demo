package com.ifitmix.club.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifitmix.base.constants.EventType;
import com.ifitmix.base.event.domain.NotifyEvent;

/**
 * Created by zhangtao on 2017/5/19.
 */
public class PushClubNotice extends NotifyEvent {

    public static final EventType EVENT_TYPE = EventType.PUSH_CLUB_NOTICE;

    @Override
    public EventType getType() {
        return EVENT_TYPE;
    }

    private Long clubId;

    private Integer uid;

    private Long clubNoticeId;

    @JsonCreator
    public PushClubNotice(@JsonProperty("clubId") Long clubId,
                           @JsonProperty("uid") Integer uid,
                           @JsonProperty("clubNoticeId") Long clubNoticeId) {
        this.clubId = clubId;
        this.uid = uid;
        this.clubNoticeId = clubNoticeId;
    }

    public Long getClubId() {
        return clubId;
    }

    public Integer getUid() {
        return uid;
    }

    public Long getClubNoticeId() {
        return clubNoticeId;
    }

    @Override
    public String toString() {
        return "PushClubNotice{" +
                "clubId = " + clubId +
                ", uid = " + uid +
                ", clubNoticeId = " + clubNoticeId +
                "}" + super.toString();
    }

}
