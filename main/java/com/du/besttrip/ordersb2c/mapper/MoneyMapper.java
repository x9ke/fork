package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.MoneyDto;
import com.du.besttrip.ordersb2c.dto.Money;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MoneyMapper {
    Money toEntity(MoneyDto dto);
    MoneyDto toDto(Money entity);
}