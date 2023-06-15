package com.example.maatjes.dtos;

import com.example.maatjes.models.ActivitiesToGive;
import com.example.maatjes.models.ActivitiesToReceive;
import com.example.maatjes.models.Availability;
import com.example.maatjes.models.Frequency;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    public String name;
    public int age;
    public char sex;
    public String phoneNumber;
    @Email(message = "Invalid email address") @NotBlank
    public String emailAddress;
    public String street;
    public String houseNumber;
    @Pattern(regexp = "\\d{4}[A-Za-z]{2}", message = "Invalid postal code")
    public String postalCode;
    @NotBlank
    public String city;
    //    private image profilePicture;
    @NotBlank
    public String bio;
    // private pdf identification;
    public boolean givesHelp;
    public boolean needsHelp;
//    @NotBlank
//    public ActivitiesToGive activitiesToGive;
//    @NotBlank
//    public ActivitiesToReceive activitiesToReceive;
//    @NotBlank
//    public Availability availability;
//    @NotBlank
//    public Frequency frequency;

}
