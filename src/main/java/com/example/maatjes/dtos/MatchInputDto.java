package com.example.maatjes.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchInputDto {
    public Long id;
    public boolean accepted;
    public LocalDate startMatch;
    public LocalDate endMatch;
    public String frequency;
    public String activities;
}
