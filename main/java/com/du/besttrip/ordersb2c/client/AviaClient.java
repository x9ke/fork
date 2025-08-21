package com.du.besttrip.ordersb2c.client;

import com.du.besttrip.ordersb2c.avia.api.AviaApi;
import com.du.besttrip.ordersb2c.config.feign.AviaFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@FeignClient(value = "avia", configuration = AviaFeignConfig.class)
@Transactional(propagation = Propagation.NEVER)
public interface AviaClient extends AviaApi {
}
