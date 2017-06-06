//package com.ifitmix.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.anonymous().disable()
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable();
////        http.anonymous().disable().authorizeRequests().anyRequest().permitAll();
//    }
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        System.err.println("  ====================WebSecurityConfig  ");
//        web.ignoring().antMatchers("/hystrix.stream/**", "/info", "/error","/uaa/**");
//    }
//}
