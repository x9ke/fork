package com.du.besttrip.ordersb2c.exception.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public record ValidationErrorResponse(
        UUID uid,
        String requestUid,
        long code,
        List<ValidationError> errors,
        LocalDateTime timestamp
) {
    public ValidationErrorResponse(String requestUid, long code, List<ValidationError> errors) {
        this(UUID.randomUUID(), requestUid, code, errors, LocalDateTime.now(ZoneId.of("UTC")));
    }
}
