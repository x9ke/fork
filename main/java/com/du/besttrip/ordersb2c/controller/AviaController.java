package com.du.besttrip.ordersb2c.controller;

import com.du.besttrip.ordersb2c.avia.model.CreateBookingRequestDto;
import com.du.besttrip.ordersb2c.avia.model.CreateBookingResponseDto;
import com.du.besttrip.ordersb2c.dto.OrderDto;
import com.du.besttrip.ordersb2c.service.AviaService;
import com.du.besttrip.ordersb2c.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
@RequestMapping("avia/")
@Slf4j
@Validated
public class AviaController {
    private final OrdersService ordersService;
    private final AviaService aviaService;

    @PostMapping("createBooking")
    public CreateBookingResponseDto createBooking(@Valid @RequestBody CreateBookingRequestDto request) {
        return aviaService.createBooking(request);
    }

    @GetMapping("booking/{orderId}")
    public OrderDto getBookingByUid(@PathVariable UUID orderId) {
        return ordersService.getOrderByUid(orderId);
    }

    @PostMapping("cancelBooking/{uuid}")
    public void cancelBooking(@PathVariable UUID uuid) {
        aviaService.cancelBooking(uuid);
    }

    @GetMapping("payment/getPaymentPage/{uuid}")
    public String getPaymentPage(@PathVariable UUID uuid) {
        return aviaService.getPaymentPage(uuid);
    }

    @PostMapping("/test")
    public void testSave(@Valid @RequestBody CreateBookingResponseDto response) {
        ordersService.saveOrder(response);
    }
}