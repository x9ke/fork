package com.du.besttrip.ordersb2c.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    NONE("Без статуса"),
    CANCELED("Отменена"),
    IN_PROGRESS("Оформляется"),
    PAID("Оплачена"),
    COMPLETED("Завершена");

    private final String description;
}
