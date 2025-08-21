package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.AviaProductDto;
import com.du.besttrip.ordersb2c.avia.model.CityDto;
import com.du.besttrip.ordersb2c.avia.model.TicketDto;
import com.du.besttrip.ordersb2c.enums.AccountingProviderAndService;
import com.du.besttrip.ordersb2c.enums.ProductStatus;
import com.du.besttrip.ordersb2c.model.ProductEntity;
import com.du.besttrip.ordersb2c.model.product.avia.AviaProductEntity;
import com.du.besttrip.ordersb2c.model.product.avia.AviaTicketEntity;
import com.du.besttrip.ordersb2c.model.product.avia.FlightLegEntity;
import com.du.besttrip.ordersb2c.model.reference.CityReference;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AviaFlightMapper.class, ReferenceMapper.class})
public abstract class AviaProductMapper {

    @Autowired
    protected TicketMapper ticketMapper;

    @Mapping(target = "externalOrderId", source = "externalOrderId")
    @Mapping(target = "externalBillingNumber", source = "externalBillingNumber")
    @Mapping(target = "complexRoute", source = "isComplexRoute")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapProductStatus")
    @Mapping(target = "provider", source = "provider", qualifiedByName = "mapProvider")
    @Mapping(target = "providerForAccounting", source = "providerForAccounting", qualifiedByName = "mapAccountingProvider")
    @Mapping(target = "flight", source = "flight")
    @Mapping(target = "reservationInfo", ignore = true)
    @Mapping(target = "cities", source = "cities")
    @Mapping(target = "id", ignore = true)
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

            // Маппинг билетов к legs
            if (dto.getTickets() != null && !dto.getTickets().isEmpty()) {
                List<FlightLegEntity> legs = entity.getFlight().getLegs();

                for (TicketDto ticketDto : dto.getTickets()) {
                    AviaTicketEntity ticketEntity = ticketMapper.toEntity(ticketDto, dto.getProvider());

                    // Определяем к каким legs относится билет
                    Set<FlightLegEntity> targetLegs = determineLegsForTicket(ticketDto, legs);

                    // Связываем билет с legs
                    for (FlightLegEntity leg : targetLegs) {
                        leg.addTicket(ticketEntity);
                    }

                    // Устанавливаем связи для пассажиров
                    if (ticketEntity.getPassengers() != null) {
                        ticketEntity.getPassengers().forEach(passenger -> {
                            passenger.setTicket(ticketEntity);
                            if (passenger.getExternalId() == null && ticketDto.getPnr() != null) {
                                passenger.setExternalId(ticketDto.getPnr() + "_" + UUID.randomUUID().toString().substring(0, 8));
                            }
                            if (passenger.getTicketNumber() == null) {
                                passenger.setTicketNumber(generateTicketNumber());
                            }
                        });
                    }

                    if (ticketEntity.getTicketNumber() == null) {
                        ticketEntity.setTicketNumber(generateTicketNumber());
                    }
                }
            }
        }
    }

    private Set<FlightLegEntity> determineLegsForTicket(TicketDto ticketDto, List<FlightLegEntity> legs) {
        Set<FlightLegEntity> result = new HashSet<>();

        // Логика определения legs по ticketWay
        if (ticketDto.getTicketWay() != null && !legs.isEmpty()) {
            switch (ticketDto.getTicketWay()) {
                case WAY_THERE -> {
                    if (!legs.isEmpty()) result.add(legs.get(0));
                }
                case WAY_BACK -> {
                    if (legs.size() > 1) result.add(legs.get(legs.size() - 1));
                }
                case ROUND_TRIP -> {
                    if (!legs.isEmpty()) result.add(legs.get(0));
                    if (legs.size() > 1) result.add(legs.get(legs.size() - 1));
                }
                case COMPLEX_ROUTE -> result.addAll(legs);
            }
        }

        // Если не определили - связываем со всеми legs
        if (result.isEmpty()) {
            result.addAll(legs);
        }

        return result;
    }

    private String generateTicketNumber() {
        return "TKT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    public abstract List<AviaProductDto> toDtoList(List<ProductEntity> entities);

    @Named("mapCitiesToDto")
    protected Set<CityDto> mapCitiesToDto(Set<CityReference> cities) {
        if (cities == null) return new java.util.HashSet<>();
        return cities.stream()
                .map(city -> {
                    CityDto dto = new CityDto();
                    dto.setUid(UUID.randomUUID());
                    dto.setName(city.name());
                    return dto;
                })
                .collect(Collectors.toSet());
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
        };
    }
}