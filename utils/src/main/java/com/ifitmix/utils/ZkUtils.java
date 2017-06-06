package com.ifitmix.utils;

/**
 * Created by zhangtao on 2017/4/18.
 */
public class ZkUtils {

    public static final String ZK_ROOT = "/ifitmix";

    public static String createZkSchedulerLeaderPath(String applicationName) {
        return String.format("%s/%s/schedulers", ZK_ROOT, applicationName);
    }
}
