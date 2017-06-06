package com.ifitmix.common.event.dao;

import com.ifitmix.common.event.constant.ProcessStatus;
import com.ifitmix.common.event.domain.EventProcess;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by zhangtao on 2017/4/28.
 */
public interface EventProcessRepository extends MongoRepository<EventProcess, Long> {
    List<EventProcess> findByProcessStatus(ProcessStatus processStatus);

    EventProcess getByEventId(String eventId);
}
