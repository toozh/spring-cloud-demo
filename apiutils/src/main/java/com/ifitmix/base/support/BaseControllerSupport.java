package com.ifitmix.base.support;

import com.ifitmix.base.api.ResponseEntity;

/**
 * Created by zhangtao on 2017/4/1.
 * 控制器基类
 */
public abstract class BaseControllerSupport {

    public ResponseEntity createResponseEntity(Object results) {
        return new ResponseEntity(results);
    }

}
