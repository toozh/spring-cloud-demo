package com.ifitmix.user.service.gateway;

import com.ifitmix.sports.api.SportsUrl;
import com.ifitmix.sports.api.dtos.UserRunDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhangtao on 2017/3/13.
 */
@FeignClient(value = SportsUrl.SERVICE_HOSTNAME, fallback = SportsHystrix.class)
public interface SportsClient {

    @RequestMapping(value = SportsUrl.RUN_INFO_V1, method = RequestMethod.GET)
    UserRunDto getUserRunInfo(@PathVariable("version") String version,
                              @PathVariable("userId") Integer userId,
                              @PathVariable("startTime") String startTime);

}
