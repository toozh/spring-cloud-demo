package com.ifitmix.common.event.dao;

import com.ifitmix.common.event.domain.AskResponseEventPublish;

/**
 * Created by zhangtao on 2017/4/28.
 */
public interface AskResponseEventPublishRepository extends EventPublishRepository<AskResponseEventPublish> {
    Long countByAskEventId(String askEventId);
}
