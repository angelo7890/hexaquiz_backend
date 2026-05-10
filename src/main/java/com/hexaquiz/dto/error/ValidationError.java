package com.hexaquiz.dto.error;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ValidationError(
        Map<String, String> errors,
        HttpStatus status
) {
}
