package com.ifitmix.club.service.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.common.domain.PageInfo;
import com.ifitmix.user.api.dtos.UserDto;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhangtao on 2017/5/11.
 */
@Service
public class UserGateway {

    @Autowired
    private UserClient userClient;

    public ResponseEntity<UserDto> findUserById(Integer id) {
        return userClient.findUserById(id);
    }

    public ResponseEntity<PageInfo<UserDto>> findUser(Map<String, Object> map) {
        String results = userClient.findUser(map);
        ResponseEntity<PageInfo<UserDto>> responseEntity = JSON.parseObject(results, new TypeReference<ResponseEntity<PageInfo<UserDto>>>() {});
        return responseEntity;
    }

}
