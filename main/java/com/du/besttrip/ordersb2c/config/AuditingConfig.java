package com.du.besttrip.ordersb2c.config;

import com.du.besttrip.ordersb2c.config.spi.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditingConfig {
    @Bean("auditorAware")
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
