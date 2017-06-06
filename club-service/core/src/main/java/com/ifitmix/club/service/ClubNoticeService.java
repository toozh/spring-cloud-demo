package com.ifitmix.club.service;

import com.google.common.collect.Maps;
import com.ifitmix.base.Constants;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.exception.AppBusinessException;
import com.ifitmix.club.api.constants.ClubCode;
import com.ifitmix.club.api.dtos.ClubNoticeDto;
import com.ifitmix.club.api.event.PushClubNotice;
import com.ifitmix.club.dao.ClubNoticeRepository;
import com.ifitmix.club.domain.Club;
import com.ifitmix.club.domain.ClubNotice;
import com.ifitmix.club.service.gateway.UserGateway;
import com.ifitmix.club.utils.DtoUtils;
import com.ifitmix.common.client.AliyunCenterClient;
import com.ifitmix.common.constants.FileConstants;
import com.ifitmix.common.context.SystemContextHolder;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.common.event.service.EventBus;
import com.ifitmix.common.mongo.BaseMongoRepositoryImpl;
import com.ifitmix.user.api.dtos.UserDto;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/5/19.
 */
@Service
public class ClubNoticeService extends BaseMongoRepositoryImpl<ClubNotice, Long> {

    @Autowired
    private ClubNoticeRepository clubNoticeRepository;

    @Autowired
    private ClubService clubService;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private UserGateway userGateway;

    @SuppressWarnings("Duplicates")
    public ResponseEntity getClubNotices(Long clubId, PageInfo<ClubNoticeDto> pageInfo) {
        Page<ClubNotice> page = clubNoticeRepository.findClubNoticeByClubIdAndStatus(clubId, Constants.CLUB_NOTICE_STATUS_NORMAL,new PageRequest(pageInfo.getPageNo() - 1, pageInfo.getSize(), new Sort(Sort.Direction.DESC, "beginTime")));
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setResult(page.getContent().stream().map(clubNotice -> {return DtoUtils.convertToClubNoticeDto(clubNotice);}).collect(Collectors.toList()));

        if(pageInfo.getResult() != null && pageInfo.getResult().size() > 0) {
            Set<Integer> uids = pageInfo.getResult().stream().map(clubNoticeDto -> {return clubNoticeDto.getUid();}).collect(Collectors.toSet());
            Map<String, Object> map = new HashedMap();
            map.put("filter[uids]", StringUtils.join(uids.toArray(), ","));
            map.put("size", PageInfo.NO_PAGE);
            ResponseEntity<PageInfo<UserDto>> responseEntity = userGateway.findUser(map);
            Map<Integer, UserDto> userDtoMap = Maps.uniqueIndex(responseEntity.getResults().getResult(), userDto -> userDto.getId());
            pageInfo.getResult().forEach(clubNoticeDto -> clubNoticeDto.setUserDto(userDtoMap.get(clubNoticeDto.getUid())));
        }
        return new ResponseEntity(pageInfo);
    }

    public ResponseEntity addClubNotice(Long clubId, Integer uid, String name, String content, String address, Long beginTime, Long endTime, String desc, MultipartFile image) {
        Club club = clubService.findClubById(clubId);
        if(club == null || club.getStatus().equals(Constants.CLUB_STATUS_INVALID)) {
            return new ResponseEntity(ClubCode.CLUB_NOT_EXIST.getCode(),
                    ClubCode.CLUB_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }

        ClubNotice clubNotice = new ClubNotice();
        clubNotice.setClubId(clubId);
        clubNotice.setUid(uid);
        clubNotice.setName(name);
        clubNotice.setContent(content);
        clubNotice.setAddress(address);
        clubNotice.setBeginTime(beginTime);
        clubNotice.setEndTime(endTime);
        clubNotice.setDesc(desc);
        clubNotice.setStatus(Constants.CLUB_NOTICE_STATUS_NORMAL);
        clubNotice.setModifyCount(0);
        String backImageUrl = AliyunCenterClient.putFile(FileConstants.FILE_TYPE_CLUB_NOTICE_IMAGE, image);
        clubNotice.setBackImageUrl(backImageUrl);
        clubNotice.setAddTime(System.currentTimeMillis());

        save(clubNotice);

        // 推送公告信息
        eventBus.publish(new PushClubNotice(clubId, uid, clubNotice.getId()));

        return new ResponseEntity(DtoUtils.convertToClubNoticeDto(clubNotice));
    }

    @Override
    protected MongoRepository<ClubNotice, Long> getMongoRepository() {
        return clubNoticeRepository;
    }

    public ResponseEntity modifyClubNotice(Long id, Long clubId, Integer uid, String name, String content, String address, Long beginTime, Long endTime, String desc, MultipartFile image) {
        ClubNotice clubNotice = clubNoticeRepository.findClubNoticeByIdAndUidAndClubIdAndStatus(id, uid, clubId, Constants.CLUB_NOTICE_STATUS_NORMAL);
        if(clubNotice == null) {
            return new ResponseEntity(ClubCode.CLUB_NOTICE_NOT_EXIST.getCode(),
                    ClubCode.CLUB_NOTICE_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }
        clubNotice.setName(name);
        clubNotice.setContent(content);
        clubNotice.setAddress(address);
        clubNotice.setBeginTime(beginTime);
        clubNotice.setEndTime(endTime);
        clubNotice.setDesc(desc);

        if(image != null) {
            String imageUrl = AliyunCenterClient.putFile(FileConstants.FILE_TYPE_CLUB_NOTICE_IMAGE, image);
            AliyunCenterClient.deleteFile(clubNotice.getBackImageUrl());
            clubNotice.setBackImageUrl(imageUrl);
        }
        save(clubNotice);

        return new ResponseEntity(clubNotice);

    }

    public ResponseEntity removeClubNotice(Long id, Long clubId, Integer uid) {
        ClubNotice clubNotice = clubNoticeRepository.findClubNoticeByIdAndUidAndClubIdAndStatus(id, uid, clubId, Constants.CLUB_NOTICE_STATUS_NORMAL);
        if(clubNotice == null) {
            return new ResponseEntity(ClubCode.CLUB_NOTICE_NOT_EXIST.getCode(),
                    ClubCode.CLUB_NOTICE_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }
        clubNotice.setStatus(Constants.CLUB_NOTICE_STATUS_INVALID);
        save(clubNotice);
        return new ResponseEntity(clubNotice);
    }
}
