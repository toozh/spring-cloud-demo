package com.ifitmix.base.constants;

/**
 * Created by zhangtao on 2017/4/21.
 */
public enum FailureReason {

    TIMEOUT("事件超时"),

    FAILED("事件失败"),

    CANCELLED("事件取消");

    private String desc;

    FailureReason(String desc) {
        this.desc = desc;
    }

}
