package com.du.besttrip.ordersb2c.controller;

import com.du.besttrip.ordersb2c.avia.model.CreateBookingRequestDto;
import com.du.besttrip.ordersb2c.avia.model.CreateBookingResponseDto;
import com.du.besttrip.ordersb2c.client.AviaClient;
import com.du.besttrip.ordersb2c.dto.OrderDto;
import com.du.besttrip.ordersb2c.exception.BookingValidationException;
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
@RequestMapping("avia")
@Slf4j
@Validated
public class AviaController {
    private final OrdersService ordersService;
    private final AviaClient aviaClient;
    private final AviaService aviaService;

    @PostMapping("/createBooking")
    public CreateBookingResponseDto createBooking(
            @Valid @RequestBody CreateBookingRequestDto request) {

        validateBookingRequest(request);

        CreateBookingResponseDto response = aviaClient.createBooking(request);
        log.debug("Бронирование создано в авиасервисе, orderUid: {}", response.getOrderUid());

        ordersService.saveOrder(response);
        log.info("Бронирование сохранено успешно");
        return response;
    }


    @PostMapping("/test")
    public void testSave(@Valid @RequestBody CreateBookingResponseDto response) {
        ordersService.saveOrder(response);
    }

    @GetMapping("/bookings/{orderUid}")
    public OrderDto getBookingByUid(
            @PathVariable UUID orderUid,
            @RequestHeader("X-User-Id") UUID userUid) {
        log.info("Запрос получения бронирования, orderUid: {}", orderUid);
        log.debug("Проверка существования заказа в базе, orderUid: {}", orderUid);
        return ordersService.getOrderByUid(orderUid, userUid);
    }

    @PostMapping("cancelBooking/{uuid}")
    public void cancelBooking(@PathVariable UUID uuid) {
        aviaService.cancelBooking(uuid);
    }

    @GetMapping("payment/getPaymentPage/{uuid}")
    public String getPaymentPage(@PathVariable UUID uuid) {
        return aviaService.getPaymentPage(uuid);
    }

    private void validateBookingRequest(CreateBookingRequestDto request) {
        if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
            log.warn("Валидация неуспешна. Список пассажиров пуст");
            throw new BookingValidationException("At least one passenger is required");
        }
        if (request.getSelectFlightToken() == null) {
            log.warn("Валидация неуспешна. Нет токена перелета");
            throw new BookingValidationException("Flight token is required");
        }
        if (request.getPayer() == null) {
            log.warn("Валидация неуспешна. Нет информации о плательщике");
            throw new BookingValidationException("Payer information is required");
        }
    }
}