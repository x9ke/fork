package com.du.besttrip.ordersb2c.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

@Validated
@PropertySource("classpath:application.yaml")
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        boolean disableAuth,
        @NotBlank
        String appToken,
        @NotNull
        ApplicationClientProperties client
) {
}