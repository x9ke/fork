package com.du.besttrip.ordersb2c.client;

import com.du.besttrip.ordersb2c.acquiring.api.AcquiringApi;
import com.du.besttrip.ordersb2c.config.feign.AcquiringFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@FeignClient(value = "acquiring", configuration = AcquiringFeignConfig.class)
@Transactional(propagation = Propagation.NEVER)
public interface AcquiringClient extends AcquiringApi {
}