package com.du.besttrip.ordersb2c.exception.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public record BaseErrorResponse<T>(
        UUID uid,
        String requestUid,
        long code,
        String message,
        LocalDateTime timestamp,
        T payload
) {
    public BaseErrorResponse(String requestUid, long code, String message) {
        this(
                UUID.randomUUID(),
                requestUid,
                code,
                message,
                LocalDateTime.now(ZoneId.of("UTC")),
                null
        );
    }
}
