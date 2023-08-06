package com.example.maatjes.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Mock
    Match match;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should get appointment description")
    void getDescription() {
        //arrange
        Appointment a = new Appointment(1L, LocalDate.of(2023, 7, 19), LocalTime.of(9, 0), LocalTime.of(10, 0), "Tuinieren", "lisa", "peter1991", match);
        //act
        String str = a.getDescription();
        //assert
        assertEquals(str, "Tuinieren");
    }
}