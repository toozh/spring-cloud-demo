package com.ifitmix.common.spring;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by zhangtao on 2017/3/30.
 * 使 FeignClients 生效
 */
@EnableFeignClients("com.ifitmix.**.service")
public class ServiceClientConfiguration {

    @Bean(destroyMethod = "shutdown")
    @org.springframework.cloud.context.config.annotation.RefreshScope
    public EurekaClient eurekaClient(ApplicationInfoManager manager,
                                     EurekaClientConfig config,
                                     DiscoveryClient.DiscoveryClientOptionalArgs optionalArgs,
                                     ApplicationContext context) {
        manager.getInfo(); // force initialization
        return new CloudEurekaClient(manager, config, optionalArgs, context);
    }

}
