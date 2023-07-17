package com.example.maatjes.dtos.outputDtos;

import com.example.maatjes.enums.Activities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewOutputDto {
    public Long id;
    public double rating;
    public String description;
    public boolean verified;
    public String writtenBy;
    public String writtenFor;
    public List<Activities> activities;
    public String feedbackAdmin;
    public LocalDate date;
}
