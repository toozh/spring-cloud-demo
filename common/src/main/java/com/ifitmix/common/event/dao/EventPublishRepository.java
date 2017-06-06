package com.ifitmix.common.event.dao;

import com.ifitmix.common.event.constant.ProcessStatus;
import com.ifitmix.common.event.domain.EventPublish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by zhangtao on 2017/4/28.
 */
@NoRepositoryBean
public interface EventPublishRepository<T extends EventPublish> extends MongoRepository<T, Long> {

    List<T> findByProcessStatus(ProcessStatus processStatus);

    T findByEventId(String eventId);

    List<T> findByEventIdIn(List<String> eventIds);

}
