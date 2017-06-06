package com.ifitmix.club.dao;

import com.ifitmix.club.domain.ClubUserRunStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by zhangtao on 2017/5/12.
 */
@Repository
public interface ClubUserRunStatRepository extends MongoRepository<ClubUserRunStat, Long> {
    ClubUserRunStat findClubUserRunStatByUidAndTypeAndAddTimeBetween(Integer uid, Integer type, Long beginTime, Long endTime);

    Page<ClubUserRunStat> findByUidInAndTypeAndAddTimeBetween(Set<Integer> memberUidSet, int type, long beginTime, long endTime, Pageable pageable);
}
