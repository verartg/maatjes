package com.example.maatjes.dtos;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.Availability;
import com.example.maatjes.enums.ContactPerson;
import com.example.maatjes.enums.Frequency;
import com.example.maatjes.models.Appointment;
import com.example.maatjes.models.Message;
import com.example.maatjes.models.Review;
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
public class MatchDto {
    public Long id;
    public boolean giverAccepted = false;
    public boolean receiverAccepted = false;
    public ContactPerson contactPerson;
    public LocalDate startMatch;
    public LocalDate endMatch;
    public Availability availability;
    public Frequency frequency;
    public String helpReceiverName;
    public String helpGiverName;

    //todo onderstaande worden niet opgeslagen in de database
    public List<Activities> activities;
    public List<Review> matchReviews;
    //todo onderstaanden nog implementern.
    public List<Appointment> appointments;
    public List<Message> messages;

}
