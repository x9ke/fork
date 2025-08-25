package com.du.besttrip.ordersb2c.repository;

import com.du.besttrip.ordersb2c.model.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersRepository extends BaseJpaRepository<OrderEntity, UUID> {

    @Query("""
    SELECT DISTINCT o FROM OrderEntity o
    LEFT JOIN FETCH o.products p
    WHERE o.id = :orderId
    AND EXISTS (
        SELECT 1 FROM AviaProductEntity ap
        LEFT JOIN ap.flight f
        LEFT JOIN f.legs l
        LEFT JOIN l.segments s
        LEFT JOIN l.tickets t
        LEFT JOIN t.passengers pass
        WHERE ap.id = p.id
    )""")
    Optional<OrderEntity> findByIdWithAviaProductsGraph(@Param("orderId") UUID orderId);
}