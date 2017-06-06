package com.ifitmix.common.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Created by zhangtao on 2017/3/30.
 */
@RefreshScope
public class ApplicationConstant {

    @Value("${spring.cloud.stream.kafka.binder.zkNodes}")
    public String zkAddress;

    @Value("${spring.application.name}")
    public String applicationName;

    /**
     *  阿里云 存储根目录
     */
    @Value("${Aliyun.FileCenter.STORAGE_PATH}")
    public String STORAGE_PATH;

    @Value("${Aliyun.ENDPOINT}")
    public String ALIYUN_ENDPOINT;

    @Value("${Aliyun.ACCESS_ID}")
    public String ALIYUN_ACCESS_ID;

    @Value("${Aliyun.ACCESS_KEY}")
    public String ALIYUN_ACCESS_KEY;

    @Value("${Aliyun.BUCKET_NAME}")
    public String ALIYUN_BUCKET_NAME;

    /**
     * 最大俱乐部人数
     */
    @Value("${APP_CLUB_MAX_MEMBERS}")
    public String APP_CLUB_MAX_MEMBERS;

    /**
     * 置顶俱乐部
     */
    @Value("${APP_CLUB_TOP_CLUB}")
    public String APP_CLUB_TOP_CLUB;

//    @Value("${spring.application.index}")
//    public int applicationIndex;

}
