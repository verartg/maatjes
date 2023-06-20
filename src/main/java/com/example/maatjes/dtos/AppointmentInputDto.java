package com.example.maatjes.dtos;

import jakarta.validation.constraints.NotBlank;
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
public class AppointmentInputDto {
    @NotBlank
    public Date date;
    @NotBlank
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String description;
    public boolean accepted;

    
}
