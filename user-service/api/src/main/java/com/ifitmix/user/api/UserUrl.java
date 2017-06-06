package com.ifitmix.user.api;

/**
 * Created by zhangtao on 2017/3/13.
 */
public interface UserUrl {

    String SERVICE_NAME = "USER-SERVICE";

    String SERVICE_HOSTNAME = "http://USER-SERVICE";

    String USER_V1 = "v1/user";

    String USER_INFO_V1 = "v1/user/{userId}";

    static String buildUrl(String url) {
        return SERVICE_HOSTNAME + url;
    }

}
