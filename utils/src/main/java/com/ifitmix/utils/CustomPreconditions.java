package com.ifitmix.utils;

import com.ifitmix.base.Constants;
import com.ifitmix.base.api.CommonErrorCode;
import com.ifitmix.base.exception.AppBusinessException;

/**
 * Created by zhangtao on 2017/4/28.
 */
public class CustomPreconditions {

    public static void assertNotGreaterThanMaxQueryBatchSize(int size) {
        if(size > Constants.MAX_BATCH_QUERY_SIZE) {
            throw new AppBusinessException(CommonErrorCode.BAD_REQUEST, "一次查询的Id数量不能超过" + Constants.MAX_BATCH_QUERY_SIZE);
        }
    }
}
