package com.foodmarket.exceptions;

public class StockQuantityNotSatisfiedException extends RuntimeException{

    public StockQuantityNotSatisfiedException(String message) {
        super(message);
    }

    public StockQuantityNotSatisfiedException(String message, Throwable cause) {
        super(message, cause);
    }

}
