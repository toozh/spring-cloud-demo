package com.ifitmix.sports.controller;

import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.support.BaseControllerSupport;
import com.ifitmix.common.client.AliyunCenterClient;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.sports.domain.UserRun;
import com.ifitmix.sports.service.UserRunService;
import com.ifitmix.sports.api.SportsUrl;
import com.ifitmix.sports.api.dtos.UserRunDto;
import com.ifitmix.sports.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhangtao on 2017/3/9.
 */
@RefreshScope
@RestController
public class UserRunController extends BaseControllerSupport {

    @Autowired
    private UserRunService userRunService;

    /**
     * 获取用户 单条跑步 记录
     * @param uid
     * @param startTime
     * @return
     */
    @RequestMapping(value = SportsUrl.RUN_INFO_V1, method = RequestMethod.GET)
    public ResponseEntity getUserRunInfo(@PathVariable("uid") Integer uid,
                                         @PathVariable("startTime") Long startTime) {
        ResponseEntity responseEntity = new ResponseEntity(DtoUtils.convertToUserRunDto(userRunService.findUserRunByStartTimeAndUid(startTime, uid)));
        return responseEntity;
    }

    /**
     * 获取用户 跑步 运动记录（分页）
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = SportsUrl.RUN_V1, method = RequestMethod.GET)
    public ResponseEntity getUserRunList(PageInfo<UserRunDto> pageInfo) {
        return createResponseEntity(userRunService.findUserRunByPage(pageInfo));
    }

    /**
     * 上传用户 运动 记录
     * @param userRunDto
     * @return
     */
    @RequestMapping(value = SportsUrl.RUN_V1, method = RequestMethod.POST)
    public ResponseEntity addUserRun(UserRunDto userRunDto,
                                     HttpServletRequest request) {
        Map<String, MultipartFile> fileMap = AliyunCenterClient.buildMultipartFile(request);
        // todo 重新设计 RUI
        Integer uid = 1;
        userRunDto.setUid(uid);
        UserRun userRun = userRunService.addUserRun(userRunDto, fileMap);
        return createResponseEntity(DtoUtils.convertToUserRunDto(userRun));
    }



}
