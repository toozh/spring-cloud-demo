package com.ifitmix.club.dao;

import com.ifitmix.club.domain.ClubNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangtao on 2017/5/19.
 */
@Repository
public interface ClubNoticeRepository extends MongoRepository<ClubNotice, Long> {
    Page<ClubNotice> findClubNoticeByClubIdAndStatus(Long clubId, Integer status,Pageable pageable);

    ClubNotice findClubNoticeByIdAndUidAndClubIdAndStatus(Long id, Integer uid, Long clubId, int status);
}
