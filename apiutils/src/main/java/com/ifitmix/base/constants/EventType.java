package com.ifitmix.base.constants;

/**
 * Created by zhangtao on 2017/4/21.
 */
public enum  EventType {

    /**
     * 俱乐部消息事件
     */
    PUSH_CLUB_MESSAGE,
    /**
     * 俱乐部公告事件
     */
    PUSH_CLUB_NOTICE,

    USER_CREATE,

    ASK_RESPONSE,

    REVOKE_ASK,

    ASK_TEST_EVENT,

    TEST_EVENT;

    public static EventType valueOfIgnoreCase(String name) {
        if(name == null) {
            return null;
        }
        return valueOf(name.toUpperCase());
    }

}
