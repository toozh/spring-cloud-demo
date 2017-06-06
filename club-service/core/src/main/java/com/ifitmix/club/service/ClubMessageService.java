package com.ifitmix.club.service;

import com.google.common.collect.Maps;
import com.ifitmix.base.Constants;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.event.domain.PushBody;
import com.ifitmix.club.api.constants.ClubCode;
import com.ifitmix.club.api.dtos.ClubMessageDto;
import com.ifitmix.club.api.event.PushClubMessage;
import com.ifitmix.club.dao.ClubMessageRepository;
import com.ifitmix.club.domain.Club;
import com.ifitmix.club.domain.ClubMember;
import com.ifitmix.club.domain.ClubMessage;
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

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zhangtao on 2017/5/18.
 */
@Service
public class ClubMessageService extends BaseMongoRepositoryImpl<ClubMessage, Long> {

    @Autowired
    private ClubMessageRepository clubMessageRepository;

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private UserGateway userGateway;

    @SuppressWarnings("Duplicates")
    public ResponseEntity getClubMessages(Long clubId, PageInfo<ClubMessageDto> pageInfo) {
        Page<ClubMessage> page = clubMessageRepository.findClubMessageByClubId(clubId, new PageRequest(pageInfo.getPageNo() - 1, pageInfo.getSize(), new Sort(Sort.Direction.DESC, "_id")));
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setResult(page.getContent().stream().map(clubMessage -> {return DtoUtils.convertToClubMessageDto(clubMessage);}).collect(Collectors.toList()));

        if(pageInfo.getResult() != null && pageInfo.getResult().size() > 0) {
            Set<Integer> uids = pageInfo.getResult().stream().map(clubMessageDto -> {return clubMessageDto.getUid();}).collect(Collectors.toSet());

            Map<String, Object> map = new HashedMap();
            map.put("filter[uids]", StringUtils.join(uids.toArray(), ","));
            map.put("size", PageInfo.NO_PAGE);
            ResponseEntity<PageInfo<UserDto>> responseEntity = userGateway.findUser(map);
            Map<Integer, UserDto> userDtoMap = Maps.uniqueIndex(responseEntity.getResults().getResult(), userDto -> userDto.getId());
            pageInfo.getResult().forEach(clubMessageDto -> clubMessageDto.setUserDto(userDtoMap.get(clubMessageDto.getUid())));
        }

        return new ResponseEntity(pageInfo);
    }

    public ResponseEntity addClubMessage(Long clubId, Integer uid, String content, Integer msgType, Map<String, MultipartFile> multipartFileMap) {
        Club club = clubService.findClubById(clubId);
        if(club == null || club.getStatus().equals(Constants.CLUB_STATUS_INVALID)) {
            return new ResponseEntity(ClubCode.CLUB_NOT_EXIST.getCode(),
                    ClubCode.CLUB_NOT_EXIST.getMessage(SystemContextHolder.get().getLanguage()));
        }

        ClubMember clubMember = clubMemberService.findClubMemberByClubIdAndUid(clubId, uid);
        if(clubMember == null || clubMember.getStatus().equals(Constants.CLUB_MEMBER_STATUS_INVALID)) {
            return new ResponseEntity(ClubCode.CLUB_ARE_NOT_MEMBER.getCode(),
                    ClubCode.CLUB_ARE_NOT_MEMBER.getMessage(SystemContextHolder.get().getLanguage()));
        }

        ClubMessage clubMessage = new ClubMessage();
        clubMessage.setUid(uid);
        clubMessage.setClubId(clubId);
        clubMessage.setContent(content);
        clubMessage.setMsgType(msgType);

        String fileLink = null;
        MultipartFile multipartFile = multipartFileMap.get("contentFile");
        if(multipartFile != null) {
            switch (msgType) {
                case Constants.CLUB_MESSAGE_TYPE_VOICE:
                    fileLink = AliyunCenterClient.putFile(FileConstants.FILE_TYPE_CLUB_MSG_VOICE, multipartFile);
                    break;
                case Constants.CLUB_MESSAGE_TYPE_IMG:
                    fileLink = AliyunCenterClient.putFile(FileConstants.FILE_TYPE_CLUB_MSG_IMAGE, multipartFile);
                    break;
            }
            clubMessage.setFileLink(fileLink);
        }
        clubMessage.setAddTime(System.currentTimeMillis());
        save(clubMessage);
        ClubMessageDto clubMessageDto = DtoUtils.convertToClubMessageDto(clubMessage);

        // 创建俱乐部留言消息推送
        eventBus.publish(createPushClubMessage(club , uid, clubMessage));

        return new ResponseEntity(clubMessageDto);
    }


    public PushClubMessage createPushClubMessage(Club club, Integer uid, ClubMessage clubMessage) {
        String alert = MessageFormat.format(PushBody.PUSH_TITLE_ALERT.get(PushBody.PUSH_TYPE_CLUB_11), club.getName());
        PushBody pushBody = new PushBody();
        pushBody.setType(PushBody.TYPE_NOTIFICATIONS);
        pushBody.setResponseType(PushBody.RESPONSE_TYPE_OPEN_ACTION);
        pushBody.setPushType(PushBody.PUSH_TYPE_CLUB);
        pushBody.setTitle(alert);
        pushBody.setAlert(alert);
        pushBody.setBornTimestamp(clubMessage.getAddTime());
        pushBody.setContent(clubMessage.getContent());
        pushBody.setFileLink(clubMessage.getFileLink());
        pushBody.setContentType(clubMessage.getMsgType());

        club.getMemberUidSet().remove(uid);

        return new PushClubMessage(uid, club.getMemberUidSet(), clubMessage.getId(), pushBody);

    }

    @Override
    protected MongoRepository<ClubMessage, Long> getMongoRepository() {
        return clubMessageRepository;
    }
}
