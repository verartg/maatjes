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
public class MessageOutputDto {
    public Long id;
    public String content;
    public LocalTime createdAt;
    public LocalDate createdAtDate;
    public String writtenByName;
    }
