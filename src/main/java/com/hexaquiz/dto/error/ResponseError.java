package com.hexaquiz.dto.error;

import org.springframework.http.HttpStatus;

public record ResponseError(
        String message,
        HttpStatus status
){
}
