package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.AviaProductPriceDto;
import com.du.besttrip.ordersb2c.model.jsonb.TicketPrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MoneyMapper.class})
public interface PriceMapper {

    TicketPrice toEntity(AviaProductPriceDto dto);
}