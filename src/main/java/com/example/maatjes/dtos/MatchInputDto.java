package com.example.maatjes.dtos;

import java.time.LocalDate;

public class MatchInputDto {
    public Long id;
    public boolean accepted;
    public LocalDate startMatch;
    public LocalDate endMatch;
    public String frequency;
    public String activities;

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
