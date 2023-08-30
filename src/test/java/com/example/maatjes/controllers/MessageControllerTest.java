package com.example.maatjes.controllers;

import com.example.maatjes.dtos.inputDtos.MessageInputDto;
import com.example.maatjes.enums.Activities;
import com.example.maatjes.models.*;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.MessageRepository;
import com.example.maatjes.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    @MockBean
    private Authentication authentication;

    Match match;;
    Account helpGiver;
    Account helpReceiver;
    MessageInputDto messageInputDto;
    Message message1;
    Message message2;

    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testUser");

        Long matchId = 1L;
        String username = "testUser";

        User giverUser = new User();
        giverUser.setUsername(username);
        giverUser.setPassword("$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee");
        giverUser.setEnabled(true);
        giverUser.setApikey(null);
        giverUser.setEmail("lisa@user.nl");

        User receiverUser = new User();
        receiverUser.setUsername("receiverUser");
        receiverUser.setPassword("$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee");
        receiverUser.setEnabled(true);
        receiverUser.setApikey(null);
        receiverUser.setEmail("vera@user.nl");

        helpGiver = new Account();
        helpGiver.setAccountId(1001L);

        List<Activities> activitiesToGive = new ArrayList<>();
        activitiesToGive.add(Activities.BREIEN);
        List<Activities> activitiesToReceive = new ArrayList<>();
        helpGiver.setActivitiesToGive(activitiesToGive);
        helpGiver.setActivitiesToReceive(activitiesToReceive);

        List<Review> givenReviews = new ArrayList<>();
        helpGiver.setGivenReviews(givenReviews);
        List<Review> receivedReviews = new ArrayList<>();
        helpGiver.setReceivedReviews(receivedReviews);

        helpGiver.setUser(giverUser);

        helpReceiver = new Account();
        helpReceiver.setAccountId(1002L);

        List<Activities> activitiesToGiveForReceiver = new ArrayList<>();
        List<Activities> activitiesToReceiveForReceiver = new ArrayList<>();
        helpReceiver.setActivitiesToGive(activitiesToGiveForReceiver);
        activitiesToReceive.add(Activities.BREIEN);
        helpReceiver.setActivitiesToReceive(activitiesToReceiveForReceiver);

        helpReceiver.setUser(receiverUser);

        match = new Match();
        match.setMatchId(matchId);

        List<Activities> activities = new ArrayList<>();
        activities.add(Activities.BREIEN);
        match.setActivities(activities);

        List<Message> messages = new ArrayList<>();
        match.setMessages(messages);

        messageInputDto = new MessageInputDto();
        messageInputDto.setContent("Hello, this is a test message.");

        message1 = new Message();
        message1.setMatch(match);
        message1.setWrittenByName(username);
        message1.setCreatedAt(LocalTime.of(14, 30, 0));
        message1.setContent("Hoi!!");
        messages.add(message1);

        message2 = new Message();
        message2.setMatch(match);
        message2.setWrittenByName("receiverUser");
        message2.setCreatedAt(LocalTime.of(14, 30, 0));
        message2.setContent("Doei!!");
        messages.add(message2);

        userRepository.save(giverUser);
        userRepository.save(receiverUser);

        giverUser.setAccount(helpGiver);
        receiverUser.setAccount(helpReceiver);

        helpGiver.setAccountId(accountRepository.save(helpGiver).getAccountId());
        helpReceiver.setAccountId(accountRepository.save(helpReceiver).getAccountId());

        match.setHelpGiver(helpGiver);
        match.setHelpReceiver(helpReceiver);
        matchRepository.save(match);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        messageRepository.deleteAll();
    }

    @Test
    void writeMessage() throws Exception {
        when(authentication.getName()).thenReturn("testUser");

        mockMvc.perform(post("/messages/{match}", match.getMatchId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(messageInputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.content").value(messageInputDto.getContent()))
                .andExpect(jsonPath("$.writtenByName").value("testUser"));
    }

    @Test
    void getAllMessagesWithMatchId() throws Exception {

        when(authentication.getName()).thenReturn("testUser");

        mockMvc.perform(get("/messages/{match}",match.getMatchId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(notNullValue()))
                .andExpect(jsonPath("$[0].content").value(message1.getContent()))
                .andExpect(jsonPath("$[0].writtenByName").value(message1.getWrittenByName()))
                .andExpect(jsonPath("$[1].id").value(notNullValue()))
                .andExpect(jsonPath("$[1].content").value(message2.getContent()))
                .andExpect(jsonPath("$[1].writtenByName").value(message2.getWrittenByName()));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}