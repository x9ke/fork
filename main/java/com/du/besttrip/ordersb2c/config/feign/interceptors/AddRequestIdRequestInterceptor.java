package com.du.besttrip.ordersb2c.config.feign.interceptors;

import com.du.besttrip.ordersb2c.constant.HeaderConstants;
import com.du.besttrip.ordersb2c.constant.MDCParam;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.UUID;

public class AddRequestIdRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String requestId = Optional.ofNullable(MDC.get(MDCParam.REQUEST_ID.name()))
                .orElse(UUID.randomUUID().toString());
        requestTemplate.header(HeaderConstants.X_REQUEST_ID, requestId);
    }
}
