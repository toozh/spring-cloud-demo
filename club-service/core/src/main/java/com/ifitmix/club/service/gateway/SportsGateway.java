package com.ifitmix.club.service.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.sports.api.dtos.UserRunDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangtao on 2017/5/16.
 */
@Service
public class SportsGateway {

    @Autowired
    private SportsClient userRunClient;

    /**
     * 获取用户跑步列表
     * @param uids
     * @param beginTime
     * @param endTime
     * @param size
     * @return
     */
    public ResponseEntity<PageInfo<UserRunDto>> getUserRunList(String uids, Long beginTime,
                                                               Long endTime, Integer size) {
        String results = userRunClient.getUserRunList(uids, beginTime, endTime, size);
        ResponseEntity<PageInfo<UserRunDto>> responseEntity = JSON.parseObject(results, new TypeReference<ResponseEntity<PageInfo<UserRunDto>>>() {});
        return responseEntity;
    }

    public ResponseEntity<UserRunDto> getUserRunInfo(Integer uid, Long startTime) {
        String results = userRunClient.getUserRunInfo(uid, startTime);
        ResponseEntity<UserRunDto> responseEntity = JSON.parseObject(results, new TypeReference<ResponseEntity<UserRunDto>>() {});
        return responseEntity;
    }

    public ResponseEntity<PageInfo<UserRunDto>> getClubDynamic(String uids, String types, long beginTime, long endTime, int size, int pageNo) {
        String results = userRunClient.getClubDynamic(uids, types, beginTime, endTime, size, pageNo);
        ResponseEntity<PageInfo<UserRunDto>> responseEntity = JSON.parseObject(results, new TypeReference<ResponseEntity<PageInfo<UserRunDto>>>() {});
        return responseEntity;
    }
}
