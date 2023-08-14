package com.example.maatjes.services;

import com.example.maatjes.dtos.inputDtos.AppointmentInputDto;
import com.example.maatjes.dtos.outputDtos.AppointmentOutputDto;
import com.example.maatjes.enums.Activities;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.exceptions.BadRequestException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.exceptions.IllegalArgumentException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Appointment;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.AppointmentRepository;
import com.example.maatjes.repositories.MatchRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceUnitTest {

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    MatchRepository matchRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AppointmentService appointmentService;

    Match match1;
    AppointmentInputDto appointmentInputDto;
    AppointmentInputDto appointmentInputDto2;
    Appointment appointment1;
    Appointment appointment2;
    String username;
    Account helpGiver;

    @BeforeEach
    void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        username = "testUser";
        Long matchId = 1L;
        Long accountId = 1L;

        appointmentInputDto = new AppointmentInputDto();
        appointmentInputDto.setMatchId(matchId);
        appointmentInputDto.setId(1001L);
        appointmentInputDto.setDate(LocalDate.of(2123, 10, 15));
        appointmentInputDto.setStartTime(LocalTime.of(14, 30));
        appointmentInputDto.setEndTime(LocalTime.of(16, 0));
        appointmentInputDto.setDescription("Example appointment description in the future");

        appointmentInputDto2 = new AppointmentInputDto();
        appointmentInputDto2.setMatchId(matchId);
        appointmentInputDto2.setId(1002L);
        appointmentInputDto2.setDate(LocalDate.of(2023, 11, 15));
        appointmentInputDto2.setStartTime(LocalTime.of(14, 30));
        appointmentInputDto2.setEndTime(LocalTime.of(16, 0));
        appointmentInputDto2.setDescription("Example 2 appointment");

        appointment1 = new Appointment();
        appointment1.setId(1050L);
        appointment1.setMatch(match1);
        appointment1.setDate(LocalDate.of(2123, 8, 16));
        appointment1.setStartTime(LocalTime.of(14, 30));
        appointment1.setEndTime(LocalTime.of(16, 0));
        appointment1.setCreatedForName("lisa");
        appointment1.setCreatedByName(username);
        appointment1.setDescription("Example appointment 1");

        appointment2 = new Appointment();
        appointment2.setId(1051L);
        appointment2.setMatch(match1);
        appointment2.setDate(LocalDate.of(2023, 6, 14));
        appointment2.setStartTime(LocalTime.of(10, 0));
        appointment2.setEndTime(LocalTime.of(11, 30));
        appointment2.setCreatedForName("lisa");
        appointment2.setCreatedByName(username);
        appointment2.setDescription("Example appointment 2");

        helpGiver = new Account();
        Account helpReceiver = new Account();
        Account helpGiver2 = new Account();

        match1 = new Match();
        match1.setMatchId(matchId);
        match1.setGiverAccepted(true);
        match1.setReceiverAccepted(true);
        match1.setHelpGiver(helpGiver);
        match1.setHelpReceiver(helpReceiver);
        match1.setActivities(Arrays.asList(Activities.KLUSSEN));
        match1.setAppointments(new ArrayList<>());
        match1.setMessages(new ArrayList<>());
        match1.setMatchReviews(new ArrayList<>());
        match1.getAppointments().add(appointment1);
        match1.getAppointments().add(appointment2);

        User giverUser = new User();
        giverUser.setUsername("testUser");
        User receiverUser = new User();
        receiverUser.setUsername("lisa");
        User giverUser2 = new User();
        giverUser2.setUsername("vera");

        helpGiver.setUser(giverUser);
        helpGiver.setAccountId(accountId);
        helpReceiver.setUser(receiverUser);
        helpReceiver.setAccountId(11L);
        helpGiver2.setUser(giverUser2);
        helpGiver2.setAccountId(12L);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should create appointment")
    void createAppointment()  {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match1));
        when(appointmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentOutputDto result = appointmentService.createAppointment(appointmentInputDto);

        assertNotNull(result);
        assertEquals(result.createdByName, "testUser");
        assertEquals(result.createdForName, "lisa");
        assertEquals(result.date, LocalDate.of(2123, 10, 15));
        assertEquals(result.startTime, LocalTime.of(14, 30));
        assertEquals(result.endTime, LocalTime.of(16, 0));
        assertEquals(result.description, "Example appointment description in the future");
    }


    @Test
    @DisplayName("Should throw RecordNotFoundException when match/account/appointment is not found")
    void recordNotFoundException() {
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        when(appointmentRepository.findById(1050L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> appointmentService.createAppointment(appointmentInputDto));
        assertThrows(RecordNotFoundException.class, () -> appointmentService.getAppointmentsByMatchId(1L));
        assertThrows(RecordNotFoundException.class, () -> appointmentService.getAppointmentsByAccountId(1L));
        assertThrows(RecordNotFoundException.class, () -> appointmentService.getAppointment(1050L));
        assertThrows(RecordNotFoundException.class, () -> appointmentService.removeAppointment(1050L));
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user is not associated with the match/account/appointment")
    void accessDeniedException() {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match1));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(helpGiver));
        when(appointmentRepository.findById(1050L)).thenReturn(Optional.of(appointment1));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        when(authentication.getName()).thenReturn("anotherUser");

        assertThrows(AccessDeniedException.class, () -> appointmentService.createAppointment(appointmentInputDto));
        assertThrows(AccessDeniedException.class, () -> appointmentService.getAppointmentsByMatchId(1L));
        assertThrows(AccessDeniedException.class, () -> appointmentService.getAppointmentsByAccountId(1L));
        assertThrows(AccessDeniedException.class, () -> appointmentService.getAppointment(1050L));
        assertThrows(AccessDeniedException.class, () -> appointmentService.updateAppointment(1050L, appointmentInputDto2));
        assertThrows(AccessDeniedException.class, () -> appointmentService.removeAppointment(1050L));
    }

    @Test
    @DisplayName("Should throw BadRequestException when match is not accepted")
    void badRequestException() {
        match1.setGiverAccepted(false);
        match1.setReceiverAccepted(false);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match1));

        assertThrows(BadRequestException.class, () -> appointmentService.createAppointment(appointmentInputDto));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid appointment time")
    void invalidAppointmentTime_ThrowsIllegalArgumentException() throws RecordNotFoundException, BadRequestException, AccessDeniedException {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match1));
        appointmentInputDto.setEndTime(LocalTime.of(12, 0));

        assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(appointmentInputDto));
    }

    @Test
    @DisplayName("Should return all appointments associated with match")
    void getAppointmentsByMatchId() {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match1));

        List<AppointmentOutputDto> result = appointmentService.getAppointmentsByMatchId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(appointment1.getDate(), result.get(0).getDate());
        assertEquals(appointment1.getStartTime(), result.get(0).getStartTime());
        assertEquals(appointment1.getEndTime(), result.get(0).getEndTime());
        assertEquals(appointment1.getDescription(), result.get(0).getDescription());
        assertEquals(match1.getHelpReceiver().getUser().getUsername(), result.get(0).getCreatedForName());
        assertEquals(match1.getHelpGiver().getUser().getUsername(), result.get(0).getCreatedByName());
        verify(matchRepository, times(1)).findById(1L);
        assertFalse(result.stream().anyMatch(appointmentOutputDto -> appointmentOutputDto.getId().equals(appointment2.getId())));
    }

    @Test
    @DisplayName("Should return all appointments associated with account")
    void getAppointmentsByAccountId() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(helpGiver));

        List<Match> helpGiversMatches = new ArrayList<>();
        helpGiversMatches.add(match1);
        List<Match> helpReceiversMatches = new ArrayList<>();
        helpGiver.setHelpGivers(helpGiversMatches);
        helpGiver.setHelpReceivers(helpReceiversMatches);

        List<AppointmentOutputDto> result = appointmentService.getAppointmentsByAccountId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(appointment1.getDate(), result.get(0).getDate());
        assertEquals(appointment1.getStartTime(), result.get(0).getStartTime());
        assertEquals(appointment1.getEndTime(), result.get(0).getEndTime());
        assertEquals(appointment1.getDescription(), result.get(0).getDescription());
        assertEquals(match1.getHelpReceiver().getUser().getUsername(), result.get(0).getCreatedForName());
        assertEquals(match1.getHelpGiver().getUser().getUsername(), result.get(0).getCreatedByName());

        Mockito.verify(accountRepository, Mockito.times(1)).findById(1L);
    }


    @Test
    @DisplayName("Should return appointment with appointmentId")
    void getAppointment() {
        when(appointmentRepository.findById(1050L)).thenReturn(Optional.of(appointment1));

        AppointmentOutputDto result = appointmentService.getAppointment(1050L);

        assertNotNull(result);
        assertEquals(appointment1.getDate(), result.getDate());
        assertEquals(appointment1.getStartTime(), result.getStartTime());
        assertEquals(appointment1.getEndTime(), result.getEndTime());
        assertEquals(appointment1.getDescription(), result.getDescription());
        assertEquals(match1.getHelpReceiver().getUser().getUsername(), result.getCreatedForName());
        assertEquals(match1.getHelpGiver().getUser().getUsername(), result.getCreatedByName());
        Mockito.verify(appointmentRepository, Mockito.times(1)).findById(1050L);
    }

    @Test
    @DisplayName("should update the appointment")
    void updateAppointment() {
        when(appointmentRepository.findById(1050L)).thenReturn(Optional.of(appointment1));
        when(appointmentRepository.save(any())).thenReturn(appointment1);

        AppointmentOutputDto result = appointmentService.updateAppointment(1050L, appointmentInputDto2);

        assertNotNull(result);
        assertEquals(1050L, result.getId());
        assertEquals(appointmentInputDto2.getDate(), result.getDate());
        assertEquals(appointmentInputDto2.getStartTime(), result.getStartTime());
        assertEquals(appointmentInputDto2.getEndTime(), result.getEndTime());
        assertEquals(appointmentInputDto2.getDescription(), result.getDescription());
    }

    @Test
    @DisplayName("Should remove appointment")
    void removeAppointment() {
        when(appointmentRepository.findById(1050L)).thenReturn(Optional.of(appointment1));

        String result = appointmentService.removeAppointment(1050L);

        Assertions.assertEquals("Afspraak succesvol verwijderd", result);
        verify(appointmentRepository, times(1)).deleteById(1050L);
    }
}