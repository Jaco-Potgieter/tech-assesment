package com.pvt.catalog_lifecycle.utilities.custom_exceptions;


public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
