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

    // Method with context to determine which implementation to use
    default TicketExternalId toEntity(TicketExternalIdDto dto, @Context ProviderAndService provider) {
        if (dto == null) return null;

        Set<String> passengerIds = dto.getPassengerIds() != null ? new HashSet<>(dto.getPassengerIds()) : new HashSet<>();
        Set<String> passengerKeys = dto.getPassengerKeys() != null ? new HashSet<>(dto.getPassengerKeys()) : new HashSet<>();
        Set<String> passengerUuids = dto.getPassengerUuids() != null ? new HashSet<>(dto.getPassengerUuids()) : new HashSet<>();

        // Determine which implementation based on provider
        if (provider == ProviderAndService.VIPSERVICE_AVIA) {
            // VipService might have different fields - adjust as needed
            return new TicketExternalIdVipService(
                    dto.getProductUid()
            );
        } else {
            // Default to MyAgent for all other providers
            return new TicketExternalIdMyAgent(
                    dto.getLocator(),
                    passengerIds,
                    passengerKeys,
                    passengerUuids
            );
        }
    }

    // Overloaded method without context (defaults to MyAgent)
    default TicketExternalId toEntity(TicketExternalIdDto dto) {
        return toEntity(dto, ProviderAndService.MYAGENT_AVIA);
    }
}