package com.ifitmix.common.event.domain;

import com.ifitmix.common.event.constant.AskEventStatus;
import com.ifitmix.common.mongo.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangtao on 2017/4/28.
 */
@Document(collection = "EventWatch")
public class EventWatch {

    @Id
    @GeneratedValue
    private Long id;

    private AskEventStatus askEventStatus;

    private String extraParams;

    private String askEventIds;

    private String callbackClass;

    private boolean united;

    private Long timeoutTime;

    private Long addTime;

    private Long modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AskEventStatus getAskEventStatus() {
        return askEventStatus;
    }

    public void setAskEventStatus(AskEventStatus askEventStatus) {
        this.askEventStatus = askEventStatus;
    }

    public String getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(String extraParams) {
        this.extraParams = extraParams;
    }

    public List<String> getAskEventIds() {
        return Arrays.asList(askEventIds.split(","));
    }

    public void setAskEventIds(List<String> askEventIds) {
        this.askEventIds = String.join(",", askEventIds);
    }

    public String getCallbackClass() {
        return callbackClass;
    }

    public void setCallbackClass(String callbackClass) {
        this.callbackClass = callbackClass;
    }

    public boolean isUnited() {
        return united;
    }

    public void setUnited(boolean united) {
        this.united = united;
    }

    public Long getTimeoutTime() {
        return timeoutTime;
    }

    public void setTimeoutTime(Long timeoutTime) {
        this.timeoutTime = timeoutTime;
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
