package com.du.besttrip.ordersb2c.model.product.avia.jsonb;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TicketExternalIdMyAgent.class, name = "MYAGENT"),
        @JsonSubTypes.Type(value = TicketExternalIdVipService.class, name = "VIPSERVICE"),
})

public sealed interface TicketExternalId permits TicketExternalIdMyAgent, TicketExternalIdVipService
{

}
