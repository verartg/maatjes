package com.example.maatjes.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
public class Match {
    @Id
    @GeneratedValue
    private Long id;
    private boolean accepted;
//    private User contactperson; <-- dit wordt een relatie
    private LocalDate startMatch;
    private LocalDate endMatch;
    private String frequency;
    private String activities;
//    private Calendar calendar; <-- ik denk dat deze er niet in hoeft, omdat in het account al een lijst met matches staat waardoor je bij het aanmaken van een afspraak in je account je gematchte kan bereiken?

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDate getStartMatch() {
        return startMatch;
    }

    public void setStartMatch(LocalDate startMatch) {
        this.startMatch = startMatch;
    }

    public LocalDate getEndMatch() {
        return endMatch;
    }

    public void setEndMatch(LocalDate endMatch) {
        this.endMatch = endMatch;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }
}
