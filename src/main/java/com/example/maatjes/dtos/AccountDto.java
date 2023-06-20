package com.example.maatjes.dtos;

import com.example.maatjes.enums.ActivitiesToGive;
import com.example.maatjes.enums.ActivitiesToReceive;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
import com.example.maatjes.models.Match;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    public byte[] document;
    public boolean givesHelp;
    public boolean needsHelp;
    public ActivitiesToGive activitiesToGive;
    public ActivitiesToReceive activitiesToReceive;
    public Availability availability;
    public Frequency frequency;
    public List<Match> helpReceivers;
    public List<Match> helpGivers;
}