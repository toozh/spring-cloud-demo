package com.ifitmix.club.dao;

import com.ifitmix.club.domain.ClubMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangtao on 2017/5/18.
 */
@Repository
public interface ClubMessageRepository extends MongoRepository<ClubMessage, Long> {
    Page<ClubMessage> findClubMessageByClubId(Long clubId, Pageable pageable);
}
