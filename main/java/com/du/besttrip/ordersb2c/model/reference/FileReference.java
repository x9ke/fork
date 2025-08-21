package com.du.besttrip.ordersb2c.model.reference;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FileReference(
        @NotNull
        UUID id,
        @NotNull
        String fileName,
        @Nullable
        String publicUrl
) {
}
