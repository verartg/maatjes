package com.example.maatjes.models;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Account")
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
    @Lob
    private byte[] document;
    private boolean givesHelp;
    private boolean needsHelp;
    @ElementCollection
    @CollectionTable(name = "activities_to_give")
    @Enumerated(EnumType.STRING)
    private List<Activities> activitiesToGive;
    @ElementCollection
    @CollectionTable(name = "activities_to_receive")
    @Enumerated(EnumType.STRING)
    private List<Activities> activitiesToReceive;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @OneToMany(mappedBy = "helpGiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> helpReceivers;

    @OneToMany(mappedBy = "helpReceiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> helpGivers;

}
