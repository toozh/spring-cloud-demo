package com.ifitmix.common.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifitmix.common.mongo.SaveMongoEventListener;
import com.ifitmix.utils.JsonUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.MultipartConfigElement;

/**
 * Created by zhangtao on 2017/3/20.
 * 基本配置
 */
@EnableMongoRepositories({"com.ifitmix.**.dao"})
@EnableHystrix
@ComponentScan({"com.ifitmix.**.domain", "com.ifitmix.**.service", "com.ifitmix.**.controller"})
public class BaseConfiguration {
    @Bean
    public ApplicationConstant applicationConstant() {
        return new ApplicationConstant();
    }

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return ApplicationContextHolder.getInstance();
    }

    /**
     * 自增主键
     * @return
     */
    @Bean
    private SaveMongoEventListener saveMongoEventListener() {
        return new SaveMongoEventListener();
    }

    /**
     * 去掉 _class 属性
     * @param factory
     * @param context
     * @param beanFactory
     * @return
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        try {
            mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
        }
        catch (NoSuchBeanDefinitionException ignore) {}

        // Don't save _class to mongo
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return mappingConverter;
    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //// 设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;
        factory.setMaxFileSize("128MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("256MB");
        //Sets the directory location where files will be stored.
        //factory.setLocation("路径地址");
        return factory.createMultipartConfig();
    }

    //customize object mapper
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtils.OBJECT_MAPPER;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(objectMapper());
    }
}
