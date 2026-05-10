package com.hexaquiz.exception.handler;

import com.hexaquiz.dto.error.ResponseError;
import com.hexaquiz.dto.error.ValidationError;
import com.hexaquiz.exception.error.ErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ErrorException.class)
    public ResponseEntity<ResponseError> handleErrorException(ErrorException exception) {

        ResponseError errorResponse =
                new ResponseError(exception.getMessage(), exception.getStatus());

        return new ResponseEntity<>(errorResponse, exception.getStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );
        var error = new ValidationError(errors, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseError> handleUsernameNotFound(
            UsernameNotFoundException ex
    ) {

        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
