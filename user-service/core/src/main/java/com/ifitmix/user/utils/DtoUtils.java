package com.ifitmix.user.utils;


import com.ifitmix.common.client.AliyunCenterClient;
import com.ifitmix.user.api.dtos.UserDto;
import com.ifitmix.user.domain.User;
import com.ifitmix.utils.ConvertUtil;

/**
 * Created by zhangtao on 2017/3/9.
 */
public class DtoUtils {

    public static UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        if(user == null) {
            return userDto;
        }
        userDto = (UserDto) ConvertUtil.convertBeanTo(user, UserDto.class);
        userDto.setAvatar(AliyunCenterClient.buildUrl(userDto.getAvatar()));
        return userDto;
    }

}
