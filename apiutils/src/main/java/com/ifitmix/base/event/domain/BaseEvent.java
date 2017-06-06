package com.ifitmix.base.event.domain;

import com.ifitmix.base.constants.EventType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 子类必须定义static变量EVENT_TYPE
 *
 * 例如：public static final EventType EVENT_TYPE = EventType.TEST_EVENT;
 *
 * Created by zhangtao on 2017/4/21.
 */
public abstract class BaseEvent {

    protected String id;

    protected LocalDateTime createTime;

    public BaseEvent() {
        createTime = LocalDateTime.now();
    }

    public abstract EventType getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof BaseEvent)) return false;
        BaseEvent baseEvent = (BaseEvent) o;
        return Objects.equals(id, baseEvent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


}
