package com.example.maatjes.dtos.inputDtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    public Long id;
    @Future(message = "De afspraak moet in de toekomst liggen.")
    public LocalDate date;
    @NotNull(message = "De begintijd moet worden ingevuld.")
    public LocalTime startTime;
    @NotNull(message = "De eindtijd moet worden ingevuld.")
    public LocalTime endTime;
    @NotBlank(message = "Er moet een beschrijving worden ingevuld.")
    public String description;
    public Long matchId;
}

