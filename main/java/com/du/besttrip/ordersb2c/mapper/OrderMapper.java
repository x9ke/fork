package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.CreateBookingResponseDto;
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
public abstract class OrderMapper {

    @Autowired
    protected AviaProductMapper aviaProductMapper;

    @Mapping(target = "id", source = "orderUid")
    @Mapping(target = "products", source = "aviaProduct", qualifiedByName = "mapAviaProductToList")
    //TODO где взять статус
    @Mapping(target = "status", expression = "java(com.du.besttrip.ordersb2c.enums.OrderStatus.IN_PROGRESS)")
    @Mapping(target = "files", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "personUid", ignore = true)
    @Mapping(target = "passengerUids", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    public abstract OrderEntity toEntity(CreateBookingResponseDto dto);

    @AfterMapping
    protected void afterMapping(@MappingTarget OrderEntity order, CreateBookingResponseDto dto) {
        order.getProducts().forEach(product -> product.setOrder(order));

        //TODO тут какой то нужен uid
        if (dto.getAviaProduct() != null && dto.getAviaProduct().getPersonUids() != null
                && !dto.getAviaProduct().getPersonUids().isEmpty()) {
            String firstPersonUid = dto.getAviaProduct().getPersonUids().iterator().next();
            order.setPersonUid(UUID.fromString(firstPersonUid));
        }
    }

    @Named("mapAviaProductToList")
    protected List<ProductEntity> mapAviaProductToList(com.du.besttrip.ordersb2c.avia.model.AviaProductDto aviaProduct) {
        if (aviaProduct == null) {
            return Collections.emptyList();
        }
        ProductEntity productEntity = aviaProductMapper.toEntity(aviaProduct);
        return Collections.singletonList(productEntity);
    }
}