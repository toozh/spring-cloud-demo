package com.ifitmix;

import com.ifitmix.config.AccessFilter;
import com.ifitmix.config.ErrorFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Created by zhangtao on 2017/3/12.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@ComponentScan("com.ifitmix.config")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @Bean
//    public CommonsMultipartResolver commonsMultipartResolver() {
//        return new CommonsMultipartResolver();
//    }

    @Bean
    public AccessFilter accessFilter(RedisTemplate redisTemplate, MongoTemplate mongoTemplate, MultipartResolver commonsMultipartResolver) {
        return new AccessFilter(redisTemplate, mongoTemplate, commonsMultipartResolver);
    }

    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }

}
