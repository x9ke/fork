package com.du.besttrip.ordersb2c.dto;

import com.du.besttrip.ordersb2c.avia.model.AviaProductDto;
import com.du.besttrip.ordersb2c.enums.OrderStatus;
import com.du.besttrip.ordersb2c.model.reference.FileReference;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record OrderDto(
        UUID orderUid,
        OrderStatus status,
        Set<FileReference> files,
        UUID personUid,
        Set<UUID> travelerUids,
        List<AviaProductDto> products
) {
}