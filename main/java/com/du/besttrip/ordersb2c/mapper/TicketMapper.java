package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.TicketDto;
import com.du.besttrip.ordersb2c.avia.model.ProviderAndService;
import com.du.besttrip.ordersb2c.enums.AviaTicketStatus;
import com.du.besttrip.ordersb2c.enums.AviaTicketWay;
import com.du.besttrip.ordersb2c.model.product.avia.AviaTicketEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PassengerMapper.class, PriceMapper.class, TicketExternalIdMapper.class})
public interface TicketMapper {

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "ticketWay", source = "ticketWay", qualifiedByName = "mapTicketWay")
    @Mapping(target = "pnr", source = "pnr")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapTicketStatus")
    @Mapping(target = "nextStatusInProcess", source = "nextStatusInProcess", qualifiedByName = "mapTicketStatus")
    @Mapping(target = "externalId", source = "externalId", qualifiedByName = "mapExternalId")
    @Mapping(target = "ticketPrice", source = "ticketPrice")
    @Mapping(target = "priceWasChanged", source = "priceWasChanged")
    @Mapping(target = "ticketNumber", source = "number")
    @Mapping(target = "files", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "passengers", source = "passengers")
    @Mapping(target = "legs", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    AviaTicketEntity toEntity(TicketDto dto, @Context ProviderAndService provider);

    @Named("mapExternalId")
    default com.du.besttrip.ordersb2c.model.product.avia.jsonb.TicketExternalId mapExternalId(
            com.du.besttrip.ordersb2c.avia.model.TicketExternalIdDto dto,
            @Context ProviderAndService provider) {
        TicketExternalIdMapper mapper = org.mapstruct.factory.Mappers.getMapper(TicketExternalIdMapper.class);
        return mapper.toEntity(dto, provider);
    }

    @Named("mapTicketStatus")
    default AviaTicketStatus mapTicketStatus(com.du.besttrip.ordersb2c.avia.model.AviaTicketStatus dtoStatus) {
        if (dtoStatus == null) return null;
        return switch (dtoStatus) {
            case BOOKING_CREATED -> AviaTicketStatus.BOOKING_CREATED;
            case BOOKING_CANCELED -> AviaTicketStatus.BOOKING_CANCELED;
            case BOOKING_EXPIRED -> AviaTicketStatus.BOOKING_EXPIRED;
            case BOOKING_ISSUED, ISSUED -> AviaTicketStatus.ISSUED;
            case REFUND -> AviaTicketStatus.REFUND;
            case ERROR -> AviaTicketStatus.ERROR;
            case EXCHANGE_REQUESTED -> AviaTicketStatus.REFUND;
            case EXCHANGE -> AviaTicketStatus.ERROR;
        };
    }

    @Named("mapTicketWay")
    default AviaTicketWay mapTicketWay(com.du.besttrip.ordersb2c.avia.model.TicketWayDto dtoTicketWay) {
        if (dtoTicketWay == null) return null;
        return switch (dtoTicketWay) {
            case WAY_THERE -> AviaTicketWay.WAY_THERE;
            case WAY_BACK -> AviaTicketWay.WAY_BACK;
            case ROUND_TRIP -> AviaTicketWay.ROUND_TRIP;
            case COMPLEX_ROUTE -> AviaTicketWay.COMPLEX_ROUTE;
        };
    }
}