package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.dto.OrderDto;
import com.du.besttrip.ordersb2c.model.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AviaProductMapper.class})
public interface OrderViewMapper {

    @Mapping(source = "id", target = "orderUid")
    OrderDto toDto(OrderEntity entity);
    List<OrderDto> toDtoList(List<OrderEntity> entities);
}