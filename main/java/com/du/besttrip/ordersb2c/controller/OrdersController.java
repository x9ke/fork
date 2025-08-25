package com.du.besttrip.ordersb2c.controller;

import com.du.besttrip.ordersb2c.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class OrdersController {
    private final OrdersService ordersService;
}
