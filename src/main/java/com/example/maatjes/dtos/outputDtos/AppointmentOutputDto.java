package com.example.maatjes.dtos.outputDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentOutputDto {
    public Long id;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public String description;
    public String createdByName;
    public String createdForName;
}
