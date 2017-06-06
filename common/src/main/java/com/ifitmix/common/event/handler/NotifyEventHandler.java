package com.ifitmix.common.event.handler;

import com.ifitmix.base.event.domain.NotifyEvent;

/**
 * Created by zhangtao on 2017/4/21.
 */
public interface NotifyEventHandler<E extends NotifyEvent> {

    void notify(E event);

}
