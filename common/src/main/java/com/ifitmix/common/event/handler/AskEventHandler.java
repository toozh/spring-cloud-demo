package com.ifitmix.common.event.handler;

import com.ifitmix.base.api.BooleanWrapper;
import com.ifitmix.base.event.domain.AskEvent;

/**
 * Created by zhangtao on 2017/4/21.
 */
public interface AskEventHandler<E extends AskEvent> {

    BooleanWrapper processRequest(E event);

}
