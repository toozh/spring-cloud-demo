package com.ifitmix.club.dao;

import com.ifitmix.club.domain.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by zhangtao on 2017/5/10.
 */
@Repository
public interface ClubRepository extends MongoRepository<Club, Long> {

    List<Club> findClubByUid(Integer id);

    Club findClubByUidAndName(Integer uid, String name);

    Page<Club> findClubByUid(Integer uid, Pageable pageable);

    List<Club> findClubByIdIn(Collection<Long> clubIds);

    Club findClubByIdAndUid(Long clubId, Integer uid);
}
