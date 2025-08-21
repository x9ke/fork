package com.du.besttrip.ordersb2c.repository;

import com.du.besttrip.ordersb2c.model.product.avia.AviaTicketEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AviaTicketRepository extends BaseJpaRepository<AviaTicketEntity, UUID> {
}
