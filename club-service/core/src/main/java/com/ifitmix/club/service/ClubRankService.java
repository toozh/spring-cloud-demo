package com.ifitmix.club.service;

import com.google.common.collect.Maps;
import com.ifitmix.base.Constants;
import com.ifitmix.base.api.CommonErrorCode;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.exception.AppBusinessException;
import com.ifitmix.club.api.dtos.ClubUserRunStatDto;
import com.ifitmix.club.dao.ClubMemberRepository;
import com.ifitmix.club.dao.ClubRankingRepository;
import com.ifitmix.club.dao.ClubUserRunStatRepository;
import com.ifitmix.club.domain.ClubMember;
import com.ifitmix.club.domain.ClubRanking;
import com.ifitmix.club.domain.ClubUserRunStat;
import com.ifitmix.club.service.gateway.UserGateway;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.common.mongo.BaseMongoRepositoryImpl;
import com.ifitmix.user.api.dtos.UserDto;
import com.ifitmix.utils.ConvertUtil;
import com.ifitmix.utils.DateUtil;
import com.ifitmix.utils.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/5/19.
 */
@Service
public class ClubRankService extends BaseMongoRepositoryImpl<ClubRanking, Long> {

    @Autowired
    private ClubRankingRepository clubRankingRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private ClubUserRunStatRepository clubUserRunStatRepository;

    @Autowired
    private UserGateway userGateway;

    @Override
    protected MongoRepository<ClubRanking, Long> getMongoRepository() {
        return clubRankingRepository;
    }


    @SuppressWarnings("Duplicates")
    public ResponseEntity getClubMileStat(Long clubId, Integer type) {
        Date beginDay;
        Date endDay;
        Date today = new Date();

        if(type == Constants.CLUB_RANK_TYPE_WEEK) {
            beginDay = DateUtil.getDayBegin(DateUtil.getWeekBegin(today));
            endDay = DateUtil.getDayEnd(DateUtil.getWeekEnd(today));
        } else if(type == Constants.CLUB_RANK_TYPE_MONTH) {
            beginDay = DateUtil.getDayBegin(DateUtil.getMonthBegin(today));
            endDay = DateUtil.getDayEnd(DateUtil.getMonthEnd(today));
        } else {
            throw new AppBusinessException(CommonErrorCode.INVALID_ARGUMENT);
        }

        int dayNumber = DateUtil.diffDay(endDay, beginDay, true);

        List<ClubRanking> list = clubRankingRepository.findByClubIdAndTypeAndAddTimeBetween(clubId, type, beginDay.getTime(), endDay.getTime(), new Sort(Sort.Direction.DESC, "addTime"));
        ClubRanking ranking = new ClubRanking();
        if(list == null || list.size() == 0) {
            ranking.setStep(0L);
            ranking.setCalorie(0L);
            ranking.setDistance(0L);
        } else {
            ranking.setStep(list.get(0).getStep());
            ranking.setCalorie(list.get(0).getCalorie());
            ranking.setDistance(list.get(0).getDistance());
        }

        //俱乐部排行榜 （统计图数据）
        List<ClubRanking> clubRankingList = clubRankingRepository.findByClubIdAndTypeAndAddTimeBetween(clubId, Constants.CLUB_RANK_TYPE_DAY, beginDay.getTime(), endDay.getTime(), new Sort(Sort.Direction.DESC, "addTime"));
        List<Long> rankStatViewList = new ArrayList<>(dayNumber);

        Map<String, ClubRanking> timeMap = Maps.uniqueIndex(clubRankingList, clubRanking -> clubRanking.getAddTimeStr());

        for(int i = 0; i < dayNumber; i++) {
            String strTime = DateUtil.format(DateUtil.addDate(beginDay, Calendar.DAY_OF_MONTH, i), "yyyy-MM-dd");
            ClubRanking clubRanking = timeMap.get(strTime);
            if(clubRanking == null) {
                rankStatViewList.add(0L);
            } else {
                rankStatViewList.add(clubRanking.getDistance());
            }
        }

        Map<String,Object> resultMap = new HashedMap();
        resultMap.put("statView", rankStatViewList);
        resultMap.put("totalCount", ranking);

        return new ResponseEntity(resultMap);

    }
    @SuppressWarnings("Duplicates")
    public ResponseEntity getUserRank(Long clubId, Integer type, PageInfo<ClubUserRunStatDto> pageInfo) {
        Date beginDay;
        Date endDay;
        Date today = new Date();

        if(type == Constants.CLUB_RANK_TYPE_WEEK) {
            beginDay = DateUtil.getDayBegin(DateUtil.getWeekBegin(today));
            endDay = DateUtil.getDayEnd(DateUtil.getWeekEnd(today));
        } else if(type == Constants.CLUB_RANK_TYPE_MONTH) {
            beginDay = DateUtil.getDayBegin(DateUtil.getMonthBegin(today));
            endDay = DateUtil.getDayEnd(DateUtil.getMonthEnd(today));
        } else {
            throw new AppBusinessException(CommonErrorCode.INVALID_ARGUMENT);
        }

        List<ClubMember> list = clubMemberRepository.findByClubIdAndStatus(clubId, Constants.CLUB_MEMBER_STATUS_NORMAL);
        Set<Integer> uidSet = list.stream().map(clubMember -> {return clubMember.getUid();}).collect(Collectors.toSet());

        Page<ClubUserRunStat> page = clubUserRunStatRepository.findByUidInAndTypeAndAddTimeBetween(uidSet, type, beginDay.getTime(), endDay.getTime(), new PageRequest(pageInfo.getPageNo() - 1, pageInfo.getSize(), new Sort(Sort.Direction.DESC, "distance")));


        if(page.getContent() != null && page.getContent().size() > 0) {
            Set<Integer> uids = page.getContent().stream().map(clubUserRunStat -> {return clubUserRunStat.getUid();}).collect(Collectors.toSet());
            Map<String, Object> map = new HashedMap();
            map.put("filter[uids]", StringUtils.join(uids.toArray(), ","));
            map.put("size", PageInfo.NO_PAGE);
            ResponseEntity<PageInfo<UserDto>> responseEntity = userGateway.findUser(map);
            Map<Integer, UserDto> userDtoMap = Maps.uniqueIndex(responseEntity.getResults().getResult(), userDto -> userDto.getId());
            pageInfo.setResult(page.getContent().stream().map(clubUserRunStat -> {
                ClubUserRunStatDto clubUserRunStatDto = (ClubUserRunStatDto) ConvertUtil.convertBeanTo(clubUserRunStat, ClubUserRunStatDto.class);
                clubUserRunStatDto.setUserDto(userDtoMap.get(clubUserRunStatDto.getUid()));
                return clubUserRunStatDto;
            }).collect(Collectors.toList()));
        }
        pageInfo.setTotal(page.getTotalElements());

        return new ResponseEntity(pageInfo);
    }
}
