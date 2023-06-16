package com.example.maatjes.dtos;

import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.ContactPerson;
import com.example.maatjes.enums.Frequency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchDto {
    public Long id;
    public boolean accepted;
    public ContactPerson contactPerson;
    public LocalDate startMatch;
    public LocalDate endMatch;
    public Availability availability;
    public Frequency frequency;

}
