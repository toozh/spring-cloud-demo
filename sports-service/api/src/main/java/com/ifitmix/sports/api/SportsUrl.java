package com.ifitmix.sports.api;

/**
 * Created by zhangtao on 2017/3/9.
 */
public interface SportsUrl {

    String SERVICE_NAME = "SPORTS";

    String SERVICE_HOSTNAME = "http://SPORTS-SERVICE";

    String RUN_V1 = "v1/sports/run";

    String RUN_INFO_V1 = "v1/sports/run/{uid}/{startTime}";

    static String buildUrl(String url) {
        return SERVICE_HOSTNAME + url;
    }

}
