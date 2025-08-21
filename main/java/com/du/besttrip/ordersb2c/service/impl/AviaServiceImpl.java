package com.du.besttrip.ordersb2c.service.impl;

import com.du.besttrip.ordersb2c.acquiring.model.BankType;
import com.du.besttrip.ordersb2c.acquiring.model.Options;
import com.du.besttrip.ordersb2c.acquiring.model.StartFromOrdersRequest;
import com.du.besttrip.ordersb2c.avia.model.CancelBookingRequestDto;
import com.du.besttrip.ordersb2c.client.AcquiringClient;
import com.du.besttrip.ordersb2c.client.AviaClient;
import com.du.besttrip.ordersb2c.enums.AviaTicketStatus;
import com.du.besttrip.ordersb2c.enums.ProductStatus;
import com.du.besttrip.ordersb2c.exception.NotFoundException;
import com.du.besttrip.ordersb2c.model.ProductEntity;
import com.du.besttrip.ordersb2c.model.product.avia.AviaTicketEntity;
import com.du.besttrip.ordersb2c.repository.AviaTicketRepository;
import com.du.besttrip.ordersb2c.repository.ProductRepository;
import com.du.besttrip.ordersb2c.service.AviaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AviaServiceImpl implements AviaService {

    private final ProductRepository productRepository;
    private final AviaClient aviaClient;
    private final AviaTicketRepository aviaTicketRepository;
    private final AcquiringClient acquiringClient;

    @Override
    public void cancelBooking(UUID productUuid) {
        ProductEntity productEntity = productRepository.findProductByUid(productUuid)
                .orElseThrow(() -> new NotFoundException("Product not found with Uid: " + productUuid));
        aviaClient.cancelBooking(new CancelBookingRequestDto()); //TODO нужно переделать avia
        List<AviaTicketEntity> tickets = productRepository.findAllTicketsByProductId(productUuid);
        tickets.forEach(ticket -> ticket.setStatus(AviaTicketStatus.BOOKING_CANCELED));
        aviaTicketRepository.saveAll(tickets);
        productRepository.updateProductStatus(productUuid, ProductStatus.BOOKING_CANCELED);
    }

    @Override
    public String getPaymentPage(UUID uuid) {
        Set<AviaTicketEntity> productTickets = new HashSet<>(productRepository.findAllTicketsByProductId(uuid));
        BigDecimal totalAmount = productTickets.stream()
                .map(x -> x.getTicketPrice().totalPrice().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        String currency = productTickets.iterator().next().getTicketPrice().totalPrice().currency().toString();
        var request = new StartFromOrdersRequest
                .Builder()
                .productId(uuid)
                .amount(totalAmount)
                .currency(currency)
                .description("ОПЛАТА АВИА")
                .build();
        Options options = acquiringClient.startFromOrders(BankType.GAZPROMBANK, request);

        return options.getPaymentPageUrl().toString();
    }
}
