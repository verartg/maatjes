package com.example.maatjes.services;

import com.example.maatjes.dtos.inputDtos.MessageInputDto;
import com.example.maatjes.dtos.outputDtos.MessageOutputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.models.*;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.MessageRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceUnitTest {

    @Mock
    MatchRepository matchRepository;

    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageService messageService;

    Match match, match2;
    User giverUser, receiverUser, receiverUser2;
    Account helpGiver, helpReceiver, helpReceiver2;
    MessageInputDto messageInputDto;
    Message message, message2;

    @BeforeEach
    void setUp() {
        Long matchId = 1L;
        Long matchId2 = 2L;
        String username = "testUser";

        match = new Match();
        match.setMatchId(matchId);

        giverUser = new User();
        giverUser.setUsername(username);
        receiverUser = new User();
        receiverUser.setUsername("receiverUser");

        helpGiver = new Account();
        helpGiver.setUser(giverUser);
        helpReceiver = new Account();
        helpReceiver.setUser(receiverUser);

        match.setHelpGiver(helpGiver);
        match.setHelpReceiver(helpReceiver);

        List<Message> messages = new ArrayList<>();
        match.setMessages(messages);

        messageInputDto = new MessageInputDto();
        messageInputDto.setContent("Test message content");

        message = new Message();
        message.setId(12L);
        message.setMatch(match);
        message.setCreatedAtDate(LocalDate.of(2023, 8, 15));
        message.setWrittenByName("testUser");
        message.setCreatedAt(LocalTime.of(14, 30));
        message.setContent("Hoi!");
        messages.add(message);

        match2 = new Match();
        match2.setMatchId(matchId2);

        receiverUser2 = new User();
        receiverUser2.setUsername("lisa");
        helpReceiver2 = new Account();
        helpReceiver2.setUser(receiverUser2);

        match2.setHelpGiver(helpReceiver2);
        match2.setHelpReceiver(helpReceiver);

        List<Message> messages2 = new ArrayList<>();
        match2.setMessages(messages2);

        message2 = new Message();
        message2.setId(13L);
        message2.setMatch(match2);
        message2.setCreatedAtDate(LocalDate.of(2023, 6, 15));
        message2.setWrittenByName("receiverUser");
        message2.setCreatedAt(LocalTime.of(14, 30));
        message2.setContent("Doei!");
        messages2.add(message2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should write message")
    void writeMessage() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        MessageOutputDto result = messageService.writeMessage(1L, messageInputDto);
        assertAll("Write message",
                () -> assertNotNull(result),
                () -> assertEquals("testUser", result.getWrittenByName()),
                () -> assertEquals(messageInputDto.getContent(), result.getContent()),
                () -> assertEquals(2, match.getMessages().size())
        );
    }

    @Test @DisplayName("Should throw AccessDeniedException when user is not associated with the match")
    void accessDeniedException() {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn("testUser");
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(auth.getName()).thenReturn("anotherUser");

        assertAll("Access denied exception",
                () -> assertThrows(AccessDeniedException.class, () -> messageService.writeMessage(1L, messageInputDto)),
                () -> assertThrows(AccessDeniedException.class, () -> messageService.getAllMessagesWithMatchId(1L))
        );
    }


    @Test @DisplayName("Should return all messages associated with match")
    void getAllMessagesWithMatchId() {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn("testUser");
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        List<MessageOutputDto> result = messageService.getAllMessagesWithMatchId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        MessageOutputDto outputDto = result.get(0);

        assertAll("Get all messages",
                () -> assertEquals(message.getId(), outputDto.getId()),
                () -> assertEquals(message.getContent(), outputDto.getContent()),
                () -> assertEquals(message.getCreatedAt(), outputDto.getCreatedAt()),
                () -> assertEquals(message.getCreatedAtDate(), outputDto.getCreatedAtDate()),
                () -> assertEquals(message.getWrittenByName(), outputDto.getWrittenByName())
        );

        verify(matchRepository, times(1)).findById(1L);
        assertFalse(result.stream().anyMatch(dto -> dto.getId().equals(message2.getId())));
    }

    @Test @DisplayName("Should delete messages older than one month")
    void deleteOldMessages() {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneMonthAgo = currentDate.minusMonths(1);

        message.setCreatedAtDate(currentDate);
        message2.setCreatedAtDate(oneMonthAgo);

        when(messageRepository.findByCreatedAtDateBefore(eq(oneMonthAgo))).thenReturn(Collections.singletonList(message2));

        messageService.deleteOldMessages();

        verify(messageRepository).deleteAll(Collections.singletonList(message2));
        verify(messageRepository, never()).delete(eq(message));
    }
}