package com.ifitmix.club.service;

import com.ifitmix.club.dao.NoticeHandleRecordRepository;
import com.ifitmix.club.domain.NoticeHandleRecord;
import com.ifitmix.common.mongo.BaseMongoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Created by zhangtao on 2017/6/5.
 */
@Service
public class NoticeHandleRecordService extends BaseMongoRepositoryImpl<NoticeHandleRecord, Long> {

    @Autowired
    private NoticeHandleRecordRepository noticeHandleRecordRepository;

    @Autowired
    private MongoOperations mongoOperations;






    @Override
    protected MongoRepository getMongoRepository() {
        return noticeHandleRecordRepository;
    }
}
