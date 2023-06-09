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

import java.util.ArrayList;
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
    @Column(name = "activity")
    @Enumerated(EnumType.STRING)
    private List<Activities> activitiesToGive = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "activities_to_receive")
    @Column(name = "activity")
    @Enumerated(EnumType.STRING)
    private List<Activities> activitiesToReceive = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Enumerated(EnumType.STRING)
    private Availability availability;

    @OneToMany(mappedBy = "helpGiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> helpReceivers;

    @OneToMany(mappedBy = "helpReceiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> helpGivers;

}
