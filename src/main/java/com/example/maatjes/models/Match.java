package com.example.maatjes.models;

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
    private boolean accepted;
    @Enumerated(EnumType.STRING)
    private ContactPerson contactPerson;

    private LocalDate startMatch;
    private LocalDate endMatch;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @ManyToOne
    @JoinColumn(name = "helpGiver_id")
    private Account helpGiver;

    @ManyToOne
    @JoinColumn(name = "helpReceiver_id")
    private Account helpReceiver;

}
