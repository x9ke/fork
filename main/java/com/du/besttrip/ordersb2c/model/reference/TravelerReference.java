package com.du.besttrip.ordersb2c.model.reference;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TravelerReference implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    public UUID id() {
        return id;
    }
}
