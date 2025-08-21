package com.du.besttrip.ordersb2c.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitJoinTableNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.source.spi.AttributePath;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.spi.NavigablePath;

/**
 * Использует transformAttributePath из ImplicitNamingStrategyComponentPathImpl
 * и determineJoinTableName из SpringImplicitNamingStrategy ( SpringImplicitNamingStrategy используется по умолчанию) {@link org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties}
 *
 * @details
 * applyNamingStrategy(properties, MappingSettings.IMPLICIT_NAMING_STRATEGY, this.implicitStrategy,
 * SpringImplicitNamingStrategy.class::getName);
 * applyNamingStrategy(properties, MappingSettings.PHYSICAL_NAMING_STRATEGY, this.physicalStrategy,
 * CamelCaseToUnderscoresNamingStrategy.class::getName);
 * @see org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
 */
public class BesttripNamingStrategyComponentPathImpl extends ImplicitNamingStrategyJpaCompliantImpl {

    @Override
    public Identifier determineJoinTableName(ImplicitJoinTableNameSource source) {
        String name = source.getOwningPhysicalTableName() + "_"
                + source.getAssociationOwningAttributePath().getProperty();
        return toIdentifier(name, source.getBuildingContext());
    }

    @Override
    protected String transformAttributePath(AttributePath attributePath) {
        final StringBuilder sb = new StringBuilder();
        process(attributePath, sb);
        return sb.toString();
    }

    public static void process(AttributePath attributePath, StringBuilder sb) {
        String property = attributePath.getProperty();
        final AttributePath parent = attributePath.getParent();
        if (parent != null && StringHelper.isNotEmpty(parent.getProperty())) {
            process(parent, sb);
            sb.append('_');
        } else if (NavigablePath.IDENTIFIER_MAPPER_PROPERTY.equals(property)) {
            // skip it, do not pass go
            sb.append("id");
            return;
        }
        property = property.replace("<", "");
        property = property.replace(">", "");

        sb.append(property);
    }
}
