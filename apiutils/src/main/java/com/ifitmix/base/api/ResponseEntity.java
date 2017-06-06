package com.ifitmix.base.api;

import com.ifitmix.base.constants.CodeConstants;

import java.util.Date;

/**
 * Created by zhangtao on 2017/4/1.
 */
public class ResponseEntity<T> {

    private Integer code;

    private String msg;

    private String k;

    private Long st;

    private T results;

    public ResponseEntity(T results) {
        this.code = CodeConstants.SUCCESS;
        this.results = results;
        this.st = new Date().getTime();
    }

    public ResponseEntity(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.st = new Date().getTime();
    }

    public ResponseEntity() {
        this.st = new Date().getTime();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
