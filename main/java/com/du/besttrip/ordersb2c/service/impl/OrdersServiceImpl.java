package com.du.besttrip.ordersb2c.service.impl;

import com.du.besttrip.ordersb2c.avia.model.CreateBookingResponseDto;
import com.du.besttrip.ordersb2c.dto.OrderDto;
import com.du.besttrip.ordersb2c.exception.NotFoundException;
import com.du.besttrip.ordersb2c.mapper.OrderMapper;
import com.du.besttrip.ordersb2c.mapper.OrderViewMapper;
import com.du.besttrip.ordersb2c.model.OrderEntity;
import com.du.besttrip.ordersb2c.repository.OrdersRepository;
import com.du.besttrip.ordersb2c.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderMapper orderMapper;
    private  final OrderViewMapper orderViewMapper;


    @Override
    public void saveOrder(CreateBookingResponseDto dto) {
        OrderEntity order = orderMapper.toEntity(dto);
        ordersRepository.save(order);
        log.info("Заказ сохранён в БД, orderId={}", order.getId());
    }

    @Override
    public OrderDto getOrderByUid(UUID orderUid) {
        OrderEntity order = ordersRepository.findByIdWithAviaProductsGraph(orderUid)
                .orElseThrow(() -> {
                    log.warn("Заказ с orderUid={} не найден", orderUid);
                    return new NotFoundException("Order not found");
                });

        log.info("Заказ успешно найден, orderUid={}, status={}", orderUid, order.getStatus());
        return orderViewMapper.toDto(order);
    }
}