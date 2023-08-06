package com.example.maatjes.services;

import com.example.maatjes.enums.Activities;
import com.example.maatjes.models.Match;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @InjectMocks
    MatchService matchService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should get shared activities")
    void getSharedActivities() {
        List<Activities> giverActivities = Arrays.asList(Activities.WANDELEN, Activities.NEDERLANDS_LEREN, Activities.VOORLEZEN, Activities.KLUSSEN);
        List<Activities> receiverActivities = Arrays.asList(Activities.VOORLEZEN, Activities.TUINIEREN, Activities.SCHOONMAKEN);

        List<Activities> sharedActivities = matchService.getSharedActivities(giverActivities, receiverActivities);

        assertNotNull(sharedActivities);
        assertEquals(1, sharedActivities.size());
        assertTrue(sharedActivities.contains(Activities.VOORLEZEN));
    }
}