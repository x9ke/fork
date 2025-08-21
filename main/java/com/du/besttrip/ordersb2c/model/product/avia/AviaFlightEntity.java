package com.du.besttrip.ordersb2c.model.product.avia;

import com.du.besttrip.ordersb2c.enums.AviaFlightTariff;
import com.du.besttrip.ordersb2c.model.BaseEntity;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.BaggageInfo;
import com.du.besttrip.ordersb2c.model.product.avia.reference.AviaCarrierReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flight")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AviaFlightEntity extends BaseEntity {
    @Column(nullable = false)
    private Integer durationMinutes;

    @Embedded
    private AviaCarrierReference validatingCarrier;

    @Column(nullable = false)
    private boolean additionalFaresAvailable;

    @Column(nullable = false)
    private boolean refundAvailable;

    @Column(nullable = false)
    private boolean exchangeAvailable;

    @Column(nullable = false)
    private boolean baggageAvailable;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private BaggageInfo baggageInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private BaggageInfo cabinBaggageInfo;

    @Column(nullable = false)
    private String fareFamilyType;

    @Column(nullable = false)
    private String fareFamilyMarketingName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AviaFlightTariff tariff;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "flight")
    private List<FlightLegEntity> legs = new ArrayList<>();
}
