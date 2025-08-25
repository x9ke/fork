package com.du.besttrip.ordersb2c.mapper;

import com.du.besttrip.ordersb2c.avia.model.ProviderAndService;
import com.du.besttrip.ordersb2c.avia.model.TicketExternalIdDto;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.TicketExternalId;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.TicketExternalIdMyAgent;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.TicketExternalIdVipService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TicketExternalIdMapper {

    default TicketExternalId toEntity(TicketExternalIdDto dto, @Context ProviderAndService provider) {
        if (dto == null) return null;

        Set<String> passengerIds = dto.getPassengerIds() != null ? new HashSet<>(dto.getPassengerIds()) : new HashSet<>();
        Set<String> passengerKeys = dto.getPassengerKeys() != null ? new HashSet<>(dto.getPassengerKeys()) : new HashSet<>();
        Set<String> passengerUuids = dto.getPassengerUuids() != null ? new HashSet<>(dto.getPassengerUuids()) : new HashSet<>();

        if (provider == ProviderAndService.VIPSERVICE_AVIA) {
            return new TicketExternalIdVipService(
                    dto.getProductUid()
            );
        } else {
            return new TicketExternalIdMyAgent(
                    dto.getLocator(),
                    passengerIds,
                    passengerKeys,
                    passengerUuids
            );
        }
    }
}