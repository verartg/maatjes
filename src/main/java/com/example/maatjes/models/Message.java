package com.example.maatjes.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    private String writtenByName;
    private String content;
    private LocalTime createdAt;
    private LocalDate createdAtDate;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
}
