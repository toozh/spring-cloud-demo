package com.ifitmix.common.event.domain;

import com.ifitmix.base.constants.EventType;
import com.ifitmix.common.event.constant.EventCategory;
import com.ifitmix.common.event.constant.ProcessStatus;
import com.ifitmix.common.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by zhangtao on 2017/4/28.
 */
@Document(collection = "EventProcess")
public class EventProcess {

    @Id
    @GeneratedValue
    private Long id;

    private String payload;

    private ProcessStatus processStatus = ProcessStatus.NEW;

    private EventCategory eventCategory;

    private String eventId;

    private EventType eventType;

    private Long addTime;

    private Long modifyTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }
}
