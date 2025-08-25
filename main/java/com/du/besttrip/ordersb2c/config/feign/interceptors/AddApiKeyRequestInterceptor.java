package com.du.besttrip.ordersb2c.config.feign.interceptors;

import com.du.besttrip.ordersb2c.constant.HeaderConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddApiKeyRequestInterceptor implements RequestInterceptor {
    private final String apiKey;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HeaderConstants.X_API_KEY, apiKey);
    }
}
