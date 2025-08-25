package com.du.besttrip.ordersb2c.client;

import com.du.besttrip.ordersb2c.config.feign.EmailFeignConfig;
import com.du.besttrip.ordersb2c.notification.email.api.NotificationEmailApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@FeignClient(value = "email", configuration = EmailFeignConfig.class)
@Transactional(propagation = Propagation.NEVER)
public interface EmailClient extends NotificationEmailApi {
}
