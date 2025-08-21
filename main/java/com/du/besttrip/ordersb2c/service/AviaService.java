package com.du.besttrip.ordersb2c.service;

import java.util.UUID;

public interface AviaService {
    void cancelBooking(UUID productUid);
    String getPaymentPage(UUID uuid);
}