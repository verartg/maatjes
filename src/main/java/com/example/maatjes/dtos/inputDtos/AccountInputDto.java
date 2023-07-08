package com.example.maatjes.dtos.inputDtos;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
import jakarta.validation.constraints.*;
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
public class AccountInputDto {
    @NotNull(message = "Je moet je geboortedatum invullen.")
    @Past(message = "Geboortedatum moet in het verleden liggen.")
    public LocalDate birthdate;
    @NotBlank(message = "Je moet je naam invullen.")
    public String name;
    public char sex;
    public String phoneNumber;
    public String street;
    public String houseNumber;
    @Pattern(regexp = "\\d{4}[A-Za-z]{2}", message = "Ongeldige postcode")
    public String postalCode;
    @NotBlank(message = "Vul de stad in waar je woont.")
    public String city;
    @NotBlank(message = "Er moet een bio worden ingevuld.")
    public String bio;
    public byte[] document;
    public boolean givesHelp;
    public boolean needsHelp;
//todo if boolean givesHelp == true, then @NotNull
    public List<Activities> activitiesToGive;
    //todo if boolean needsHelp == true, then @NotNull
    public List<Activities> activitiesToReceive;
    public Availability availability;
    public Frequency frequency;
}
