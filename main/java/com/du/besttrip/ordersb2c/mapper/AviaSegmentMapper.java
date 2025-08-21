package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.DurationDto;
import com.du.besttrip.ordersb2c.avia.model.FlightSegmentDto;
import com.du.besttrip.ordersb2c.avia.model.TechStopDto;
import com.du.besttrip.ordersb2c.model.product.avia.FlightSegmentEntity;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.FlightSegmentTechStop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface AviaSegmentMapper {

    @Mapping(target = "departureTime", source = "departureDateTime")
    @Mapping(target = "arrivalTime", source = "arrivalDateTime")
    @Mapping(target = "flightDurationMinutes", source = "flightDuration", qualifiedByName = "segmentDurationToMinutes")
    @Mapping(target = "transferDurationMinutes", source = "transferDuration", qualifiedByName = "segmentDurationToMinutes")
    @Mapping(target = "flightClass", source = "serviceClass")
    @Mapping(target = "techStops", source = "techStops")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "leg", ignore = true)
    FlightSegmentEntity toEntity(FlightSegmentDto dto);

    @Named("segmentDurationToMinutes")
    default Integer durationToMinutes(DurationDto dto) {
        if (dto == null) {
            return null;
        }
        int days = dto.getDays() != null ? dto.getDays() : 0;
        int hours = dto.getHours() != null ? dto.getHours() : 0;
        int minutes = dto.getMinutes() != null ? dto.getMinutes() : 0;
        return days * 24 * 60 + hours * 60 + minutes;
    }

    @Mapping(target = "durationMinutes", source = "duration", qualifiedByName = "segmentDurationToMinutes")
    FlightSegmentTechStop toEntity(TechStopDto dto);
}