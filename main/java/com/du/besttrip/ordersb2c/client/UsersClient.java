package com.du.besttrip.ordersb2c.client;

import com.du.besttrip.ordersb2c.config.feign.UsersFeignConfig;
import com.du.besttrip.ordersb2c.users.api.PassengerApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@FeignClient(value = "users", configuration = UsersFeignConfig.class)
@Transactional(propagation = Propagation.NEVER)
public interface UsersClient extends PassengerApi {
}