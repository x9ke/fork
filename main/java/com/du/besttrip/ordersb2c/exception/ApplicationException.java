package com.du.besttrip.ordersb2c.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ApplicationException( HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
