package com.ifitmix.club.service.gateway;

import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.user.api.UserUrl;
import com.ifitmix.user.api.dtos.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by zhangtao on 2017/5/11.
 */
@FeignClient(value = UserUrl.SERVICE_NAME, fallback = UserHystrix.class)
public interface UserClient {

    @RequestMapping(value = UserUrl.USER_V1, method = RequestMethod.GET)
    String findUser(@RequestParam Map<String, Object> map);

    @RequestMapping(value = UserUrl.USER_INFO_V1, method = RequestMethod.GET)
    ResponseEntity<UserDto> findUserById(@PathVariable("userId") Integer userId);
}
