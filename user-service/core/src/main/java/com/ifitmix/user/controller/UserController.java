package com.ifitmix.user.controller;

import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.support.BaseControllerSupport;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.user.api.UserUrl;
import com.ifitmix.user.api.dtos.UserDto;
import com.ifitmix.user.domain.User;
import com.ifitmix.user.service.UserService;
import com.ifitmix.user.service.gateway.SportsGateway;
import com.ifitmix.user.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhangtao on 2017/3/13.
 */
@RefreshScope
@RestController
public class UserController extends BaseControllerSupport{

    @Autowired
    private UserService userService;
    @Autowired
    private SportsGateway sportsGateway;

    @RequestMapping(value = UserUrl.USER_V1, method = RequestMethod.GET)
    public ResponseEntity findUser(PageInfo<UserDto> page) {
        return createResponseEntity(userService.findUserByPage(page));
    }

    @RequestMapping(value = UserUrl.USER_INFO_V1, method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Integer userId) {

        User user = userService.getUserById(userId);
        UserDto userDto = DtoUtils.convertToUserDto(user);
        return new ResponseEntity(userDto);
    }

}
