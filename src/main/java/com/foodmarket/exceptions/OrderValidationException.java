package com.foodmarket.exceptions;

public class OrderValidationException extends RuntimeException{

    public OrderValidationException(String message) {
        super(message);
    }

    public OrderValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
