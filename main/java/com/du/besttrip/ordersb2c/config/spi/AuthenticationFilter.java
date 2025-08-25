package com.du.besttrip.ordersb2c.config.spi;

import com.du.besttrip.ordersb2c.config.properties.AppProperties;
import com.du.besttrip.ordersb2c.constant.HeaderConstants;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements Filter {
    private final AppProperties appProperties;
    private final HandlerExceptionResolver exceptionResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String apiKey = httpServletRequest.getHeader(HeaderConstants.X_API_KEY);

        if (!appProperties.disableAuth() && !Objects.equals(apiKey, appProperties.appToken())) {
            log.error("Invalid API key - {}", apiKey);
            exceptionResolver.resolveException(
                    httpServletRequest,
                    httpServletResponse,
                    null,
                    new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid API Key")
            );
            return;
        }
        filterChain.doFilter(request, response);

    }
}
