package com.du.besttrip.ordersb2c.config.spi;

import com.du.besttrip.ordersb2c.constant.HeaderConstants;
import com.du.besttrip.ordersb2c.constant.MDCParam;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.logging.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader(HeaderConstants.X_REQUEST_ID);
        String userId = request.getHeader(HeaderConstants.X_USER_ID);
        if (StringUtils.isBlank(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        try {
            MDC.put(MDCParam.REQUEST_ID.name(), requestId);
            if (StringUtils.isNotBlank(userId)) {
                MDC.put(MDCParam.USER_ID.name(), userId);
            }
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
