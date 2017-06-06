package com.ifitmix.common.spring;

import com.ifitmix.common.spring.excetion.AppErrorController;
import com.ifitmix.common.spring.excetion.AppExceptionHandlerController;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by zhangtao on 2017/3/19.
 */
@EnableSwagger2
public class WebApplication {

    // 错误页面
    @Bean
    public ErrorController errorController(ErrorAttributes errorAttributes) {
        return new AppErrorController(errorAttributes);
    }

    // 异常处理
    @Bean
    public AppExceptionHandlerController appExceptionHandlerController() {
        return new AppExceptionHandlerController();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                .select()
                .apis(requestHandler -> {
                    String packageName = requestHandler.getHandlerMethod().getMethod()
                            .getDeclaringClass().getPackage().getName();
                    return packageName.startsWith("com.ifitmix.") && packageName.contains(".controller");
                })
                .paths(PathSelectors.any())
                .build();
    }


}
