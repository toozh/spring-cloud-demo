package com.ifitmix.sports.context;

import com.ifitmix.common.event.config.EventConfiguration;
import com.ifitmix.common.scheduler.ZkSchedulerCoordinator;
import com.ifitmix.common.scheduler.config.SchedulerConfiguration;
import com.ifitmix.common.spring.BaseConfiguration;
import com.ifitmix.common.spring.ServiceClientConfiguration;
import com.ifitmix.common.spring.WebApplication;
import com.ifitmix.common.spring.WebMvcConfiguration;
import com.ifitmix.sports.scheduler.TestScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Created by zhangtao on 2017/3/9.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@Import({BaseConfiguration.class, ServiceClientConfiguration.class, WebApplication.class, WebMvcConfiguration.class,
        SchedulerConfiguration.class, EventConfiguration.class})
public class SportsApplication {

//    @Bean
//    public TestScheduler testScheduler(ZkSchedulerCoordinator zkSchedulerCoordinator) {
//        return new TestScheduler(zkSchedulerCoordinator);
//    }

    public static void main(String[] args) {
        SpringApplication.run(SportsApplication.class, args);
    }
}
