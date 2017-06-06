package com.ifitmix.common.event.domain;

import com.ifitmix.base.constants.EventType;
import com.ifitmix.common.event.constant.ProcessStatus;
import com.ifitmix.common.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;

/**
 * Created by zhangtao on 2017/4/28.
 */
public abstract class EventPublish {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 事件内容
     */
    private String payload;

    /**
     * 事件进度
     */
    private ProcessStatus processStatus = ProcessStatus.NEW;

    /**
     * 事件Id
     */
    private String eventId;

    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     * 添加时间
     */
    private Long addTime;

    /**
     * 修改时间
     */
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

