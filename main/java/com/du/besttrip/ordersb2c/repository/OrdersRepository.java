package com.du.besttrip.ordersb2c.repository;

import com.du.besttrip.ordersb2c.enums.ProductStatus;
import com.du.besttrip.ordersb2c.model.OrderEntity;
import com.du.besttrip.ordersb2c.model.ProductEntity;
import com.du.besttrip.ordersb2c.model.product.avia.AviaTicketEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersRepository extends BaseJpaRepository<OrderEntity, UUID> {

    @Query("""
    SELECT t FROM OrderEntity o
    JOIN o.products p
    JOIN AviaProductEntity ap ON p.id = ap.id
    JOIN ap.flight f
    JOIN f.legs l
    JOIN l.tickets t
    WHERE p.id = :productId""")
    List<AviaTicketEntity> findAllTicketsByProductId(@Param("productUid") UUID productUid);

    @Query("""
        SELECT p FROM OrderEntity o
        JOIN o.products p
        WHERE p.id = :productUid""")
    Optional<ProductEntity> findProductByUid(@Param("productUid") UUID productUid);

    @Query("UPDATE ProductEntity p SET p.status = :status WHERE p.id = :productId")
    @Modifying
    void updateProductStatus(@Param("productId") UUID productId,
                             @Param("status") ProductStatus status);
}