package com.hexaquiz.exception.error;

import org.springframework.http.HttpStatus;

public class ErrorException extends RuntimeException {
    private HttpStatus status;
    public ErrorException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
