package com.ifitmix.user.service.gateway;

import com.ifitmix.sports.api.dtos.UserRunDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by zhangtao on 2017/3/14.
 */
@Component
public class SportsHystrix implements SportsClient {

    @Override
    public UserRunDto getUserRunInfo(@PathVariable("version") String version, @PathVariable("userId") Integer userId, @PathVariable("startTime") String startTime) {
        throw new RuntimeException("自定义错误");
    }
}
