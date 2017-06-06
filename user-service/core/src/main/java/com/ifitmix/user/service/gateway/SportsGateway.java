package com.ifitmix.user.service.gateway;

import com.ifitmix.sports.api.dtos.UserRunDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangtao on 2017/3/13.
 */
@Service
public class SportsGateway {

    @Autowired
    private SportsClient sportsClient;

    @HystrixCommand
    public UserRunDto getUserRunInfo(String version, Integer userId, String startTime) {
        return sportsClient.getUserRunInfo(version, userId, startTime);
    }


}
