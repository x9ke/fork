package com.du.besttrip.ordersb2c.model.jsonb;

import com.du.besttrip.ordersb2c.dto.Money;

public record TicketPrice(
        Money providerPrice,
        Money serviceFee,
        Money totalPrice,
        Money refundAmount
) {
}
