package com.du.besttrip.ordersb2c;

import com.du.besttrip.ordersb2c.config.properties.AppProperties;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
@EnableScheduling
@EnableFeignClients
@EnableSchedulerLock(defaultLockAtMostFor = "1m")
public class OrdersB2CApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersB2CApplication.class, args);
	}

}
