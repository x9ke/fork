package com.du.besttrip.ordersb2c.config.feign;

import com.du.besttrip.ordersb2c.config.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class OverrideFeignConfig {
    private final AppProperties appProperties;

    @Bean
    @Primary
    public FeignClientProperties feignClientProperties() {
        return appProperties.client();
    }
}
