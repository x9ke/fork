package com.du.besttrip.ordersb2c.model.product.avia.jsonb;

import com.du.besttrip.ordersb2c.model.product.avia.reference.AirportReference;
import com.du.besttrip.ordersb2c.model.reference.CityReference;
import com.du.besttrip.ordersb2c.model.reference.CountryReference;

import java.time.LocalDateTime;

public record FlightSegmentTechStop(
        AirportReference airport,
        CityReference city,
        CountryReference country,
        LocalDateTime arrivalDateTime,
        LocalDateTime departureDateTime,
        Integer durationMinutes
) {
}
