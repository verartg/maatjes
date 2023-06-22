package com.example.maatjes.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment {

    @Id
    @GeneratedValue
    private Long id;
    private Date date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
// accepted boolean?

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    private String createdByName;
    private String createdForName;

//    @ManyToOne
//    @JoinColumn(name = "created_by_id")
//    private Account createdBy;
//
//    @ManyToOne
//    @JoinColumn(name = "created_for_id")
//    private Account createdFor;
}
