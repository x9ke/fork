package com.du.besttrip.ordersb2c.exception.dto;

import jakarta.validation.constraints.NotNull;

public record ValidationError(
        @NotNull
        String fieldName,

        @NotNull
        String message

) {}