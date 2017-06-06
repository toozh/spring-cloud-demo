package com.ifitmix.club.api.constants;

import com.ifitmix.common.constants.AppConstants;

/**
 * Created by zhangtao on 2017/5/11.
 */
public enum ClubCode {

    CLUB_NAME_REPEAT(5001, "俱乐部名称已存在", "Club name already exist"),

    CLUB_NOT_EXIST(5002, "该俱乐部不存在", "The club does not exist"),

    CLUB_IS_CLUB_MEMBER(5003, "您已经是俱乐部成员了", "Is a member of the club"),

    CLUB_RECOVERY_MEMBER(5004, "欢迎再次加入", "Welcome to join again"),

    CLUB_MEMBER_IS_REMOVE(5005, "成员已被移除", "Members have been removed"),

    CLUB_INSUFFICIENT_AUTHORITY(5006, "您的权限不足", "Your lack of authority"),

    CLUB_CREATE_USER_NOT_QUIT(5007, "您是俱乐部创建者不能退出", "You are the creator of the club can not quit"),

    CLUB_JION_ERROR_NOT_EXIST(5008, "加入失败或俱乐部不存在", "Join in the failure,the club does not exist"),

    CLUB_JOIN_ERROR_THE_REMOVE(5009, "加入失败或俱乐部已删除", "Join in the failure,the club has been removed"),

    CLUB_ARE_NOT_MEMBER(5010, "您已不是俱乐部成员", "You are not a member of the club"),

    CLUB_JOIN_MEMBER_LIMIT(5011, "俱乐部成员数量达到上限了", "Club members reach the ceiling"),

    CLUB_NOTICE_NOT_EXIST(5012, "俱乐部公告不存在", "Club notice not exist");

    private Integer code;

    private String msgChina;

    private String msgEnglish;

    ClubCode(Integer code, String msgChina, String msgEnglish) {
        this.code = code;
        this.msgChina = msgChina;
        this.msgEnglish = msgEnglish;
    }

    @SuppressWarnings("Duplicates")
    public String getMessage(String language) {
        if (language.equals(AppConstants.LANGUAGE_CH)) {
            return msgChina;
        } else if(language.equals(AppConstants.LANGUAGE_EN)) {
            return msgEnglish;
        } else {
            return msgChina;
        }
    }

    public Integer getCode() {
        return code;
    }

    public String getMsgChina() {
        return msgChina;
    }

    public String getMsgEnglish() {
        return msgEnglish;
    }
}
