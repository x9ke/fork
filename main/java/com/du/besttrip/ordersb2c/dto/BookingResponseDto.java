package com.du.besttrip.ordersb2c.dto;

import com.du.besttrip.ordersb2c.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public record BookingResponseDto(
        UUID orderUid,
        OrderStatus status,
        List<UUID> travelerUids
) {
}
