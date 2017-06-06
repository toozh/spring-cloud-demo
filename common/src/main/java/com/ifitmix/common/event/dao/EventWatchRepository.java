package com.ifitmix.common.event.dao;

import com.ifitmix.common.event.constant.AskEventStatus;
import com.ifitmix.common.event.domain.EventWatch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by zhangtao on 2017/4/28.
 */
public interface EventWatchRepository extends MongoRepository<EventWatch, Long> {
    List<EventWatch> findByAskEventStatusAndTimeoutTimeLessThan(AskEventStatus pending, Long timeout);
}
