package com.du.besttrip.ordersb2c.config.feign.interceptors;

import com.du.besttrip.ordersb2c.constant.HeaderConstants;
import com.du.besttrip.ordersb2c.constant.MDCParam;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

import java.util.Optional;

public class AddUserIdRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Optional.ofNullable(MDC.get(MDCParam.USER_ID.name()))
                .ifPresent((userId) -> requestTemplate.header(HeaderConstants.X_USER_ID, userId));
    }
}
