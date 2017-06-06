package com.ifitmix.sports.dao;

import com.ifitmix.sports.domain.UserRun;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangtao on 2017/3/9.
 */
@Repository
public interface UserRunRepository extends MongoRepository<UserRun, Long> {

    UserRun findUserRunByStartTimeAndUid(Long startTime, Integer uid);

    Page<UserRun> findUserRunByUid(Integer uid, Pageable pageable);

    @Query(value = "{ uid: ?0, distance: {'$gt': ?1, '$lt': 1000} }")
    Page<UserRun> findUserRunByUidAndDistance(Integer uid, Integer distance, Pageable pageable);


}
