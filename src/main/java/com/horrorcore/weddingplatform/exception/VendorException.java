package com.horrorcore.weddingplatform.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VendorException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public VendorException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}