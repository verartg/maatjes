package com.example.maatjes.dtos.outputDtos;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
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
    public String name;
    public int age;
    public Character sex;
    public String city;
    public String bio;
    public boolean givesHelp;
    public boolean needsHelp;
    public Availability availability;
    public Frequency frequency;
    public List<Activities> activitiesToGive;
    public List<Activities> activitiesToReceive;
    public List<ReviewOutputDto> givenReviews;
    public List<ReviewOutputDto> receivedReviews;
}