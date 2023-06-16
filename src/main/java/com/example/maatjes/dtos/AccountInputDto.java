package com.example.maatjes.dtos;

import com.example.maatjes.enums.ActivitiesToGive;
import com.example.maatjes.enums.ActivitiesToReceive;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInputDto {
    public Long id;
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
    //    private image profilePicture;
//    @NotBlank
    public String bio;
    public byte[] document;
//    @NotNull
    public boolean givesHelp;
//    @NotNull
    public boolean needsHelp;
//    @NotNull
    public ActivitiesToGive activitiesToGive;
//    @NotNull
    public ActivitiesToReceive activitiesToReceive;
//    @NotNull
    public Availability availability;
//    @NotNull
    public Frequency frequency;

}
