package com.du.besttrip.ordersb2c.service;

import com.du.besttrip.ordersb2c.avia.model.CreateBookingResponseDto;
import com.du.besttrip.ordersb2c.dto.OrderDto;

import java.util.UUID;

public interface OrdersService {
    void saveOrder(CreateBookingResponseDto dto);

    OrderDto getOrderByUid(UUID orderUid, UUID userUuid);
}
