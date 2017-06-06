package com.ifitmix.common.event.handler;

import com.ifitmix.base.constants.FailureInfo;
import com.ifitmix.base.event.domain.AskEvent;
import com.ifitmix.base.event.domain.Revokable;

/**
 * Created by zhangtao on 2017/4/21.
 */
public interface RevokableAskEventHandler<E extends AskEvent & Revokable> extends AskEventHandler<E> {

    void processRevoke(E originEvent, FailureInfo failureInfo);

}
