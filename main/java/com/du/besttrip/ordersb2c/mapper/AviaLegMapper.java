package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.DurationDto;
import com.du.besttrip.ordersb2c.avia.model.FlightLegDto;
import com.du.besttrip.ordersb2c.model.product.avia.FlightLegEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {AviaSegmentMapper.class})
public interface AviaLegMapper {

    @Mapping(target = "durationMinutes", source = "duration", qualifiedByName = "legDurationToMinutes")
    @Mapping(target = "id", source = "uid")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "flight", ignore = true)
    @Mapping(target = "tickets",  ignore = true)
    FlightLegEntity toEntity(FlightLegDto dto);

    @Named("legDurationToMinutes")
    default Integer durationToMinutes(DurationDto dto) {
        if (dto == null) {
            return null;
        }
        int days = dto.getDays() != null ? dto.getDays() : 0;
        int hours = dto.getHours() != null ? dto.getHours() : 0;
        int minutes = dto.getMinutes() != null ? dto.getMinutes() : 0;
        return days * 24 * 60 + hours * 60 + minutes;
    }
}