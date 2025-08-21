package com.du.besttrip.ordersb2c.dto;

import com.du.besttrip.ordersb2c.enums.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final Currency DEFAULT_CURRENCY = Currency.RUB;

    public Money(
            BigDecimal amount,
            Currency currency
    ) {
        this.amount = amount.setScale(SCALE, ROUNDING_MODE);
        this.currency = currency;
    }

    public Money(
            BigDecimal amount
    ) {
        this(amount, DEFAULT_CURRENCY);
    }

    public boolean isSame(Money value) {
        return this.amount.compareTo(value.amount()) == 0 && this.currency.equals(value.currency());
    }

    public boolean isCheaperThan(Money value) {
        if (!this.currency.equals(value.currency())) {
            throw new IllegalArgumentException("Currency does not match");
        }
        return this.amount.compareTo(value.amount()) < 0;
    }

    public Money subtract(Money value) {
        Objects.requireNonNull(value, "Value should not be null");
        if (!this.currency().equals(value.currency())) {
            throw new IllegalStateException("Currency does not match");
        }

        return new Money(this.amount.subtract(value.amount), this.currency);
    }
}
