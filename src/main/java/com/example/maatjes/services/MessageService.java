package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.MessageOutputDto;
import com.example.maatjes.dtos.inputDtos.MessageInputDto;
import com.example.maatjes.exceptions.AccessDeniedException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.Message;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.MessageRepository;
import com.example.maatjes.util.SecurityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MatchRepository matchRepository;

    public MessageService(MessageRepository messageRepository, MatchRepository matchRepository) {
        this.messageRepository = messageRepository;
        this.matchRepository = matchRepository;
    }

    public MessageOutputDto writeMessage(Long matchId, MessageInputDto messageInputDto) throws RecordNotFoundException, AccessDeniedException {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        String helpGiverName = match.getHelpGiver().getUser().getUsername();
        String helpReceiverName = match.getHelpReceiver().getUser().getUsername();

        SecurityUtils.validateUsernames(helpGiverName, helpReceiverName, "matches");

        Message message = transferInputDtoToMessage(messageInputDto);
        message.setCreatedAt(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        message.setCreatedAtDate(LocalDate.now());
        message.setWrittenByName(SecurityContextHolder.getContext().getAuthentication().getName());
        message.setMatch(match);

        match.getMessages().add(message);
        messageRepository.save(message);

        return transferMessageToOutputDto(message);
    }

    public List<MessageOutputDto> getAllMessagesWithMatchId(Long matchId) throws RecordNotFoundException, AccessDeniedException {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        String helpGiverName = match.getHelpGiver().getUser().getUsername();
        String helpReceiverName = match.getHelpReceiver().getUser().getUsername();

        SecurityUtils.validateUsernames(helpGiverName, helpReceiverName, "matches");

        List<Message> messages = match.getMessages();
        List<MessageOutputDto> messageOutputDtos = new ArrayList<>();
        for (Message message : messages) {
                messageOutputDtos.add(transferMessageToOutputDto(message));
        }
        messageOutputDtos.sort(Comparator.comparing(MessageOutputDto::getCreatedAt));
        return messageOutputDtos;
    }

    public void deleteOldMessages() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Message> messagesToDelete = messageRepository.findByCreatedAtDateBefore(oneMonthAgo);
        messageRepository.deleteAll(messagesToDelete);
    }

    public MessageOutputDto transferMessageToOutputDto(Message message) {
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