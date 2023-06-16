package com.example.maatjes.models;

import com.example.maatjes.enums.ActivitiesToGive;
import com.example.maatjes.enums.ActivitiesToReceive;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.Frequency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.Collection;
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
    @Enumerated(EnumType.STRING)
    private ActivitiesToGive activitiesToGive;
    @Enumerated(EnumType.STRING)
    private ActivitiesToReceive activitiesToReceive;
//    private ArrayList<Match> matches; <-- dit wordt een relatie
    @Enumerated(EnumType.STRING)
    private Availability availability;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @OneToMany(mappedBy = "account")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    Collection<AccountMatch> accountMatches;

//    @ManyToMany
//    @JoinTable(name = "account_match",
//            joinColumns = @JoinColumn(name = "account_id"),
//            inverseJoinColumns = @JoinColumn(name = "match_id"))
//    private List<Match> matches = new ArrayList<>();
//
//    public void addMatch(Match match) {
//        matches.add(match);
//        match.getAccounts().add(this);
//    }
//
//    public void removeMatch(Match match) {
//        matches.remove(match);
//        match.getAccounts().remove(this);
//    }
}
