package com.ifitmix.user.context;

import com.ifitmix.common.event.config.EventConfiguration;
import com.ifitmix.common.scheduler.config.SchedulerConfiguration;
import com.ifitmix.common.spring.BaseConfiguration;
import com.ifitmix.common.spring.ServiceClientConfiguration;
import com.ifitmix.common.spring.WebApplication;
import com.ifitmix.common.spring.WebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Import;

/**
 * Created by zhangtao on 2017/3/13.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@Import({BaseConfiguration.class, ServiceClientConfiguration.class, WebApplication.class, WebMvcConfiguration.class,
        SchedulerConfiguration.class, EventConfiguration.class})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
