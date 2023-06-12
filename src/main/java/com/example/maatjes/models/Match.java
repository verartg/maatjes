package com.example.maatjes.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Match {
    @Id
    @GeneratedValue
    private Long id;
    private boolean accepted;
//    private User contactperson; <-- dit wordt geen relatie denk ik, maaaar hoe implementeer ik dit
    private LocalDate startMatch;
    private LocalDate endMatch;
//    @Enumerated
//    private Availability frequency; <--
    private List<String> activities;
}
