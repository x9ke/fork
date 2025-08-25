package com.du.besttrip.ordersb2c.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String id) {
        super(String.format("Object with id - %s not found", id));
    }
}
