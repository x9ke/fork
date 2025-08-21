package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.CityDto;
import com.du.besttrip.ordersb2c.avia.model.SimpleTextReferenceDto;
import com.du.besttrip.ordersb2c.model.product.avia.reference.AirportReference;
import com.du.besttrip.ordersb2c.model.product.avia.reference.AviaCarrierReference;
import com.du.besttrip.ordersb2c.model.reference.CityReference;
import com.du.besttrip.ordersb2c.model.reference.CountryReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReferenceMapper {

    AviaCarrierReference toEntity(SimpleTextReferenceDto dto);
    @Mapping(target = "code", source = "name")
    AirportReference toEntityFromAirport(SimpleTextReferenceDto dto);
    @Mapping(target = "code", source = "name")
    CityReference toEntityFromCity(SimpleTextReferenceDto dto);
    @Mapping(target = "code", source = "name")
    CountryReference toEntityFromCountry(SimpleTextReferenceDto dto);
    @Mapping(target = "code", source = "name")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "id", ignore = true)
    CityReference toEntityFromCityDto(CityDto dto);
}