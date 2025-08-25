package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.*;
import com.du.besttrip.ordersb2c.dto.OrderDto;
import com.du.besttrip.ordersb2c.model.OrderEntity;
import com.du.besttrip.ordersb2c.model.ProductEntity;
import com.du.besttrip.ordersb2c.model.jsonb.TicketPrice;
import com.du.besttrip.ordersb2c.model.product.avia.*;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.*;
import com.du.besttrip.ordersb2c.model.reference.CityReference;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class OrderViewMapper {

    //TODO
    @Autowired
    protected MoneyMapper moneyMapper;

    @Mapping(source = "id", target = "orderUid")
    @Mapping(source = "products", target = "products", qualifiedByName = "mapProductsToDto")
    public abstract OrderDto toDto(OrderEntity entity);

    @Named("mapProductsToDto")
    protected List<AviaProductDto> mapProductsToDto(List<ProductEntity> products) {
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }

        return products.stream()
                .filter(p -> p instanceof AviaProductEntity)
                .map(p -> mapAviaProductToDto((AviaProductEntity) p))
                .collect(Collectors.toList());
    }

    protected AviaProductDto mapAviaProductToDto(AviaProductEntity entity) {
        if (entity == null) {
            return null;
        }

        AviaProductDto dto = new AviaProductDto();

        dto.setType("AviaProductDto");
        dto.setUid(entity.getId());
        dto.setExternalOrderId(entity.getExternalOrderId());
        dto.setExternalBillingNumber(entity.getExternalBillingNumber());
        dto.setIsComplexRoute(entity.isComplexRoute());
        dto.setBookingTimeLimitExpired(false); // TODO: вычислять на основе времени
        dto.setExchanged(false); // TODO: добавить поле в entity если нужно
        dto.setAllowModification(true); // TODO: вычислять на основе статуса

        dto.setStatus(mapProductStatus(entity.getStatus()));
        dto.setNextStatusInProcess(ProductStatus.ISSUED); // TODO: вычислять логику

        dto.setProvider(mapProvider(entity.getProvider()));
        dto.setProviderForAccounting(mapProvider(entity.getProviderForAccounting()));

        dto.setCities(mapCitiesToDto(entity.getCities()));

        dto.setPersonUids(new HashSet<>()); // TODO: получить из связанных данных

        if (entity.getReservationInfo() != null) {
            Map<String, String> customAttrs = new HashMap<>();
            if (entity.getReservationInfo().vipServiceReservationToken() != null) {
                customAttrs.put("VIP_SERVICE_RESERVATION_TOKEN",
                        entity.getReservationInfo().vipServiceReservationToken());
            }
            dto.setCustomAttributes(customAttrs);
        } else {
            dto.setCustomAttributes(new HashMap<>());
        }

        if (entity.getFlight() != null) {
            dto.setFlight(mapFlightToDto(entity.getFlight()));
        }

        List<TicketDto> tickets = collectTicketsFromLegs(entity);
        dto.setTickets(tickets);

        dto.setAviaProductPrice(calculateProductPrice(tickets));

        dto.setName(generateProductName(entity));

        return dto;
    }

    protected AviaFlightDto mapFlightToDto(AviaFlightEntity flight) {
        if (flight == null) {
            return null;
        }

        AviaFlightDto dto = new AviaFlightDto();

        dto.setDuration(minutesToDuration(flight.getDurationMinutes()));

        if (flight.getValidatingCarrier() != null) {
            SimpleTextReferenceDto carrier = new SimpleTextReferenceDto();
            carrier.setId(flight.getValidatingCarrier().id());
            carrier.setName(flight.getValidatingCarrier().name());
            dto.setValidatingCarrier(carrier);
        }

        dto.setAdditionalFaresAvailable(flight.isAdditionalFaresAvailable());
        dto.setRefundAvailable(flight.isRefundAvailable());
        dto.setExchangeAvailable(flight.isExchangeAvailable());
        dto.setBaggageAvailable(flight.isBaggageAvailable());

        dto.setFareFamilyType(flight.getFareFamilyType());
        dto.setFareFamilyMarketingName(flight.getFareFamilyMarketingName());

        dto.setBaggageInfo(mapBaggageInfo(flight.getBaggageInfo()));
        dto.setCabinBaggageInfo(mapBaggageInfo(flight.getCabinBaggageInfo()));

        if (flight.getLegs() != null) {
            dto.setLegs(flight.getLegs().stream()
                    .map(this::mapLegToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    protected FlightLegDto mapLegToDto(FlightLegEntity leg) {
        if (leg == null) {
            return null;
        }

        FlightLegDto dto = new FlightLegDto();
        dto.setUid(leg.getId());
        dto.setDuration(minutesToDuration(leg.getDurationMinutes()));

        if (leg.getSegments() != null) {
            dto.setSegments(leg.getSegments().stream()
                    .map(this::mapSegmentToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    protected FlightSegmentDto mapSegmentToDto(FlightSegmentEntity segment) {
        if (segment == null) {
            return null;
        }

        FlightSegmentDto dto = new FlightSegmentDto();

        // Аэропорты и города
        dto.setDepartureAirport(mapAirportReference(segment.getDepartureAirport()));
        dto.setDepartureCity(mapCityReference(segment.getDepartureCity()));
        dto.setDepartureCountry(mapCountryReference(segment.getDepartureCountry()));
        dto.setDepartureDateTime(segment.getDepartureTime());
        dto.setDepartureTerminal(segment.getDepartureTerminal());

        dto.setArrivalAirport(mapAirportReference(segment.getArrivalAirport()));
        dto.setArrivalCity(mapCityReference(segment.getArrivalCity()));
        dto.setArrivalCountry(mapCountryReference(segment.getArrivalCountry()));
        dto.setArrivalDateTime(segment.getArrivalTime());
        dto.setArrivalTerminal(segment.getArrivalTerminal());

        dto.setFlightNumber(segment.getFlightNumber());
        dto.setCarrierTripNumber(segment.getFlightNumber()); // TODO: добавить поле если отличается
        dto.setServiceClass(mapFlightClass(segment.getFlightClass()));

        dto.setFlightDuration(minutesToDuration(segment.getFlightDurationMinutes()));
        dto.setTransferDuration(minutesToDuration(segment.getTransferDurationMinutes()));

        if (segment.getOperatingCarrier() != null) {
            SimpleTextReferenceDto carrier = new SimpleTextReferenceDto();
            carrier.setId(segment.getOperatingCarrier().id());
            carrier.setName(segment.getOperatingCarrier().name());
            dto.setOperatingCarrier(carrier);
            dto.setValidatingCarrier(carrier); // TODO: может отличаться
        }

        SimpleTextReferenceDto aircraft = new SimpleTextReferenceDto();
        aircraft.setId("320"); // TODO: добавить поле в entity
        aircraft.setName("Airbus A320");
        dto.setAircraft(aircraft);

        dto.setSeatsAvailable(segment.getSeatsAvailable());
        dto.setBaggageAvailable(segment.isBaggageAvailable());
        dto.setBaggageInfo(mapBaggageInfo(segment.getBaggageInfo()));
        dto.setCabinBaggageInfo(mapBaggageInfo(segment.getCabinBaggageInfo()));

        dto.setTechStops(mapTechStops(segment.getTechStops()));

        return dto;
    }

    protected List<TicketDto> collectTicketsFromLegs(AviaProductEntity product) {
        Set<AviaTicketEntity> allTickets = new HashSet<>();

        if (product.getFlight() != null && product.getFlight().getLegs() != null) {
            for (FlightLegEntity leg : product.getFlight().getLegs()) {
                if (leg.getTickets() != null) {
                    allTickets.addAll(leg.getTickets());
                }
            }
        }

        return allTickets.stream()
                .map(this::mapTicketToDto)
                .collect(Collectors.toList());
    }

    protected TicketDto mapTicketToDto(AviaTicketEntity ticket) {
        if (ticket == null) {
            return null;
        }

        TicketDto dto = new TicketDto();
        dto.setUid(ticket.getId());
        dto.setStatus(mapTicketStatus(ticket.getStatus()));
        dto.setNextStatusInProcess(mapTicketStatus(ticket.getNextStatusInProcess()));
        dto.setPnr(ticket.getPnr());
        dto.setPriceWasChanged(ticket.isPriceWasChanged());
        dto.setTicketWay(mapTicketWay(ticket.getTicketWay()));

        if (ticket.getTicketPrice() != null) {
            dto.setTicketPrice(mapTicketPrice(ticket.getTicketPrice()));
        }

        if (ticket.getExternalId() != null) {
            dto.setExternalId(mapExternalId(ticket.getExternalId()));
        }

        if (ticket.getPassengers() != null) {
            dto.setPassengers(ticket.getPassengers().stream()
                    .map(this::mapPassengerToDto)
                    .collect(Collectors.toList()));
        }

        if (ticket.getLegs() != null) {
            dto.setLegUids(ticket.getLegs().stream()
                    .map(leg -> leg.getId().toString())
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    protected BookingPassengerDto mapPassengerToDto(AviaPassengerEntity passenger) {
        if (passenger == null) {
            return null;
        }

        BookingPassengerDto dto = new BookingPassengerDto();

        // TODO: Получить данные из TravelerReference
        dto.setFirstName("Иван");
        dto.setLastName("Иванов");
        dto.setMiddleName("Иванович");
        dto.setPersonHasNoMiddleName(false);
        dto.setBirthDate(java.time.LocalDate.of(1990, 5, 15));
        dto.setGender(BookingPassengerDto.GenderEnum.MALE);
        dto.setType(PassengerType.ADULT);

        // TODO: Получить данные из TravelerReference
        PassengerDocumentInfoDto docDto = new PassengerDocumentInfoDto();
        docDto.setType(PassengerDocumentInfoDto.TypeEnum.RUSSIAN_PASSPORT);
        docDto.setNumber("4510 123456");
        docDto.setIssuedDate(java.time.LocalDate.of(2015, 6, 20));
        dto.setDocument(docDto);

        // TODO: Получить данные из TravelerReference
        CitizenshipDto citizenship = new CitizenshipDto();
        citizenship.setUid(UUID.randomUUID());
        citizenship.setName("Российская Федерация");
        dto.setCitizenship(citizenship);

        return dto;
    }

    protected DurationDto minutesToDuration(Integer minutes) {
        if (minutes == null) return null;

        DurationDto dto = new DurationDto();
        dto.setDays(minutes / (24 * 60));
        dto.setHours((minutes % (24 * 60)) / 60);
        dto.setMinutes(minutes % 60);
        return dto;
    }

    protected BaggageInfoDto mapBaggageInfo(BaggageInfo info) {
        if (info == null) return null;

        BaggageInfoDto dto = new BaggageInfoDto();
        dto.setPiecesAvailable(info.piecesAvailable());
        dto.setWeightPerPiece(info.weightPerPiece());

        if (info.dimensions() != null) {
            BaggageDimensionsDto dims = new BaggageDimensionsDto();
            dims.setWidth(info.dimensions().width());
            dims.setLength(info.dimensions().length());
            dims.setHeight(info.dimensions().height());
            dto.setDimensions(dims);
        }

        return dto;
    }

    protected List<TechStopDto> mapTechStops(List<FlightSegmentTechStop> techStops) {
        if (techStops == null) return new ArrayList<>();

        return techStops.stream()
                .map(this::mapTechStop)
                .collect(Collectors.toList());
    }

    protected TechStopDto mapTechStop(FlightSegmentTechStop techStop) {
        if (techStop == null) return null;

        TechStopDto dto = new TechStopDto();
        dto.setAirport(mapAirportReference(techStop.airport()));
        dto.setCity(mapCityReference(techStop.city()));
        dto.setCountry(mapCountryReference(techStop.country()));
        dto.setArrivalDateTime(techStop.arrivalDateTime());
        dto.setDepartureDateTime(techStop.departureDateTime());
        dto.setDuration(minutesToDuration(techStop.durationMinutes()));
        return dto;
    }

    protected SimpleTextReferenceDto mapAirportReference(com.du.besttrip.ordersb2c.model.product.avia.reference.AirportReference ref) {
        if (ref == null) return null;

        SimpleTextReferenceDto dto = new SimpleTextReferenceDto();
        dto.setId(ref.id().toString());
        dto.setName(ref.name());
        return dto;
    }

    protected SimpleTextReferenceDto mapCityReference(com.du.besttrip.ordersb2c.model.reference.CityReference ref) {
        if (ref == null) return null;

        SimpleTextReferenceDto dto = new SimpleTextReferenceDto();
        dto.setId(ref.id().toString());
        dto.setName(ref.name());
        return dto;
    }

    protected SimpleTextReferenceDto mapCountryReference(com.du.besttrip.ordersb2c.model.reference.CountryReference ref) {
        if (ref == null) return null;

        SimpleTextReferenceDto dto = new SimpleTextReferenceDto();
        dto.setId(ref.id().toString());
        dto.setName(ref.name());
        return dto;
    }

    protected Set<CityDto> mapCitiesToDto(Set<CityReference> cities) {
        if (cities == null) return new HashSet<>();

        return cities.stream()
                .map(city -> {
                    CityDto dto = new CityDto();
                    dto.setUid(UUID.randomUUID());
                    dto.setName(city.name());
                    return dto;
                })
                .collect(Collectors.toSet());
    }

    protected AviaProductPriceDto mapTicketPrice(TicketPrice price) {
        if (price == null) return null;

        AviaProductPriceDto dto = new AviaProductPriceDto();
        dto.setProviderPrice(moneyMapper.toDto(price.providerPrice()));
        dto.setServiceFee(moneyMapper.toDto(price.serviceFee()));
        dto.setTotalPrice(moneyMapper.toDto(price.totalPrice()));
        dto.setRefundAmount(moneyMapper.toDto(price.refundAmount()));

        // Вычисляемые поля
        dto.setTotalPayedMinusTotalReturned(moneyMapper.toDto(price.totalPrice()));
        dto.setTotalPayedMinusTotalReturnedMinusServiceFee(
                moneyMapper.toDto(price.providerPrice()));

        return dto;
    }

    protected AviaProductPriceDto calculateProductPrice(List<TicketDto> tickets) {
        // TODO: правильно вычислить общую цену из билетов
        AviaProductPriceDto dto = new AviaProductPriceDto();

        MoneyDto zeroMoney = new MoneyDto();
        zeroMoney.setAmount(java.math.BigDecimal.ZERO);
        zeroMoney.setCurrency(CurrencyDto.RUB);

        dto.setProviderPrice(zeroMoney);
        dto.setServiceFee(zeroMoney);
        dto.setTotalPrice(zeroMoney);
        dto.setRefundAmount(zeroMoney);
        dto.setTotalPayedMinusTotalReturned(zeroMoney);
        dto.setTotalPayedMinusTotalReturnedMinusServiceFee(zeroMoney);

        return dto;
    }

    protected TicketExternalIdDto mapExternalId(TicketExternalId externalId) {
        if (externalId == null) return null;

        TicketExternalIdDto dto = new TicketExternalIdDto();

        if (externalId instanceof TicketExternalIdMyAgent myAgent) {
            dto.setLocator(myAgent.locator());
            dto.setPassengerIds(new ArrayList<>(myAgent.passengerIds()));
            dto.setPassengerKeys(new ArrayList<>(myAgent.passengerKeys()));
            dto.setPassengerUuids(new ArrayList<>(myAgent.passengerUuids()));
        } else if (externalId instanceof TicketExternalIdVipService vipService) {
            dto.setProductUid(vipService.productUid());
        }

        return dto;
    }

    protected String generateProductName(AviaProductEntity entity) {
            List<String> cityNames = entity.getCities().stream()
                    .map(CityReference::name)
                    .collect(Collectors.toList());
            return String.join(" - ", cityNames);
    }

    protected ProductStatus mapProductStatus(com.du.besttrip.ordersb2c.enums.ProductStatus status) {
        if (status == null) return null;
        return ProductStatus.valueOf(status.name());
    }

    protected ProviderAndService mapProvider(com.du.besttrip.ordersb2c.enums.ProviderAndService provider) {
        if (provider == null) return null;
        return ProviderAndService.valueOf(provider.name());
    }

    protected ProviderAndService mapProvider(com.du.besttrip.ordersb2c.enums.AccountingProviderAndService provider) {
        if (provider == null) return null;

        return switch (provider) {
            case MYAGENT_AVIA -> ProviderAndService.MYAGENT_AVIA;
            case VIPSERVICE_AVIA -> ProviderAndService.VIPSERVICE_AVIA;
            case IWAY_TRANSFER -> ProviderAndService.IWAY_TRANSFER;
            case MYAGENT_RZD -> ProviderAndService.MYAGENT_RZD;
            case IM_RZD -> ProviderAndService.IM_RZD;
            case OSTROVOK_HOTEL -> ProviderAndService.OSTROVOK_HOTEL;
            case MYAGENT_HOTEL -> ProviderAndService.MYAGENT_HOTEL;
            case DADATA_DICTIONARY -> ProviderAndService.DADATA_DICTIONARY;
            case UNKNOWN -> ProviderAndService.UNKNOWN;
        };
    }

    protected AviaTicketStatus mapTicketStatus(com.du.besttrip.ordersb2c.enums.AviaTicketStatus status) {
        if (status == null) return null;
        return AviaTicketStatus.valueOf(status.name());
    }

    protected FlightClassDto mapFlightClass(com.du.besttrip.ordersb2c.enums.FlightClass flightClass) {
        if (flightClass == null) return null;
        return FlightClassDto.valueOf(flightClass.name());
    }

    protected TicketWayDto mapTicketWay(com.du.besttrip.ordersb2c.enums.AviaTicketWay ticketWay) {
        if (ticketWay == null) return null;
        return TicketWayDto.valueOf(ticketWay.name());
    }
}