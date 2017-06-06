package com.ifitmix.club.service;

import com.ifitmix.club.dao.ClubUserRunStatRepository;
import com.ifitmix.club.domain.ClubUserRunStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by zhangtao on 2017/5/12.
 */
@Service
public class ClubUserRunStatService {

    @Autowired
    private ClubUserRunStatRepository clubUserRunStatRepository;


    public ClubUserRunStat findClubUserRunStatByUidAndTypeAndAddTimeBetween(Integer uid, Integer type, Long beginTime, Long endTime) {
        return clubUserRunStatRepository.findClubUserRunStatByUidAndTypeAndAddTimeBetween(uid, type, beginTime, endTime);
    }


    public Page<ClubUserRunStat> findByUidInAndTypeAndAddTimeBetween(Set<Integer> memberUidSet, int type, long beginTime, long endTime, Pageable pageable) {
        return clubUserRunStatRepository.findByUidInAndTypeAndAddTimeBetween(memberUidSet, type, beginTime, endTime, pageable);
    }
}
