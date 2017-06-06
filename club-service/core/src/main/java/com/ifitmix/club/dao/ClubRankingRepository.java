package com.ifitmix.club.dao;

import com.ifitmix.club.domain.ClubRanking;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangtao on 2017/5/19.
 */
public interface ClubRankingRepository extends MongoRepository<ClubRanking, Long> {
    List<ClubRanking> findByClubIdAndTypeAndAddTimeBetween(Long clubId, Integer type, Long beginDay, Long endDay, Sort sort);
}
