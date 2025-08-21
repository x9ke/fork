package com.du.besttrip.ordersb2c.model.product.avia.jsonb;

public record BaggageInfo(
        Integer piecesAvailable,
        Integer weightPerPiece,
        AviaBaggageInfoDimensions dimensions
) {
}
