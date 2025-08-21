package com.du.besttrip.ordersb2c.model.product.avia;

import com.du.besttrip.ordersb2c.enums.AviaTicketStatus;
import com.du.besttrip.ordersb2c.enums.AviaTicketWay;
import com.du.besttrip.ordersb2c.model.AuditableEntity;
import com.du.besttrip.ordersb2c.model.jsonb.TicketPrice;
import com.du.besttrip.ordersb2c.model.product.avia.jsonb.TicketExternalId;
import com.du.besttrip.ordersb2c.model.reference.FileReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "avia_ticket")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AviaTicketEntity extends AuditableEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AviaTicketStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AviaTicketStatus nextStatusInProcess;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AviaTicketWay ticketWay;

    @Column(nullable = false)
    private String pnr;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Set<FileReference> files = new HashSet<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private TicketExternalId externalId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private TicketPrice ticketPrice;

    // TODO Добавил сюда хотя есть в passenger (как будто тут находиться ему логичнее), но пока не удалял из AviaPassengerEntity
    @Column(nullable = false)
    private String ticketNumber;

    @Column(nullable = false)
    private boolean priceWasChanged;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ticket")
    private Set<AviaPassengerEntity> passengers = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "leg_ticket",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "leg_id")
    )
    private Set<FlightLegEntity> legs = new HashSet<>();

    public void addLeg(FlightLegEntity leg) {
        legs.add(leg);
        leg.addTicket(this);
    }

    public void removeLeg(FlightLegEntity leg) {
        legs.remove(leg);
        leg.removeTicket(this);
    }
}
