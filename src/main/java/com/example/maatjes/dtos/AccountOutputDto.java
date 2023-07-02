package com.example.maatjes.dtos;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AccountOutputDto {
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
    public String bio;
    public byte[] document;
    public boolean givesHelp;
    public boolean needsHelp;
    public Availability availability;
    public Frequency frequency;
    public List<Activities> activitiesToGive;
    public List<Activities> activitiesToReceive;
    public List<Match> helpReceivers;
    public List<Match> helpGivers;
    public List<Review> givenReviews;
    public List<Review> receivedReviews;
}