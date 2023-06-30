package com.example.maatjes.dtos;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInputDto {
    //todo leeftijd omzetten naar geboortedatum
    @NotNull(message = "Je moet je leeftijd invullen.")
    public Integer age;
    @NotBlank(message = "Je moet je naam invullen.")
    public String name;
    public char sex;
    public String phoneNumber;
    @Email(message = "Ongeldig emailadres") @NotBlank
    public String emailAddress;
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
