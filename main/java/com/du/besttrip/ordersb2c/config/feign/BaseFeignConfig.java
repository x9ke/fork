package com.du.besttrip.ordersb2c.config.feign;

import com.du.besttrip.ordersb2c.config.feign.interceptors.AddApiKeyRequestInterceptor;
import com.du.besttrip.ordersb2c.config.feign.interceptors.AddRequestIdRequestInterceptor;
import com.du.besttrip.ordersb2c.config.feign.interceptors.AddUserIdRequestInterceptor;
import com.du.besttrip.ordersb2c.config.properties.AppProperties;
import com.du.besttrip.ordersb2c.config.properties.ApplicationClientProperties;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

public abstract class BaseFeignConfig {
    protected final AppProperties appProperties;

    protected BaseFeignConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    protected AddApiKeyRequestInterceptor createApiKeyInterceptor(String clientName) {
        String apiToken = Optional.ofNullable(appProperties.client().getClients().get(clientName))
                .map(ApplicationClientProperties.ApplicationClientConfiguration::getApiToken)
                .orElseThrow(() -> new IllegalStateException("No API token found for " + clientName));
        return new AddApiKeyRequestInterceptor(apiToken);
    }

    @Bean
    public AddRequestIdRequestInterceptor addRequestIdRequestInterceptor() {
        return new AddRequestIdRequestInterceptor();
    }

    @Bean
    public AddUserIdRequestInterceptor addUserIdRequestInterceptor() {
        return new AddUserIdRequestInterceptor();
    }
}