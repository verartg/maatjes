package com.example.maatjes.dtos;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    public int age;
//    @NotBlank
    public String name;
//    @NotNull
    public char sex;
    public String phoneNumber;
//    @Email(message = "Invalid email address") @NotBlank
    public String emailAddress;
    public String street;
    public String houseNumber;
//    @Pattern(regexp = "\\d{4}[A-Za-z]{2}", message = "Invalid postal code")
    public String postalCode;
//    @NotBlank
    public String city;
//    @NotBlank
    public String bio;
    public byte[] document;
//    @NotNull
    public boolean givesHelp;
//    @NotNull
    public boolean needsHelp;
//    @NotNull
    public List<Activities> activitiesToGive;
    //    @NotNull
    public List<Activities> activitiesToReceive;
//    @NotNull
    public Availability availability;
//    @NotNull
    public Frequency frequency;
}
