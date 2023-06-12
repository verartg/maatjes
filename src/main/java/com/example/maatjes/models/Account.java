package com.example.maatjes.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;
    private char sex;
    private String phoneNumber;
    private String emailAddress;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
//    private image profilePicture;
    private String bio;
// private pdf identification;
    private boolean givesHelp;
    private boolean needsHelp;
    @Enumerated
    private ActivitiesToGive activitiesToGive;
    @Enumerated
    private ActivitiesToReceive activitiesToReceive;
//    private ArrayList<Match> matches; <-- dit wordt een relatie
    @Enumerated
    private Availability availability;
    @Enumerated
    private Frequency frequency;
}
