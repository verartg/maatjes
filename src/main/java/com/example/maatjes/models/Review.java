package com.example.maatjes.models;

import jakarta.persistence.Entity;
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
public class Review {
    @Id
    @GeneratedValue
    private Long id;
    private float rating;
    private String description;
//    private account writtenBy; <-- dit wordt een relatie
//    private account writtenAbout; <-- dit wordt een relatie
    private boolean verified;
}
