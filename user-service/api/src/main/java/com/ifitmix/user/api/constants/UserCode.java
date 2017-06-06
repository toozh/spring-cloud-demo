package com.ifitmix.user.api.constants;

import com.ifitmix.common.constants.AppConstants;

/**
 * Created by zhangtao on 2017/5/12.
 */
public enum UserCode {

    LOGIN_USER_NOT_EXIST(2000, "该用户不存在", "Users do not exist");

    private Integer code;

    private String msgChina;

    private String msgEnglish;

    UserCode(Integer code, String msgChina, String msgEnglish) {
        this.code = code;
        this.msgChina = msgChina;
        this.msgEnglish = msgEnglish;
    }

    public Integer getCode() {
        return code;
    }

    @SuppressWarnings("Duplicates")
    public String getMessage(String language) {
        if(language.equals(AppConstants.LANGUAGE_CH)) {
            return msgChina;
        } else if(language.equals(AppConstants.LANGUAGE_EN)) {
            return msgEnglish;
        } else {
            return msgChina;
        }
    }
}
