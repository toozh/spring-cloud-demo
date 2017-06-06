package com.ifitmix.common.event.dao;

import com.ifitmix.common.event.constant.ProcessStatus;
import com.ifitmix.common.event.domain.EventWatchProcess;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by zhangtao on 2017/4/28.
 */
public interface EventWatchProcessRepository extends MongoRepository<EventWatchProcess, Long> {

    List<EventWatchProcess> findByProcessStatus(ProcessStatus processStatus);
}
