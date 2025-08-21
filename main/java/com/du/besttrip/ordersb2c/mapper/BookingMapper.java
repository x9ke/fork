package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.CreateBookingResponseDto;
import com.du.besttrip.ordersb2c.enums.OrderStatus;
import com.du.besttrip.ordersb2c.model.OrderEntity;
import com.du.besttrip.ordersb2c.model.ProductEntity;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        uses = {
                AviaProductMapper.class,
                AviaFlightMapper.class,
                AviaLegMapper.class,
                AviaSegmentMapper.class,
                ReferenceMapper.class
        }
)
public abstract class BookingMapper {

    @Autowired
    protected AviaProductMapper aviaProductMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", source = "aviaProduct", qualifiedByName = "mapAviaProductToList")
    @Mapping(target = "status", expression = "java(com.du.besttrip.ordersb2c.enums.OrderStatus.IN_PROGRESS)")
    @Mapping(target = "files", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "personUid", ignore = true)
    @Mapping(target = "travelerUids", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    public abstract OrderEntity toEntity(CreateBookingResponseDto dto);

    @AfterMapping
    protected void afterMapping(@MappingTarget OrderEntity order, CreateBookingResponseDto dto) {
        if (order.getProducts() != null) {
            order.getProducts().forEach(product -> product.setOrder(order));
        }

        if (dto.getAviaProduct() != null && dto.getAviaProduct().getPersonUids() != null
                && !dto.getAviaProduct().getPersonUids().isEmpty()) {
            String firstPersonUid = dto.getAviaProduct().getPersonUids().iterator().next();
            order.setPersonUid(UUID.fromString(firstPersonUid));
        }
    }

    @Named("mapStringToUuid")
    protected UUID mapStringToUuid(String value) {
        return value != null ? UUID.fromString(value) : null;
    }

    @Named("mapAviaProductToList")
    protected List<ProductEntity> mapAviaProductToList(com.du.besttrip.ordersb2c.avia.model.AviaProductDto aviaProduct) {
        if (aviaProduct == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(aviaProductMapper.toEntity(aviaProduct));
    }
}