package com.example.maatjes.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest
class AccountServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createAccount() {
    }

    @Test
    void getAccountsByFilters() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void getIdentificationDocument() {
    }

    @Test
    void updateAccount() {
    }

    @Test
    void uploadIdentificationDocument() {
    }

    @Test
    void removeIdentificationDocument() {
    }

    @Test
    void removeAccount() {
    }

    @Test
    void transferAccountToOutputDto() {
    }

    @Test
    void transferInputDtoToAccount() {
    }
}