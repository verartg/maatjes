package com.example.maatjes.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
//    private Role role; <-- dit leren we geloof ik nog


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
