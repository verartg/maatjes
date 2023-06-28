package com.example.maatjes.controllers;

import com.example.maatjes.exceptions.AccountNotAssociatedException;
import com.example.maatjes.exceptions.FileSizeExceededException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    //    Deze exception handler vangt elke RecordNotFoundException op die naar de gebruiker gegooid wordt en returned daar voor in de plaats een ResponseEntity met de Message en de NOT_FOUND-status (404)
    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<Object> exception(RecordNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> exception(IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = FileSizeExceededException.class)
    public ResponseEntity<Object> exception(FileSizeExceededException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
}

    @ExceptionHandler(value = AccountNotAssociatedException.class)
    public ResponseEntity<Object> exception(AccountNotAssociatedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
