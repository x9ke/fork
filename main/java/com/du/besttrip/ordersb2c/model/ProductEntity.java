package com.du.besttrip.ordersb2c.model;

import com.du.besttrip.ordersb2c.enums.AccountingProviderAndService;
import com.du.besttrip.ordersb2c.enums.ProductStatus;
import com.du.besttrip.ordersb2c.enums.ProductType;
import com.du.besttrip.ordersb2c.enums.ProviderAndService;
import com.du.besttrip.ordersb2c.model.reference.CityReference;
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
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public abstract class ProductEntity extends AuditableEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ProductStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ProviderAndService provider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private AccountingProviderAndService providerForAccounting;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Set<CityReference> cities = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;

    public abstract ProductType getProductType();
}
