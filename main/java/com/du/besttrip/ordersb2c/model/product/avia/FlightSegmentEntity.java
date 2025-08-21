package com.du.besttrip.ordersb2c.model.product.avia;

import com.du.besttrip.ordersb2c.enums.FlightClass;
import com.du.besttrip.ordersb2c.model.BaseEntity;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.BaggageInfo;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.FlightSegmentTechStop;
import com.du.besttrip.ordersb2c.model.product.avia.reference.AirportReference;
import com.du.besttrip.ordersb2c.model.product.avia.reference.AviaCarrierReference;
import com.du.besttrip.ordersb2c.model.reference.CityReference;
import com.du.besttrip.ordersb2c.model.reference.CountryReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flight_segment")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class FlightSegmentEntity extends BaseEntity {
    @Embedded
    private AirportReference departureAirport;

    @Embedded
    private CityReference departureCity;

    @Embedded
    private CountryReference departureCountry;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column()
    private String departureTerminal;

    @Embedded
    private AirportReference arrivalAirport;

    @Embedded
    private CityReference arrivalCity;

    @Embedded
    private CountryReference arrivalCountry;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column()
    private String arrivalTerminal;

    @Column(nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private Integer flightDurationMinutes;

    @Column(nullable = false)
    private Integer transferDurationMinutes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private FlightClass flightClass;

    @Embedded
    private AviaCarrierReference operatingCarrier;

    @Column()
    private Integer seatsAvailable;

    @Column(nullable = false)
    private boolean baggageAvailable;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private BaggageInfo baggageInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private BaggageInfo cabinBaggageInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<FlightSegmentTechStop> techStops =  new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private FlightLegEntity leg;
}
