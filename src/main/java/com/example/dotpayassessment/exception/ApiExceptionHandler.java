package com.example.dotpayassessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException are){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiException exception = new ApiException(
                are.getMessage(),
                "error",
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, status);
    }

    @ExceptionHandler(value = {ApiNotFoundException.class})
    public ResponseEntity<Object> handleApiNotFoundException(ApiNotFoundException anfe){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiException exception = new ApiException(
                anfe.getMessage(),
                "error",
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(exception, status);
    }
}
