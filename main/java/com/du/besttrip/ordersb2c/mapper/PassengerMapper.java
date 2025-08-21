package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.BookingPassengerDto;
import com.du.besttrip.ordersb2c.model.product.avia.AviaPassengerEntity;
import com.du.besttrip.ordersb2c.model.reference.IdentificationDocumentReference;
import com.du.besttrip.ordersb2c.model.reference.TravelerReference;
import org.mapstruct.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    @Mapping(target = "traveler", source = ".", qualifiedByName = "mapTravelerReference")
    @Mapping(target = "document", source = "document", qualifiedByName = "mapDocumentReference")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "ticketNumber", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    AviaPassengerEntity toEntity(BookingPassengerDto dto);

    @Named("mapTravelerReference")
    default TravelerReference mapTravelerReference(BookingPassengerDto dto) {
        if (dto == null) return null;

        // Generate deterministic UUID based on traveler data
        String travelerKey = String.format("%s|%s|%s|%s",
                dto.getFirstName() != null ? dto.getFirstName() : "",
                dto.getLastName() != null ? dto.getLastName() : "",
                dto.getBirthDate() != null ? dto.getBirthDate().toString() : "",
                dto.getMiddleName() != null ? dto.getMiddleName() : ""
        );

        UUID travelerId = generateDeterministicUUID(travelerKey);
        return new TravelerReference(travelerId);
    }

    @Named("mapDocumentReference")
    default IdentificationDocumentReference mapDocumentReference(
            com.du.besttrip.ordersb2c.avia.model.PassengerDocumentInfoDto dto) {
        if (dto == null) return null;

        // Generate deterministic UUID based on document data
        String documentKey = String.format("%s|%s",
                dto.getType() != null ? dto.getType().toString() : "",
                dto.getNumber() != null ? dto.getNumber() : ""
        );

        UUID documentId = generateDeterministicUUID(documentKey);
        return new IdentificationDocumentReference(documentId);
    }

    default UUID generateDeterministicUUID(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Use first 16 bytes of hash to create UUID
            long msb = 0;
            long lsb = 0;
            for (int i = 0; i < 8; i++) {
                msb = (msb << 8) | (hash[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (hash[i] & 0xff);
            }

            return new UUID(msb, lsb);
        } catch (NoSuchAlgorithmException e) {
            // Fallback to random UUID
            return UUID.randomUUID();
        }
    }
}