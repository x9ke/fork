package com.du.besttrip.ordersb2c.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductType {
    AVIA("Авиа"),
    HOTEL("Отель"),
    RZD("Ж/Д билеты"),
    TRANSFER("Трансферы"),
    CUSTOM("Кастомная услуга");

    private final String info;
}
