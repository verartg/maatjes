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
//    private Account account; <-- dit wordt een relatie
//    private Account createdFor; <-- dit wordt een relatie, maar moet dit onderscheid wel?
    private boolean accepted;

//    @ManyToOne
//    @JoinColumn(name = "calendar_id")
//    private Calendar myCalendar;

//    public Calendar getMyCalendar() {
//        return myCalendar;
//    }
//
//    public void setMyCalendar(Calendar myCalendar) {
//        this.myCalendar = myCalendar;
//    }
}
