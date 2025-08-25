package com.du.besttrip.ordersb2c.service;

import com.du.besttrip.ordersb2c.avia.model.CreateBookingRequestDto;
import com.du.besttrip.ordersb2c.avia.model.CreateBookingResponseDto;

import java.util.UUID;

public interface AviaService {
    CreateBookingResponseDto createBooking(CreateBookingRequestDto createBookingRequestDto);
    void cancelBooking(UUID productUid);
    String getPaymentPage(UUID uuid);
}