package com.ifitmix.club.controller;

import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.support.BaseControllerSupport;
import com.ifitmix.club.api.ClubUrl;
import com.ifitmix.club.api.dtos.ClubMemberDto;
import com.ifitmix.club.api.dtos.ClubMessageDto;
import com.ifitmix.club.api.dtos.ClubNoticeDto;
import com.ifitmix.club.api.dtos.ClubUserRunStatDto;
import com.ifitmix.club.domain.Club;
import com.ifitmix.club.domain.ClubMessage;
import com.ifitmix.club.domain.ClubNotice;
import com.ifitmix.club.domain.ClubUserRunStat;
import com.ifitmix.club.service.*;
import com.ifitmix.club.service.gateway.UserGateway;
import com.ifitmix.common.client.AliyunCenterClient;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.sports.api.dtos.UserRunDto;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhangtao on 2017/5/10.
 */
@RestController
public class ClubController extends BaseControllerSupport {

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private ClubMessageService clubMessageService;

    @Autowired
    private ClubNoticeService clubNoticeService;

    @Autowired
    private ClubRankService clubRankService;

    @Autowired
    private UserGateway userGateway;


    /**
     * 获取用户 加入的俱乐部
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_USER_CLUB_V1, method = RequestMethod.GET)
    public ResponseEntity getClubList(@PathVariable("uid") Integer uid) {
        return new ResponseEntity(clubService.findUserClubByUid(uid));
    }

    /**
     * 创建俱乐部
     *
     * @param name
     * @param desc
     * @param uid
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_V1, method = RequestMethod.POST)
    public ResponseEntity addClub(@RequestParam("name") String name,
                                  @RequestParam("desc") String desc,
                                  @RequestParam("uid") Integer uid,
                                  HttpServletRequest request) {
        Map<String, MultipartFile> multipartFileMap = AliyunCenterClient.buildMultipartFile(request);
        return clubService.addClub(multipartFileMap, uid, name, desc);
    }

    /**
     * 修改俱乐部
     *
     * @param clubId
     * @param uid
     * @param name
     * @param desc
     * @param request
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_INFO_V1, method = {RequestMethod.POST, RequestMethod.PATCH})
    public ResponseEntity modifyClub(@PathVariable("clubId") Long clubId,
                                     @RequestParam("uid") Integer uid,
                                     @RequestParam("name") String name,
                                     @RequestParam("desc") String desc,
                                     HttpServletRequest request) {
        Map<String, MultipartFile> multipartFileMap = AliyunCenterClient.buildMultipartFile(request);
        return clubService.modifyClub(multipartFileMap, clubId, uid, name, desc);
    }

    /**
     * 获取 俱乐部信息
     *
     * @param clubId
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_INFO_V1, method = RequestMethod.GET)
    public ResponseEntity getClubById(@PathVariable("clubId") Long clubId) {
        Club club = clubService.findClubById(clubId);
        club.setBackImageUrl(AliyunCenterClient.buildUrl(club.getBackImageUrl()));
        return new ResponseEntity(club);
    }

    /**
     * 删除俱乐部
     *
     * @param clubId
     * @param uid
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_INFO_V1, method = RequestMethod.DELETE)
    public ResponseEntity removeClub(@PathVariable("clubId") Long clubId,
                                     @RequestParam("uid") Integer uid) {
        return clubService.removeClub(clubId, uid);
    }

    /**
     * 获取俱乐部活跃信息
     *
     * @param clubId
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_ACTIVITY_V1, method = RequestMethod.GET)
    public ResponseEntity getActivity(@PathVariable("clubId") Long clubId) {
        return clubService.getActivity(clubId);
    }

    /**
     * 返回俱乐部分享地址 todo 暂时用原来的地址，之后前端用vue重构
     *
     * @param clubId
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_SHARE_V1, method = RequestMethod.GET)
    public ResponseEntity getClubShareUrl(@PathVariable("clubId") Long clubId) {
        Map<String, Object> map = new HashedMap();
//        map.put("shareUrl", "http://appt.igeekery.com:80/club/share-info.htm?clubId=" + clubId);
        map.put("shareUrl", "http://10.10.8.33:8180/club/share-info.htm?clubId=" + clubId);
        return createResponseEntity(map);
    }

    /**
     * 获取俱乐部动态
     *
     * @param clubId
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_DYNAMIC_V1, method = RequestMethod.GET)
    public ResponseEntity getClubDynamic(@PathVariable("clubId") Long clubId,
                                         PageInfo<UserRunDto> pageInfo) {
        return createResponseEntity(clubService.getClubDynamic(clubId, pageInfo));
    }

    /**
     * 获取俱乐部成员
     *
     * @param clubId
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_MEMBER_V1, method = RequestMethod.GET)
    public ResponseEntity getClubMembers(@PathVariable("clubId") Long clubId,
                                         PageInfo<ClubMemberDto> pageInfo) {
        return createResponseEntity(clubMemberService.getClubMemberList(clubId, pageInfo));
    }

    /**
     * 添加俱乐部成员
     *
     * @param clubId
     * @param addUid
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_MEMBER_V1, method = RequestMethod.POST)
    public ResponseEntity<ClubMemberDto> addClubMember(@PathVariable("clubId") Long clubId,
                                                       @RequestParam("addUid") Integer addUid) {
        return clubMemberService.addClubMember(clubId, addUid, null);
    }

    /**
     * 退出俱乐部／移除俱乐部成员
     *
     * @param clubId
     * @param uid
     * @param quitUid
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_MEMBER_V1, method = RequestMethod.DELETE)
    public ResponseEntity removeClubMember(@PathVariable("clubId") Long clubId,
                                           @RequestParam("uid") Integer uid,
                                           @RequestParam("quitUid") Integer quitUid) {
        return clubMemberService.quitClub(clubId, uid, quitUid);
    }

    /**
     * 获取俱乐部留言列表
     *
     * @param clubId
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_MESSAGE_V1, method = RequestMethod.GET)
    public ResponseEntity getClubMessages(@PathVariable("clubId") Long clubId,
                                          PageInfo<ClubMessageDto> pageInfo) {
        return clubMessageService.getClubMessages(clubId, pageInfo);
    }

    /**
     * 添加俱乐部留言
     *
     * @param clubId
     * @param uid
     * @param content
     * @param msgType
     * @param request
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_MESSAGE_V1, method = RequestMethod.POST)
    public ResponseEntity addClubMessage(@PathVariable("clubId") Long clubId,
                                         @RequestParam("uid") Integer uid,
                                         @RequestParam("content") String content,
                                         @RequestParam(value = "msgType", defaultValue = "1") Integer msgType,
                                         HttpServletRequest request) {
        Map<String, MultipartFile> multipartFileMap = AliyunCenterClient.buildMultipartFile(request);
        return clubMessageService.addClubMessage(clubId, uid, content, msgType, multipartFileMap);
    }

    /**
     * 获取公告列表
     *
     * @param clubId
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_NOTICE_V1, method = RequestMethod.GET)
    public ResponseEntity getClubNotices(@PathVariable("clubId") Long clubId,
                                         PageInfo<ClubNoticeDto> pageInfo) {
        return clubNoticeService.getClubNotices(clubId, pageInfo);
    }

    /**
     * 添加俱乐部公告
     *
     * @param clubId
     * @param uid
     * @param name
     * @param content
     * @param address
     * @param beginTime
     * @param endTime
     * @param desc
     * @param image
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_NOTICE_V1, method = RequestMethod.POST)
    public ResponseEntity addClubNotice(@PathVariable("clubId") Long clubId,
                                        @RequestParam("uid") Integer uid,
                                        @RequestParam("name") String name,
                                        @RequestParam("content") String content,
                                        @RequestParam("address") String address,
                                        @RequestParam("beginTime") Long beginTime,
                                        @RequestParam("endTime") Long endTime,
                                        @RequestParam("desc") String desc,
                                        @RequestParam(value = "image") MultipartFile image) {
        return clubNoticeService.addClubNotice(clubId, uid, name, content, address, beginTime, endTime, desc, image);
    }

    /**
     * 修改公告
     * @param clubId
     * @param id
     * @param uid
     * @param name
     * @param content
     * @param address
     * @param beginTime
     * @param endTime
     * @param desc
     * @param image
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_NOTICE_INFO_V1, method = {RequestMethod.POST, RequestMethod.PATCH})
    public ResponseEntity modifyClubNotice(@PathVariable("clubId") Long clubId,
                                           @PathVariable("noticeId") Long id,
                                           @RequestParam("uid") Integer uid,
                                           @RequestParam("name") String name,
                                           @RequestParam("content") String content,
                                           @RequestParam("address") String address,
                                           @RequestParam("beginTime") Long beginTime,
                                           @RequestParam("endTime") Long endTime,
                                           @RequestParam("desc") String desc,
                                           @RequestParam(value = "image", required = false) MultipartFile image) {
        return clubNoticeService.modifyClubNotice(id, clubId, uid, name, content, address, beginTime, endTime, desc, image);
    }

    /**
     * 删除 公告
     * @param clubId
     * @param id
     * @param uid
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_NOTICE_INFO_V1, method = RequestMethod.DELETE)
    public ResponseEntity removeClubNotice(@PathVariable("clubId") Long clubId,
                                           @PathVariable("noticeId") Long id,
                                           @RequestParam("uid") Integer uid) {
        return clubNoticeService.removeClubNotice(id, clubId, uid);
    }

    /**
     * 俱乐部里程统计
     *
     * @param clubId
     * @param type 统计类型 1：日，2：周，3：月
     *
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_MILE_V1, method = RequestMethod.GET)
    public ResponseEntity getClubRank(@PathVariable("clubId") Long clubId,
                                      @RequestParam("type") Integer type) {
        return clubRankService.getClubMileStat(clubId, type);
    }

    /**
     * 俱乐部用户排行
     * @param clubId
     * @param type
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = ClubUrl.CLUB_USER_RANK_V1, method = RequestMethod.GET)
    public ResponseEntity getUserRank(@PathVariable("clubId") Long clubId,
                                      @RequestParam("type") Integer type,
                                      PageInfo<ClubUserRunStatDto> pageInfo) {
        return clubRankService.getUserRank(clubId, type, pageInfo);
    }


}