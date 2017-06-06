package com.ifitmix.club.service.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhangtao on 2017/5/16.
 */
@Component
public class SportsHystrix implements SportsClient {

    private static Logger logger = LoggerFactory.getLogger(SportsHystrix.class);


    @Override
    public String getUserRunList(@RequestParam("filter[uids]") String uids,
                                 @RequestParam("filter[beginTime]") Long beginTime,
                                 @RequestParam("filter[endTime]") Long endTime,
                                 @RequestParam("size") Integer size) {
        logger.info(String.format("UserRunClient调用getUserRunList错误  params : {uids: %s, beginTime: %d, endTime: %d," +
                "size: %d}", uids, beginTime, endTime, size));
        return null;
    }

    @Override
    public String getClubDynamic(@RequestParam("filter[uids]") String uids, @RequestParam("filter[types]") String types, @RequestParam("filter[beginTime]") Long beginTime, @RequestParam("filter[endTime]") Long endTime, @RequestParam("size") Integer size, @RequestParam("pageNo") Integer pageNo) {
        return null;
    }

    @Override
    public String getUserRunInfo(@PathVariable("uid") Integer uid, @PathVariable("startTime") Long startTime) {
        return null;
    }
}
