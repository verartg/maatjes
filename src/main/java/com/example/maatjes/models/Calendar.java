package com.example.maatjes.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Calendar {
    @Id
    @GeneratedValue
    private Long id;
    //    private ArrayList<Appointment> appointments; <-- dit wordt een relatie

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
