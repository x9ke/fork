package com.du.besttrip.ordersb2c.config;

import com.du.besttrip.ordersb2c.config.properties.AppProperties;
import com.du.besttrip.ordersb2c.config.spi.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> getAuthenticationFilter(
            AppProperties appProperties,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver
    ) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter(appProperties, exceptionResolver));
        registrationBean.addUrlPatterns("/api/v1/*");
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }
}
