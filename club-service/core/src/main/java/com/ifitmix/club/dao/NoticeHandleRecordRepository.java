package com.ifitmix.club.dao;

import com.ifitmix.club.domain.ClubMember;
import com.ifitmix.club.domain.NoticeHandleRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangtao on 2017/5/10.
 */
@Repository
public interface NoticeHandleRecordRepository extends MongoRepository<NoticeHandleRecord, Long> {

}
