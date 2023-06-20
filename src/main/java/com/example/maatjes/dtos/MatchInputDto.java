package com.example.maatjes.dtos;

import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.ContactPerson;
import com.example.maatjes.enums.Frequency;
import com.example.maatjes.models.Account;
import jakarta.validation.constraints.NotNull;
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
    public ContactPerson contactPerson;
    public LocalDate startMatch;
    public LocalDate endMatch;
    public Availability availability;
    public Frequency frequency;
    public Account helpReceiver;
    public Account helpGiver;

}
