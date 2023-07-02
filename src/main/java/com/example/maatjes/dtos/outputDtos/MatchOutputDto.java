package com.example.maatjes.dtos.outputDtos;

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
public class MatchOutputDto {
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
    public List<Activities> activities;
    public List<Review> matchReviews;
    //todo onderstaanden nog implementern.
    //todo additionele vraag, moeten deze hier wel staan? Aangezien ik toch geen lijsten kan weergeven in PgAdmin? ik kan hier toch alleen maar bij met requests?
    public List<Appointment> appointments;
    public List<Message> messages;

}
