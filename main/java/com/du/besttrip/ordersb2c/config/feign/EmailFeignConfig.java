package com.du.besttrip.ordersb2c.config.feign;

import com.du.besttrip.ordersb2c.config.feign.interceptors.AddApiKeyRequestInterceptor;
import com.du.besttrip.ordersb2c.config.properties.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailFeignConfig extends BaseFeignConfig {
    public EmailFeignConfig(AppProperties appProperties) {
        super(appProperties);
    }

    @Bean
    public AddApiKeyRequestInterceptor emailApiKeyRequestInterceptor() {
        return createApiKeyInterceptor("email");
    }
}
