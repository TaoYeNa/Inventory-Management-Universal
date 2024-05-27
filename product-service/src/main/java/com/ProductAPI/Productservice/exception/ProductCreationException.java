package com.ProductAPI.Productservice.exception;

/**
 * customized exception class for product creation phase
 */
public class ProductCreationException extends RuntimeException {

    public ProductCreationException() {
        super();
    }

    public ProductCreationException(String message) {
        super(message);
    }

    public ProductCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductCreationException(Throwable cause) {
        super(cause);
    }
}