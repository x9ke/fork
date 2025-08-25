package com.du.besttrip.ordersb2c.model;

import com.du.besttrip.ordersb2c.enums.OrderStatus;
import com.du.besttrip.ordersb2c.model.reference.FileReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Entity
// Нельзя использовать "order" т.к. это зарезервированное слово в PostgreSQL https://www.postgresql.org/docs/current/sql-keywords-appendix.html
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class OrderEntity extends AuditableEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OrderStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Set<FileReference> files = new HashSet<>();

    @Column(nullable = false)
    private UUID personUid;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "uuid[]", nullable = false)
    private Set<UUID> passengerUids = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
    private List<ProductEntity> products = new ArrayList<>();
}
