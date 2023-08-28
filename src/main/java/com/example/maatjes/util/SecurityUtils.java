package com.example.maatjes.util;

import com.example.maatjes.exceptions.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static void validateUsername(String username, String entity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(authentication.getName())) {
            throw new AccessDeniedException("Je hebt alleen toegang tot je eigen " + entity);
        }
    }
}