package com.ifitmix.club.service;

import com.google.common.collect.Maps;
import com.ifitmix.base.Constants;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.exception.AppBusinessException;
import com.ifitmix.club.api.constants.ClubCode;
import com.ifitmix.club.api.dtos.ClubMemberDto;
import com.ifitmix.club.dao.ClubMemberRepository;
import com.ifitmix.club.domain.Club;
import com.ifitmix.club.domain.ClubMember;
import com.ifitmix.club.domain.ClubUserRunStat;
import com.ifitmix.club.service.gateway.UserGateway;
import com.ifitmix.club.utils.DtoUtils;
import com.ifitmix.common.context.SystemContextHolder;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.common.mongo.BaseMongoRepositoryImpl;
import com.ifitmix.user.api.constants.UserCode;
import com.ifitmix.user.api.dtos.UserDto;
import com.ifitmix.utils.DateUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/5/10.
 */
@Service
public class ClubMemberService extends BaseMongoRepositoryImpl<ClubMember, Long> {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private UserGateway userGateway;

    @Autowired
    private ClubService clubService;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ClubUserRunStatService clubUserRunStatService;

    @Override
    protected MongoRepository<ClubMember, Long> getMongoRepository() {
        return clubMemberRepository;
    }

    public ClubMember findOne(Long id) {
        return clubMemberRepository.findOne(id);
    }

    /**
     * 添加俱乐部成员
     * @param cid
     * @param uid
     * @param memberType
     */
    public ResponseEntity addClubMember(Long cid, Integer uid, Integer memberType) {
        ClubMember clubMember = clubMemberRepository.findClubMemberByClubIdAndUid(cid, uid);
        // 判断用户是否存在
        if(userGateway.findUserById(uid) == null) {
            throw new AppBusinessException(UserCode.LOGIN_USER_NOT_EXIST.getCode(), UserCode.LOGIN_USER_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }
        // 判断俱乐部是否存在
        Club club = clubService.findClubById(cid);
        if(cid == null) {
            throw new AppBusinessException(ClubCode.CLUB_JION_ERROR_NOT_EXIST.getCode(), ClubCode.CLUB_JION_ERROR_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }
        // 俱乐部已删除
        if(club.getStatus().equals(Club.STATUS_INVALID)) {
            throw new AppBusinessException(ClubCode.CLUB_JOIN_ERROR_THE_REMOVE.getCode(), ClubCode.CLUB_JOIN_ERROR_THE_REMOVE.getMessage(SystemContextHolder.get().getLanguage()));
        }
        //已是俱乐部成员
        if(clubMember != null && clubMember.getStatus().equals(ClubMember.STATUS_NORMAL)) {
            throw new AppBusinessException(ClubCode.CLUB_IS_CLUB_MEMBER.getCode(), ClubCode.CLUB_IS_CLUB_MEMBER.getMessage(SystemContextHolder.get().getLanguage()));
        }

        //退出俱乐部，再次加入 恢复状态
        if(clubMember != null && clubMember.getStatus().equals(ClubMember.STATUS_INVALID)) {
            clubMember.setStatus(ClubMember.STATUS_NORMAL);
            clubMemberRepository.save(clubMember);
            // 更新 俱乐部成员数量和成员编号
            updateClubQuitAndJoinInfo(club, uid, 1);
            throw new AppBusinessException(ClubCode.CLUB_RECOVERY_MEMBER.getCode(), ClubCode.CLUB_RECOVERY_MEMBER.getMessage(SystemContextHolder.get().getLanguage()));
        }

        // 俱乐部成员限制
        if(null != club.getMaxMember() && clubMemberRepository.countByClubId(club.getId()) >= club.getMaxMember()) {
            throw new AppBusinessException(ClubCode.CLUB_JOIN_MEMBER_LIMIT.getCode(), ClubCode.CLUB_JOIN_MEMBER_LIMIT.getMessage(SystemContextHolder.get().getLanguage()));
        }

        // 添加成员
        ClubMember clubMemberNew = new ClubMember();
        clubMemberNew.setClubId(club.getId());
        clubMemberNew.setUid(uid);
        clubMemberNew.setStatus(ClubMember.STATUS_NORMAL);
        clubMemberNew.setType(memberType == null ? ClubMember.TYPE_MEMBER : memberType);
        clubMemberNew.setAddTime(System.currentTimeMillis());

        save(clubMemberNew);

        updateClubQuitAndJoinInfo(club, uid, 1);

        return new ResponseEntity(clubMemberNew);
    }

    /**
     * 维护俱乐部成员信息
     *
     *  1. 处理俱乐部 memberUidSet，memberCount
     *  2. 处理俱乐部 排行榜（ClubRanking）
     *
     * @param club
     * @param uid
     * @param inc
     */
    public void updateClubQuitAndJoinInfo(Club club, Integer uid, Integer inc) {
        long dayBeginTime = DateUtil.getDayBegin(new Date()).getTime();
        long dayEndTime = DateUtil.getDayEnd(new Date()).getTime();

        ClubUserRunStat clubUserRunStat = clubUserRunStatService.findClubUserRunStatByUidAndTypeAndAddTimeBetween(uid, ClubUserRunStat.TYPE_DAY, dayBeginTime, dayEndTime);
        if(inc == 1) {
            // 更新俱乐部成员
            club.getMemberUidSet().add(uid);
        } else {
            club.getMemberUidSet().remove(uid);
        }

        Query query = new Query(Criteria.where("id").is(club.getId()));
        Update update = Update.update("memberUidSet", club.getMemberUidSet()).inc("memberCount", inc);
        mongoOperations.updateFirst(query, update, Club.class);
    }

    public List<ClubMember> findByClubIdAndStatus(Long clubId, Integer status) {
        return clubMemberRepository.findByClubIdAndStatus(clubId, status);
    }

    public PageInfo<ClubMemberDto> getClubMemberList(Long clubId, PageInfo<ClubMemberDto> pageInfo) {
        Page<ClubMember> clubMemberPage = clubMemberRepository.findClubMemberByClubIdAndStatus(clubId, Constants.CLUB_MEMBER_STATUS_NORMAL,
                new PageRequest(pageInfo.getIndex() - 1, pageInfo.getSize(), new Sort(Sort.Direction.ASC, "addTime")));

        Set<Integer> uids = clubMemberPage.getContent().stream().map(clubMember -> {return clubMember.getUid();}).collect(Collectors.toSet());
        pageInfo.setTotal(clubMemberPage.getTotalElements());
        pageInfo.setResult(clubMemberPage.getContent().stream().map(clubMember -> {return DtoUtils.convertToClubMemberDto(clubMember);}).collect(Collectors.toList()));

        if(uids != null && uids.size() > 0) {
            Map<String, Object> map = new HashedMap();
            map.put("filter[uids]", StringUtils.join(uids.toArray(), ","));
            map.put("size", PageInfo.NO_PAGE);
            ResponseEntity<PageInfo<UserDto>> responseEntity = userGateway.findUser(map);
            Map<Integer, UserDto> userDtoMap = Maps.uniqueIndex(responseEntity.getResults().getResult(), userDto -> userDto.getId());
            pageInfo.getResult().forEach(clubMemberDto -> clubMemberDto.setUserDto(userDtoMap.get(clubMemberDto.getUid())));
        }

        return pageInfo;
    }

    /**
     * 退出俱乐部/移除成员
     * @param clubId
     * @param uid
     * @param quitUid
     * @return
     */
    public ResponseEntity quitClub(Long clubId, Integer uid, Integer quitUid) {
        Club club = clubService.findClubById(clubId);
        if(club == null && club.getUid() == null) {
            return new ResponseEntity(ClubCode.CLUB_NOT_EXIST.getCode(),
                    ClubCode.CLUB_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }
        if(club.getUid().equals(quitUid)) {
            return new ResponseEntity(ClubCode.CLUB_CREATE_USER_NOT_QUIT.getCode(),
                    ClubCode.CLUB_CREATE_USER_NOT_QUIT.getMessage(SystemContextHolder.get().getLanguage()));
        }

        if(uid != quitUid && club.getUid() != uid) {
            return new ResponseEntity(ClubCode.CLUB_INSUFFICIENT_AUTHORITY.getCode(),
                    ClubCode.CLUB_INSUFFICIENT_AUTHORITY.getMessage(SystemContextHolder.get().getLanguage()));
        }

        updateClubQuitAndJoinInfo(club, quitUid, -1);
        ClubMember clubMember = clubMemberRepository.findClubMemberByClubIdAndUid(clubId, quitUid);
        if(clubMember == null) {
            return new ResponseEntity(ClubCode.CLUB_ARE_NOT_MEMBER.getCode(),
                    ClubCode.CLUB_ARE_NOT_MEMBER.getMessage(SystemContextHolder.get().getLanguage()));
        }
        clubMember.setStatus(Constants.CLUB_MEMBER_STATUS_INVALID);
        save(clubMember);
        return new ResponseEntity(clubMember);
    }

    public ClubMember findClubMemberByClubIdAndUid(Long clubId, Integer uid) {
        return clubMemberRepository.findClubMemberByClubIdAndUid(clubId, uid);
    }
}
