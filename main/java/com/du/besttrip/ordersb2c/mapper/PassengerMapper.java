package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.BookingPassengerDto;
import com.du.besttrip.ordersb2c.model.product.avia.AviaPassengerEntity;
import com.du.besttrip.ordersb2c.model.reference.IdentificationDocumentReference;
import com.du.besttrip.ordersb2c.model.reference.PassengerReference;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    @Mapping(target = "passenger", source = ".", qualifiedByName = "mapPassengerReference")
    @Mapping(target = "document", source = "document", qualifiedByName = "mapDocumentReference")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    AviaPassengerEntity toEntity(BookingPassengerDto dto);

    List<com.du.besttrip.ordersb2c.users.model.BookingPassengerDto> toBookingPassengerDtoList(List<BookingPassengerDto> dto);

    com.du.besttrip.ordersb2c.users.model.BookingPassengerDto toBookingPassengerDto(BookingPassengerDto dto);

    //TODO пока не понятно какие поля нужны и где их брать
    @Named("mapPassengerReference")
    default PassengerReference mapTravelerReference(BookingPassengerDto dto) {
        return new PassengerReference(UUID.randomUUID());
    }

    //TODO пока не понятно какие поля нужны и где их брать
    @Named("mapDocumentReference")
    default IdentificationDocumentReference mapDocumentReference(
            com.du.besttrip.ordersb2c.avia.model.PassengerDocumentInfoDto dto) {
        return new IdentificationDocumentReference(UUID.randomUUID());
    }
}