package com.example.maatjes.util;

import com.example.maatjes.exceptions.FileSizeExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileSizeExceededHandler {

    @ExceptionHandler(FileSizeExceededException.class)
    public ResponseEntity<String> handleFileSizeExceededException(FileSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}