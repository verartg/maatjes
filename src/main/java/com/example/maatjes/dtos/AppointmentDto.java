package com.example.maatjes.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    public Long id;
    public Date date;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String description;
    public String createdByName;
    public String createdForName;
}
