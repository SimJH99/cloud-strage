package com.cloud.backend.global.config;

import com.cloud.backend.global.filter.LoggingFilter;
import com.cloud.backend.global.filter.MdcFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

    @Bean
    public FilterRegistrationBean<MdcFilter> mdcFilter() {
        FilterRegistrationBean<MdcFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new MdcFilter());
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LoggingFilter());
        bean.setOrder(2);
        return bean;
    }
}
