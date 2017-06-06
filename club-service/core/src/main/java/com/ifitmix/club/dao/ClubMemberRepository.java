package com.ifitmix.club.dao;

import com.ifitmix.club.domain.ClubMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangtao on 2017/5/10.
 */
@Repository
public interface ClubMemberRepository extends MongoRepository<ClubMember, Long> {

    List<ClubMember> findClubMemberByClubId(Long clubId);

    ClubMember findClubMemberByClubIdAndUid(Long clubId, Integer uid);

    Long countByClubId(Long id);

    Long countByClubIdAndStatus(Long id, Integer status);

    List<ClubMember> findClubMemberByUidAndStatusOrderByTypeAscAddTimeDesc(Integer uid, Integer statusNormal);

    List<ClubMember> findByClubIdAndStatus(Long clubId, Integer status);

    Page<ClubMember> findClubMemberByClubIdAndStatus(Long clubId, Integer status, Pageable pageable);

    List<ClubMember> findClubMemberByClubIdAndStatus(Long clubId, Integer status);
}
