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


//    @NotBlank (message = "Je moet je wachtwoord invullen")
//    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[\\!\\#\\@\\$\\%\\&\\/\\(\\)\\=\\?\\*\\-\\+\\_\\.\\:\\;\\,\\{\\}\\^])[A-Za-z0-9!#@$%&/()=?*+-_.:;,{}]{8,20}", message = "Je wachtwoord moet het volgende bevatten: " +
//            "1. Minimaal 1 kleine letter. 2. Minimaal 1 grote letter. 3. Minimaal 1 getal. 4. Minimaal 1 symbool. 5. Je wachtwoord moet tussen de 8 en 20 karakters bevatten.")
//    public String password;
    @NotNull(message = "Je moet je geboortedatum invullen.")
    @Past(message = "Geboortedatum moet in het verleden liggen.")
    public LocalDate birthdate;
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
    public String[] roles;
}
