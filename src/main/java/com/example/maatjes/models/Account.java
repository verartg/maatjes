package com.example.maatjes.models;

import com.example.maatjes.enums.ActivitiesToGive;
import com.example.maatjes.enums.ActivitiesToReceive;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private ActivitiesToGive activitiesToGive;
    @Enumerated(EnumType.STRING)
    private ActivitiesToReceive activitiesToReceive;
//    private ArrayList<Match> matches; <-- dit wordt een relatie
    @Enumerated(EnumType.STRING)
    private Availability availability;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
}
