package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.AviaProductDto;
import com.du.besttrip.ordersb2c.avia.model.TicketDto;
import com.du.besttrip.ordersb2c.enums.AccountingProviderAndService;
import com.du.besttrip.ordersb2c.enums.ProductStatus;
import com.du.besttrip.ordersb2c.enums.ProviderAndService;
import com.du.besttrip.ordersb2c.model.product.avia.AviaProductEntity;
import com.du.besttrip.ordersb2c.model.product.avia.AviaTicketEntity;
import com.du.besttrip.ordersb2c.model.product.avia.FlightLegEntity;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AviaFlightMapper.class, ReferenceMapper.class})
public abstract class AviaProductMapper {

    //TODO
    @Autowired
    protected TicketMapper ticketMapper;

    @Mapping(target = "complexRoute", source = "isComplexRoute")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapProductStatus")
    @Mapping(target = "provider", source = "provider", qualifiedByName = "mapProvider")
    @Mapping(target = "providerForAccounting", source = "providerForAccounting", qualifiedByName = "mapAccountingProvider")
    @Mapping(target = "flight", source = "flight")
    @Mapping(target = "reservationInfo", ignore = true)
    @Mapping(target = "cities", source = "cities")
    @Mapping(target = "id", source = "uid")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "order", ignore = true)
    public abstract AviaProductEntity toEntity(AviaProductDto dto);

    @AfterMapping
    protected void afterMapping(@MappingTarget AviaProductEntity entity, AviaProductDto dto) {
        if (entity.getFlight() != null) {
            entity.getFlight().getLegs().forEach(leg -> {
                leg.setFlight(entity.getFlight());
                leg.getSegments().forEach(segment -> segment.setLeg(leg));
            });

            if (dto.getTickets() != null && !dto.getTickets().isEmpty()) {
                List<FlightLegEntity> allLegs = entity.getFlight().getLegs();
                Map<UUID, FlightLegEntity> legsMap = allLegs.stream()
                        .collect(Collectors.toMap(FlightLegEntity::getId, leg -> leg));


                for (TicketDto ticketDto : dto.getTickets()) {
                    AviaTicketEntity ticketEntity = ticketMapper.toEntity(ticketDto, dto.getProvider());

                    Set<FlightLegEntity> targetLegs = determineLegsForTicket(ticketDto, legsMap);

                    ticketEntity.getLegs().clear();
                    ticketEntity.getLegs().addAll(targetLegs);

                    for (FlightLegEntity leg : targetLegs) {
                        leg.getTickets().add(ticketEntity);
                    }

                    if (ticketEntity.getPassengers() != null) {
                        ticketEntity.getPassengers().forEach(passenger -> {
                            passenger.setTicket(ticketEntity);
                        });
                    }
                }
            }
        }
    }

    private Set<FlightLegEntity> determineLegsForTicket(TicketDto ticketDto, Map<UUID, FlightLegEntity> legsMap) {
        Set<FlightLegEntity> result = new HashSet<>();

        if (ticketDto.getLegUids() != null && !ticketDto.getLegUids().isEmpty()) {
            for (String legUidStr : ticketDto.getLegUids()) {
                    UUID legUid = UUID.fromString(legUidStr);
                    FlightLegEntity leg = legsMap.get(legUid);
                    if (leg != null) {
                        result.add(leg);
                    }
            }
        }
        return result;
    }

    @Named("mapProductStatus")
    protected ProductStatus mapProductStatus(com.du.besttrip.ordersb2c.avia.model.ProductStatus dtoStatus) {
        if (dtoStatus == null) return null;
        return switch (dtoStatus) {
            case NEW -> ProductStatus.NEW;
            case BOOKING_CREATED -> ProductStatus.BOOKING_CREATED;
            case ISSUED -> ProductStatus.ISSUED;
            case BOOKING_CANCELED -> ProductStatus.BOOKING_CANCELED;
            case ERROR -> ProductStatus.ERROR;
            case REFUND -> ProductStatus.REFUND;
            case BOOKING_EXPIRED -> ProductStatus.BOOKING_EXPIRED;
            case EXCHANGE_REQUESTED, EXCHANGE -> ProductStatus.ERROR;
        };
    }

    @Named("mapProvider")
    protected com.du.besttrip.ordersb2c.enums.ProviderAndService mapProvider(
            com.du.besttrip.ordersb2c.avia.model.ProviderAndService dtoProvider) {
        if (dtoProvider == null) return null;
        return switch (dtoProvider) {
            case MYAGENT_AVIA -> com.du.besttrip.ordersb2c.enums.ProviderAndService.MYAGENT_AVIA;
            case MYAGENT_HOTEL -> com.du.besttrip.ordersb2c.enums.ProviderAndService.MYAGENT_HOTEL;
            case MYAGENT_RZD -> com.du.besttrip.ordersb2c.enums.ProviderAndService.MYAGENT_RZD;
            case OSTROVOK_HOTEL -> com.du.besttrip.ordersb2c.enums.ProviderAndService.OSTROVOK_HOTEL;
            case IM_RZD -> com.du.besttrip.ordersb2c.enums.ProviderAndService.IM_RZD;
            case VIPSERVICE_AVIA -> com.du.besttrip.ordersb2c.enums.ProviderAndService.VIPSERVICE_AVIA;
            case IWAY_TRANSFER -> com.du.besttrip.ordersb2c.enums.ProviderAndService.IWAY_TRANSFER;
            case DADATA_DICTIONARY -> com.du.besttrip.ordersb2c.enums.ProviderAndService.MYAGENT_AVIA;
            case UNKNOWN -> ProviderAndService.UNKNOWN;
        };
    }

    @Named("mapAccountingProvider")
    protected AccountingProviderAndService mapAccountingProvider(
            com.du.besttrip.ordersb2c.avia.model.ProviderAndService dtoProvider) {
        return switch (dtoProvider) {
            case MYAGENT_AVIA -> AccountingProviderAndService.MYAGENT_AVIA;
            case MYAGENT_HOTEL -> AccountingProviderAndService.MYAGENT_HOTEL;
            case MYAGENT_RZD -> AccountingProviderAndService.MYAGENT_RZD;
            case OSTROVOK_HOTEL -> AccountingProviderAndService.OSTROVOK_HOTEL;
            case IM_RZD -> AccountingProviderAndService.IM_RZD;
            case VIPSERVICE_AVIA -> AccountingProviderAndService.VIPSERVICE_AVIA;
            case IWAY_TRANSFER -> AccountingProviderAndService.IWAY_TRANSFER;
            case DADATA_DICTIONARY -> AccountingProviderAndService.DADATA_DICTIONARY;
            case UNKNOWN -> AccountingProviderAndService.UNKNOWN;
        };
    }
}