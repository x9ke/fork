package com.du.besttrip.ordersb2c.model.product.avia;

import com.du.besttrip.ordersb2c.model.AuditableEntity;
import com.du.besttrip.ordersb2c.model.reference.IdentificationDocumentReference;
import com.du.besttrip.ordersb2c.model.reference.TravelerReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "avia_passenger")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AviaPassengerEntity extends AuditableEntity {
    @Column(nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String ticketNumber;

    @Embedded
    private TravelerReference traveler;

    @Embedded
    private IdentificationDocumentReference document;

    @ManyToOne(fetch = FetchType.LAZY)
    private AviaTicketEntity ticket;
}
