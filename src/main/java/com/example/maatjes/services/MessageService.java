package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.MessageOutputDto;
import com.example.maatjes.dtos.inputDtos.MessageInputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.Message;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.MessageRepository;
import com.example.maatjes.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MatchRepository matchRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, MatchRepository matchRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public MessageOutputDto writeMessage(Long matchId, MessageInputDto messageInputDto) throws RecordNotFoundException, AccessDeniedException {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!match.getHelpGiver().getUser().getUsername().equals(username) && !match.getHelpReceiver().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze match");
        }

        Message message = transferInputDtoToMessage(messageInputDto);
        message.setCreatedAt(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        message.setCreatedAtDate(LocalDate.now());
        message.setWrittenByName(username);
        message.setMatch(match);

        match.getMessages().add(message);
        messageRepository.save(message);

        return transferMessageOutputToDto(message);
    }

    public List<MessageOutputDto> getAllMessagesWithMatchId(Long matchId) throws RecordNotFoundException {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!match.getHelpGiver().getUser().getUsername().equals(username) && !match.getHelpReceiver().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze berichten");
        }
        List<Message> messages = match.getMessages();
        List<MessageOutputDto> messageOutputDtos = new ArrayList<>();
        for (Message message : messages) {
                messageOutputDtos.add(transferMessageOutputToDto(message));
        }
        messageOutputDtos.sort(Comparator.comparing(MessageOutputDto::getCreatedAt));
        return messageOutputDtos;
    }

    public void deleteOldMessages() {
        LocalDate currentDate = LocalDate.now().minusMonths(1);
        List<Message> messagesToDelete = messageRepository.findByCreatedAtDateBefore(currentDate);
        messageRepository.deleteAll(messagesToDelete);
    }

    public MessageOutputDto transferMessageOutputToDto(Message message) {
        MessageOutputDto messageOutputDto = new MessageOutputDto();
        messageOutputDto.id = message.getId();
        messageOutputDto.content = message.getContent();
        messageOutputDto.createdAt = message.getCreatedAt();
        messageOutputDto.writtenByName = message.getWrittenByName();
        messageOutputDto.createdAtDate = message.getCreatedAtDate();
        return messageOutputDto;
    }

    public Message transferInputDtoToMessage(MessageInputDto messageInputDto) {
        Message message = new Message();
        message.setContent(messageInputDto.getContent());
        return message;
    }
}