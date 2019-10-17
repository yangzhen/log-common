package com.yxy.common;

import com.yxy.common.config.TraceFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:themis-log-context.xml")
public class LogRecordClientConfigure {

    @Bean
    public FilterRegistrationBean catFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        TraceFilter filter = new TraceFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("log-sdk-filter");
        registration.setOrder(1);
        return registration;
    }
}