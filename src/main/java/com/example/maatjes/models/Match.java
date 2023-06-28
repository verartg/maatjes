package com.example.maatjes.models;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.ContactPerson;
import com.example.maatjes.enums.Frequency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Match")
public class Match {
    @Id
    @GeneratedValue
    private Long id;
    private boolean giverAccepted = false;
    private boolean receiverAccepted = false;
    @Enumerated(EnumType.STRING)
    private ContactPerson contactPerson;

    private LocalDate startMatch;
    private LocalDate endMatch;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    private List<Activities> activities;

    @ManyToOne
    @JoinColumn(name = "helpGiver_id")
    private Account helpGiver;

    @ManyToOne
    @JoinColumn(name = "helpReceiver_id")
    private Account helpReceiver;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> matchReviews;
}
