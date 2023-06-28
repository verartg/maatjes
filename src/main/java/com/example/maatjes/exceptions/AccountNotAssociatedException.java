package com.example.maatjes.exceptions;

public class AccountNotAssociatedException extends RuntimeException {

    public AccountNotAssociatedException() {
        super();
    }

    public AccountNotAssociatedException(String message) {super(message);}
}
