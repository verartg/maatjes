package com.example.maatjes.exceptions;

public class FileSizeExceededException extends RuntimeException{
    public FileSizeExceededException(){
        super();
    }

    public FileSizeExceededException(String message) {
        super(message);
    }
}
