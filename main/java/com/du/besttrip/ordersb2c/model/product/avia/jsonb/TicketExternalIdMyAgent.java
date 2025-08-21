package com.du.besttrip.ordersb2c.model.product.avia.jsonb;

import java.util.Set;

public record TicketExternalIdMyAgent(
        String locator,
        Set<String> passengerIds,
        Set<String> passengerKeys,
        Set<String> passengerUuids
) implements TicketExternalId {
}
