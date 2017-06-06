package com.ifitmix.common.event.constant;

/**
 * Created by zhangtao on 2017/4/27.
 */
public enum ProcessStatus {

    NEW("未处理"),

    PROCESSING("处理中"),

    PROCESSED("已处理"),

    IGNORE("忽略");

    public String desc;

    ProcessStatus(String desc) {
        this.desc = desc;
    }

}
