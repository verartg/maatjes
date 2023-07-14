package com.example.maatjes.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsernameNotFoundException(String username) {
        super("De gebruiker met de gebruikersnaam " + username + " kan niet worden gevonden.");
    }

}
