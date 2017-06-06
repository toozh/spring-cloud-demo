package com.ifitmix.common.spring.utils;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;

/**
 * 内部类也能查找出来
 * Created by zhangtao on 2017/4/21.
 */
public class InnerClassPathScanningCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider {

    public InnerClassPathScanningCandidateComponentProvider(boolean useDefaultFilters) {
        super(useDefaultFilters);
    }

    public InnerClassPathScanningCandidateComponentProvider(boolean useDefaultFilters, Environment environment) {
        super(useDefaultFilters, environment);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isConcrete();
    }
}
