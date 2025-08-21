package com.du.besttrip.ordersb2c.controller.advice;

import com.du.besttrip.ordersb2c.constant.HeaderConstants;
import com.du.besttrip.ordersb2c.exception.BookingValidationException;
import com.du.besttrip.ordersb2c.exception.NotFoundException;
import com.du.besttrip.ordersb2c.exception.dto.BaseErrorResponse;
import com.du.besttrip.ordersb2c.exception.dto.ValidationError;
import com.du.besttrip.ordersb2c.exception.dto.ValidationErrorResponse;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class MainControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseErrorResponse<Void>> handleNotFoundException(NotFoundException ex) {
        BaseErrorResponse<Void> errorResponse = new BaseErrorResponse<>(
                MDC.get(HeaderConstants.X_REQUEST_ID),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseErrorResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        BaseErrorResponse<Void> errorResponse = new BaseErrorResponse<>(
                MDC.get(HeaderConstants.X_USER_ID),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(BookingValidationException.class)
    public ResponseEntity<BaseErrorResponse<Void>> handleBookingValidationException(BookingValidationException ex) {
        BaseErrorResponse<Void> errorResponse = new BaseErrorResponse<>(
                MDC.get(HeaderConstants.X_USER_ID),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseErrorResponse<Void>> handleResponseStatusException(ResponseStatusException ex) {
        BaseErrorResponse<Void> errorResponse = new BaseErrorResponse<>(
                MDC.get(HeaderConstants.X_REQUEST_ID),
                ex.getStatusCode().value(),
                ex.getReason()
        );
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseErrorResponse<Void>> handleGenericException(Exception ex) {
        BaseErrorResponse<Void> errorResponse = new BaseErrorResponse<>(
                MDC.get(HeaderConstants.X_REQUEST_ID),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error: " + ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<ValidationErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                MDC.get(HeaderConstants.X_REQUEST_ID),
                HttpStatus.BAD_REQUEST.value(),
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
