package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.AviaFlightDto;
import com.du.besttrip.ordersb2c.avia.model.DurationDto;
import com.du.besttrip.ordersb2c.model.product.avia.AviaFlightEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {AviaLegMapper.class, ReferenceMapper.class})
public interface AviaFlightMapper {

    @Mapping(target = "durationMinutes", source = "duration", qualifiedByName = "flightDurationToMinutes")
    @Mapping(target = "validatingCarrier", source = "validatingCarrier")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "tariff", constant = "BEST_LIGHT")
    AviaFlightEntity toEntity(AviaFlightDto dto);

    @Named("flightDurationToMinutes")
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