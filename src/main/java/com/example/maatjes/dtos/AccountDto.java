package com.example.maatjes.dtos;

import com.example.maatjes.models.ActivitiesToGive;
import com.example.maatjes.models.ActivitiesToReceive;
import com.example.maatjes.models.Availability;
import com.example.maatjes.models.Frequency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AccountDto {
    public Long id;
    public String name;
    public int age;
    public char sex;
    public String phoneNumber;
    public String emailAddress;
    public String street;
    public String houseNumber;
    public String postalCode;
    public String city;
    //    private image profilePicture;
    public String bio;
    // private pdf identification;
    public boolean givesHelp;
    public boolean needsHelp;
//    public ActivitiesToGive activitiesToGive;
//    public ActivitiesToReceive activitiesToReceive;
//    public Availability availability;
//    public Frequency frequency;

}