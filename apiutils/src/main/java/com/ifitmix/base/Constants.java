package com.ifitmix.base;

/**
 * Created by zhangtao on 2017/4/28.
 */
public class Constants {
    /**
     * 终端类型
     */
    public static final int TERMINAL_NULL = 0;
    public static final int TERMINAL_IOS = 1;
    public static final int TERMINAL_ANDROID = 2;
    public static final int TERMINAL_WEB = 3;

    public static final int ASK_TIMEOUT = 30000;

    public static final int MAX_BATCH_QUERY_SIZE = 100;
    /// 运动类型
    /**
     *  未知情况下：0
     */
    public static final int RUN_TYPE_UNKNOWN = 0;
    /**
     * 跑步
     * <strong>
     *     老版本 0 未知，也记为 跑步
     * </strong>
     */
    public static final int RUN_TYPE_RUN = 1;
    /**
     * 跳绳
     */
    public static final  int RUN_TYPE_SKIP_ROPE = 2;

    /// 俱乐部
    /**
     * 俱乐部状态：有效
     */
    public static final int CLUB_STATUS_NORMAL = 0;
    /**
     * 俱乐部状态：无效
     */
    public static final int CLUB_STATUS_INVALID = 1;
    /**
     * 俱乐部成员状态：有效
     */
    public static final int CLUB_MEMBER_STATUS_NORMAL = 0;
    /**
     * 俱乐部成员状态：无效
     */
    public static final  int CLUB_MEMBER_STATUS_INVALID = 1;

    public static final int CLUB_MESSAGE_TYPE_EMPTY = 0;

    public static final int CLUB_MESSAGE_TYPE_TEXT = 1;

    public static final int CLUB_MESSAGE_TYPE_VOICE = 2;

    public static final int CLUB_MESSAGE_TYPE_IMG = 3;

    public static final  int CLUB_MESSAGE_TYPE_LINK = 4;


    public static final int CLUB_NOTICE_STATUS_NORMAL = 0;
    public static final int CLUB_NOTICE_STATUS_INVALID = 1;

    public static final int CLUB_RANK_TYPE_DAY = 1;
    public static final int CLUB_RANK_TYPE_WEEK = 2;
    public static final int CLUB_RANK_TYPE_MONTH = 3;

    public static final int MSG_TYPE_EMAIL = 1;
    public static final int MSG_TYPE_SMS= 2;
    public static final int MSG_TYPE_PUSH = 3;
}
