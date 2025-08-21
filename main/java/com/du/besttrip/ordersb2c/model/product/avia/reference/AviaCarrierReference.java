package com.du.besttrip.ordersb2c.model.product.avia.reference;

import com.du.besttrip.ordersb2c.model.reference.SimpleTextReference;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AviaCarrierReference implements SimpleTextReference {
    @Column(nullable = false)
    @NotNull
    private String id;
    @Column(nullable = false)
    @NotNull
    private String name;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }
}
