package com.example.maatjes.dtos.inputDtos;

import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.ContactPerson;
import com.example.maatjes.enums.Frequency;
import jakarta.validation.constraints.Future;
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
    public boolean giverAccepted = false;
    public boolean receiverAccepted = false;
    @NotNull(message = "Er moet een contactpersoon worden geselecteerd.")
    public ContactPerson contactPerson;
    @Future(message = "De startdatum voor de match moet in de toekomst liggen.")
    public LocalDate startMatch;
    public LocalDate endMatch;
    @NotNull(message = "Er moet een beschikbaarheid worden geselecteerd.")
    public Availability availability;
    @NotNull(message = "Er moet een frequentie worden geselecteerd.")
    public Frequency frequency;
    @NotNull(message = "Er moet een account worden ingevuld voor degene die hulp gaat geven.")
    public Long helpGiverId;
    @NotNull(message = "Er moet een account worden ingevuld voor degene die hulp gaat krijgen.")
    public Long helpReceiverId;
}
