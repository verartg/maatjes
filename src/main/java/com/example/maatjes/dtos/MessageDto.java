package com.example.maatjes.dtos;

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
public class MessageDto {
    public Long id;
    //todo hieronder naam van maken.
    public String content;
    public LocalTime createdAt;
    public LocalDate createdAtDate;
    public String writtenByName;
//hieronder is misschien meer frontend?
    public String getFormattedMessage() {
        return "At " + createdAtDate + " on " + createdAt + ", " + writtenByName + " sent you: " + content;
    }
    }
