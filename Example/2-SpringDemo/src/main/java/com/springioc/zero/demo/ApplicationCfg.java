package com.springioc.zero.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 容器的配置类
 */
@Configuration
@ComponentScan(basePackages="com.springioc.zero.demo")
public class ApplicationCfg {
    @Bean
    public User getUser(){
        return new User("成功");
    }
}
