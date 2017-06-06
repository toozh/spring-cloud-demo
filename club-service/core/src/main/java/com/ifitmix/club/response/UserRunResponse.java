package com.ifitmix.club.response;

import com.ifitmix.sports.api.dtos.UserRunDto;

/**
 * Created by zhangtao on 2017/5/16.
 */
public class UserRunResponse {

    private Integer code;

    private String message;

    private String k;

    private Long st;

    private UserRunDto results;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public Long getSt() {
        return st;
    }

    public void setSt(Long st) {
        this.st = st;
    }

    public UserRunDto getResults() {
        return results;
    }

    public void setResults(UserRunDto results) {
        this.results = results;
    }
}
