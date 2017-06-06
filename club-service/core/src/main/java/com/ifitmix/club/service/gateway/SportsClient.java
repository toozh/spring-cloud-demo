package com.ifitmix.club.service.gateway;

import com.ifitmix.sports.api.SportsUrl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhangtao on 2017/5/16.
 */
@FeignClient(value = SportsUrl.SERVICE_NAME, fallback = SportsHystrix.class)
public interface SportsClient {

    /**
     * 获取 用户跑步列表  todo 当传map时feign回无视，之后在研究源码解决 （升级版本后已经能解决了）
     * @param uids
     * @param beginTime
     * @param endTime
     * @param size
     * @return
     */
    @RequestMapping(value = SportsUrl.RUN_V1, method = RequestMethod.GET)
    public String getUserRunList(@RequestParam("filter[uids]") String uids,
                                 @RequestParam("filter[beginTime]") Long beginTime,
                                 @RequestParam("filter[endTime]") Long endTime,
                                 @RequestParam("size") Integer size);

    @RequestMapping(value = SportsUrl.RUN_V1, method = RequestMethod.GET)
    String getClubDynamic(@RequestParam("filter[uids]") String uids,
                          @RequestParam("filter[types]") String types,
                          @RequestParam("filter[beginTime]") Long beginTime,
                          @RequestParam("filter[endTime]") Long endTime,
                          @RequestParam("size") Integer size,
                          @RequestParam("pageNo") Integer pageNo);

    @RequestMapping(value = SportsUrl.RUN_INFO_V1, method = RequestMethod.GET)
    public String getUserRunInfo(@PathVariable("uid") Integer uid,
                                 @PathVariable("startTime") Long startTime);


}
