package com.ifitmix.club.service.gateway;

import com.ifitmix.base.api.CommonErrorCode;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.exception.AppBusinessException;
import com.ifitmix.user.api.dtos.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by zhangtao on 2017/5/11.
 */
@Component
public class UserHystrix implements UserClient {

    private static Logger logger = LoggerFactory.getLogger(UserHystrix.class);

    @Override
    public String findUser(@RequestParam Map<String, Object> map) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> findUserById(@PathVariable("userId") Integer id) {
        logger.info(String.format("UserClient调用findUserById错误 params: {id: %d}", id));
        throw new AppBusinessException(CommonErrorCode.GATEWAY_TIMEOUT);
    }
}
