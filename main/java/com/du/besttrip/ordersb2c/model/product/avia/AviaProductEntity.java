package com.du.besttrip.ordersb2c.model.product.avia;

import com.du.besttrip.ordersb2c.enums.ProductType;
import com.du.besttrip.ordersb2c.model.ProductEntity;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.AviaReservationInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "avia_product")
@DiscriminatorValue("AVIA")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AviaProductEntity extends ProductEntity {
    @Column(nullable = false)
    private String externalOrderId;

    @Column(nullable = false)
    private String externalBillingNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private AviaReservationInfo reservationInfo;

    @Transient
    private final ProductType productType = ProductType.AVIA;

    @Column(nullable = false)
    private boolean complexRoute = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AviaFlightEntity flight;
}
