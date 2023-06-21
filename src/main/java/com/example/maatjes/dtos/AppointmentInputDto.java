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
public class AppointmentInputDto {
    //todo nog constraints implementeren
    public Date date;
    //todo onderstaande alleen als tijd implementeren, nu is er dubbelop data.
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String description;
    public Long matchId;
    //todo onderstaande wil ik niet willen meegeven, maar via principle doen?
    public Long createdById;
    public Long createdForId;
}
