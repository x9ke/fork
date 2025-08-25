package com.du.besttrip.ordersb2c.config.spi;

import com.du.besttrip.ordersb2c.constant.MDCParam;
import org.jboss.logging.MDC;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String userId = Optional.ofNullable(MDC.get(MDCParam.USER_ID.name()))
                .map(Object::toString)
                .orElse(null);
        return Optional.ofNullable(userId);
    }
}
