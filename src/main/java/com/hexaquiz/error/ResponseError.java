package com.hexaquiz.error;

import org.springframework.http.HttpStatus;

public class ResponseError {
    private String message;
    private HttpStatus error;

    public ResponseError(String message, HttpStatus error) {
        this.message = message;
        this.error = error;
    }
}
