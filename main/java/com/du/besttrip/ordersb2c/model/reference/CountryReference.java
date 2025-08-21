package com.du.besttrip.ordersb2c.model.reference;

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
public class CountryReference implements GeoReference {
    @Column(nullable = false)
    @NotNull
    private Long id;
    @Column(nullable = false)
    @NotNull
    private String code;
    @Column(nullable = false)
    @NotNull
    private String name;

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String name() {
        return name;
    }
}
