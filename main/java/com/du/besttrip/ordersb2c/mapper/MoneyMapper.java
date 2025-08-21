package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.MoneyDto;
import com.du.besttrip.ordersb2c.dto.Money;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoneyMapper {

    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "subtract", ignore = true) // Ignore the subtract field
    Money toEntity(MoneyDto dto);

    MoneyDto toDto(Money entity);
}