package com.du.besttrip.ordersb2c.model.product.avia;

import com.du.besttrip.ordersb2c.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

@Entity
@Table(name = "flight_leg")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class FlightLegEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id; // Ручное управление ID

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 1L;

    @Column(nullable = false)
    private Integer durationMinutes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "leg")
    private List<FlightSegmentEntity> segments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private AviaFlightEntity flight;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "leg_ticket",
            joinColumns = @JoinColumn(name = "leg_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private Set<AviaTicketEntity> tickets = new HashSet<>();

    public void addTicket(AviaTicketEntity ticket) {
        tickets.add(ticket);
        ticket.getLegs().add(this);
    }

    public void removeTicket(AviaTicketEntity ticket) {
        tickets.remove(ticket);
        ticket.getLegs().remove(this);
    }
}
