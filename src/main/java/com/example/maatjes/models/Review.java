package com.example.maatjes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue
    private Long id;
    private double rating;
    private String description;
    private boolean verified;
    private String feedbackAdmin;
    private LocalDate date;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "writtenBy_id")
    private Account writtenBy;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "writtenFor_id")
    private Account writtenFor;
}




