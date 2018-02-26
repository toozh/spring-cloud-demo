package com.ifitmix.club.service;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.ifitmix.base.Constants;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.club.api.constants.ClubCode;
import com.ifitmix.club.dao.ClubMemberRepository;
import com.ifitmix.club.dao.ClubRepository;
import com.ifitmix.club.domain.Club;
import com.ifitmix.club.domain.ClubMember;
import com.ifitmix.club.domain.ClubUserRunStat;
import com.ifitmix.club.service.gateway.SportsGateway;
import com.ifitmix.club.service.gateway.UserGateway;
import com.ifitmix.common.client.AliyunCenterClient;
import com.ifitmix.common.constants.FileConstants;
import com.ifitmix.common.context.SystemContextHolder;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.common.mongo.BaseMongoRepositoryImpl;
import com.ifitmix.common.spring.ApplicationContextHolder;
import com.ifitmix.sports.api.dtos.UserRunDto;
import com.ifitmix.user.api.constants.UserCode;
import com.ifitmix.user.api.dtos.UserDto;
import com.ifitmix.utils.DateUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/5/10.
 */
@Service
public class ClubService extends BaseMongoRepositoryImpl<Club, Long> {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private UserGateway userGateway;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private SportsGateway sportsGateway;

    @Autowired
    private ClubUserRunStatService clubUserRunStatService;

    @Override
    protected MongoRepository<Club, Long> getMongoRepository() {
        return clubRepository;
    }

    /**
     * 根据ID 获取俱乐部信息
     * @param id
     * @return
     */
    public Club findClubById(Long id) {
        return clubRepository.findOne(id);
    }

    public List<Club> findClubByUid(Integer uid) {
        return clubRepository.findClubByUid(uid);
    }

    /**
     * 添加俱乐部
     * @param multipartFileMap
     * @param uid
     * @param name
     * @param desc
     */
    public ResponseEntity addClub(Map<String, MultipartFile> multipartFileMap, Integer uid, String name, String desc) {

        if(clubRepository.findClubByUidAndName(uid, name) != null) {
            return new ResponseEntity(ClubCode.CLUB_NAME_REPEAT.getCode(), ClubCode.CLUB_NAME_REPEAT.getMessage(SystemContextHolder.get().getLanguage()));
        }
        ResponseEntity<UserDto> responseEntity = userGateway.findUserById(uid);
        if(responseEntity == null || responseEntity.getResults().getId() == null) {
            return new ResponseEntity(UserCode.LOGIN_USER_NOT_EXIST.getCode(), UserCode.LOGIN_USER_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }


        String backImageUrl = "";
        if(multipartFileMap.get("image") != null) {
            backImageUrl = AliyunCenterClient.putFile(FileConstants.FILE_TYPE_USER_CLUB_BACK_IMAGE.toString(), multipartFileMap.get("image"));
        }

        Club club = new Club();
        club.setUid(uid);
        club.setName(name);
        club.setDesc(desc);
        club.setBackImageUrl(backImageUrl);
        club.setMaxMember(Integer.parseInt(ApplicationContextHolder.constant.APP_CLUB_MAX_MEMBERS));
        club.setMemberCount(0);
        club.setMemberUidSet(new HashSet<>(0));
        club.setStatus(Club.STATUS_NORMAL);
        club.setAddTime(System.currentTimeMillis());
        clubRepository.save(club);

        //添加俱乐部成员
        clubMemberService.addClubMember(club.getId(), uid, ClubMember.TYPE_CREATOR);

        return new ResponseEntity(club);

    }

    /**
     * 修改俱乐部
     * @param multipartFileMap
     * @param clubId
     * @param name
     * @param desc
     * @return
     */
    public ResponseEntity modifyClub(Map<String, MultipartFile> multipartFileMap, Long clubId, Integer uid, String name, String desc) {
        Club club = clubRepository.findOne(clubId);
        if(club == null) {
            return new ResponseEntity(ClubCode.CLUB_NOT_EXIST.getCode(),
                    ClubCode.CLUB_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }
        if(!club.getName().equals(name) && clubRepository.findClubByUidAndName(club.getUid(), name) != null) {
            return new ResponseEntity(ClubCode.CLUB_NAME_REPEAT.getCode(),
                    ClubCode.CLUB_NAME_REPEAT.getMessage(SystemContextHolder.get().getLanguage()));
        }
        if(multipartFileMap.get("image") != null) {
            String backImageUrl = AliyunCenterClient.putFile(FileConstants.FILE_TYPE_USER_CLUB_BACK_IMAGE.toString(), multipartFileMap.get("image"));
            club.setBackImageUrl(backImageUrl);
        }
        club.setName(name);
        club.setDesc(desc);
        clubRepository.save(club);
        club.setBackImageUrl(AliyunCenterClient.buildUrl(club.getBackImageUrl()));
        return new ResponseEntity(club);
    }

    /**
     * 删除俱乐部
     * <p>
     *     1. 将俱乐部状态置成无效
     *     2. 将成员状态置成无效
     * </p>
     * @param clubId
     * @param uid
     * @return
     */
    public ResponseEntity removeClub(Long clubId, Integer uid) {
        Club club = clubRepository.findClubByIdAndUid(clubId, uid);
        if(club == null) {
            return new ResponseEntity(ClubCode.CLUB_NOT_EXIST.getCode(),
                    ClubCode.CLUB_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }
        club.setStatus(Club.STATUS_INVALID);
        clubRepository.save(club);

        Query query = new Query(Criteria.where("clubId").is(clubId));
        Update update = new Update().set("status", ClubMember.STATUS_INVALID);
        mongoOperations.updateMulti(query, update, ClubMember.class);

        return new ResponseEntity(club);

    }

    /**
     * 获取用户俱乐部列表
     * @param uid
     * @return
     */
    public List<Club> findUserClubByUid(Integer uid) {

        // 公开置顶俱乐部
        List<Long> clubIds = Arrays.stream(ApplicationContextHolder.constant.APP_CLUB_TOP_CLUB.split(",")).map(id -> { return Long.parseLong(id); }).collect(Collectors.toList());
        List<Club> publicClubList = clubRepository.findClubByIdIn(clubIds);

        publicClubList.stream().forEach(club -> {
            club.setBackImageUrl(AliyunCenterClient.buildUrl(club.getBackImageUrl()));
            club.setType(Club.TYPE_PUBLIC);
            club.setHaveMember(club.getMemberUidSet().contains(uid) ? Club.SAVE_MEMBER_YES : Club.SAVE_MEMBER_NO);
        });

        //个人俱乐部
        List<ClubMember> clubMemberList = clubMemberRepository.findClubMemberByUidAndStatusOrderByTypeAscAddTimeDesc(uid, Club.STATUS_NORMAL);
        Set<Long> clubIdSet = clubMemberList.stream().map(clubMember -> {return clubMember.getClubId();}).collect(Collectors.toSet());
        clubIdSet.removeAll(clubIds);

        List<Club> clubList = clubRepository.findClubByIdIn(clubIdSet);
        clubList.stream().forEach(club -> {
            club.setBackImageUrl(AliyunCenterClient.buildUrl(club.getBackImageUrl()));
            club.setType(Club.TYPE_USER);
        });

        if(publicClubList == null) {
            publicClubList = new ArrayList<>();
            publicClubList.addAll(clubList);
        } else {
            publicClubList.addAll(clubList);
        }

        return publicClubList;
    }

    /**
     * 获取俱乐部活跃信息
     * <p>
     *     1.memberCount 成员数
     *     2.activeCount 活跃人数
     *     3.activeUser 活跃用户（今日排行）
     * </p>
     * @param clubId
     * @return
     */
    public ResponseEntity getActivity(Long clubId) {
        Long memberCount = clubMemberRepository.countByClubIdAndStatus(clubId, Constants.CLUB_MEMBER_STATUS_NORMAL);
        List<ClubMember> clubMemberList = clubMemberRepository.findClubMemberByClubIdAndStatus(clubId, Constants.CLUB_MEMBER_STATUS_NORMAL);
        Set<Integer> uids = clubMemberList.stream().map(clubMember -> { return clubMember.getUid();}).collect(Collectors.toSet());

        ResponseEntity<PageInfo<UserRunDto>> responseEntity = sportsGateway.getUserRunList(StringUtils.join(uids.toArray(), ","),
                DateUtil.getDayBegin(new Date()).getTime(), DateUtil.getDayEnd(new Date()).getTime(), PageInfo.NO_PAGE);
        Integer activeCount = 0;
        if(responseEntity != null) {
            Set<Integer> activityUids = responseEntity.getResults().getResult().stream().map(userRunDto -> {return userRunDto.getUid();}).collect(Collectors.toSet());
            activeCount = activityUids.size();
        }

        Club club = clubRepository.findOne(clubId);
        Pageable pageable = new PageRequest(1, 10);
        org.springframework.data.domain.Page<ClubUserRunStat> page = clubUserRunStatService.findByUidInAndTypeAndAddTimeBetween(club.getMemberUidSet(), ClubUserRunStat.TYPE_DAY,
                DateUtil.getDayBegin(new Date()).getTime(), DateUtil.getDayEnd(new Date()).getTime(), pageable);
        List<UserDto> activeUser = new ArrayList<>(10);
        // 如果 今日没有活跃用户，则任意取 10 人
        if(page.getContent().size() < 1) {
            Set<Integer> activeUids = new HashSet<>(10);
            List<Integer> uidList = new ArrayList<>(club.getMemberUidSet());
            for (int i = 0; i < uidList.size(); i++)  {
                if(i > 10) {
                    break;
                }
                activeUids.add(uidList.get(i));
            }

            Map<String, Object> map = new HashedMap();
            map.put("filter[uids]", StringUtils.join(activeUids.toArray(), ","));
            map.put("size", PageInfo.NO_PAGE);
            ResponseEntity<PageInfo<UserDto>> result = userGateway.findUser(map);
            result.getResults().getResult().forEach(userDto -> activeUser.add(userDto));
        } else {
            Set<Integer> activeUids = page.getContent().stream().map(clubUserRunStat -> {return clubUserRunStat.getUid();}).collect(Collectors.toSet());
            Map<String, Object> map = new HashedMap();
            map.put("filter[uids]", StringUtils.join(activeUids.toArray(), ","));
            map.put("size", PageInfo.NO_PAGE);
            ResponseEntity<PageInfo<UserDto>> result = userGateway.findUser(map);
            result.getResults().getResult().forEach(userDto -> activeUser.add(userDto));
        }


        Map<String, Object> resultMap = new HashedMap();
        resultMap.put("memberCount", memberCount);
        resultMap.put("activeCount", activeCount);
        resultMap.put("activeUser", activeUser);
        return new ResponseEntity(resultMap);
    }

    public PageInfo<UserRunDto> getClubDynamic(Long clubId, PageInfo<UserRunDto> pageInfo) {
        // 获取成员编号
        List<ClubMember> clubMemberList = clubMemberService.findByClubIdAndStatus(clubId, ClubMember.STATUS_NORMAL);
        Set<Integer> uids = clubMemberList.stream().map(clubMember -> {return clubMember.getUid();}).collect(Collectors.toSet());

        ArrayList<Integer> types = new ArrayList<>();
        types.add(Constants.RUN_TYPE_UNKNOWN);
        types.add(Constants.RUN_TYPE_RUN);
        ResponseEntity<PageInfo<UserRunDto>> responseEntity = sportsGateway.getClubDynamic(StringUtils.join(uids.toArray(), ","),
                StringUtils.join(types.toArray(), ","), DateUtil.getDayBegin(new Date()).getTime(),
                DateUtil.getDayEnd(new Date()).getTime(), pageInfo.getSize(), pageInfo.getPageNo());

        Set<Integer> currentUids = responseEntity.getResults().getResult().stream().map(userRunDto -> {return userRunDto.getUid();}).collect(Collectors.toSet());
        if(currentUids != null && currentUids.size() > 0) {
            Map<String, Object> map = new HashedMap();
            map.put("filter[uids]", StringUtils.join(currentUids.toArray(), ","));
            map.put("size", PageInfo.NO_PAGE);
            ResponseEntity<PageInfo<UserDto>> result = userGateway.findUser(map);
            Map<Integer, UserDto> userDtoMap = Maps.uniqueIndex(result.getResults().getResult(), userDto -> userDto.getId());
            responseEntity.getResults().getResult().forEach(userRunDto -> {
                userRunDto.setUserDto(userDtoMap.get(userRunDto.getUid()));
            });
        }
        pageInfo.setTotal(responseEntity.getResults().getTotal());
        pageInfo.setResult(responseEntity.getResults().getResult());

        return pageInfo;
    }
}
