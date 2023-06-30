package com.example.maatjes.dtos;

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
public class AppointmentInputDto {
    //todo nog constraints implementeren
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public String description;
    public Long matchId;
}
