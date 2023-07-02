package com.example.maatjes.services;

import com.example.maatjes.dtos.MessageOutputDto;
import com.example.maatjes.dtos.MessageInputDto;
import com.example.maatjes.exceptions.AccountNotAssociatedException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.Message;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.MessageRepository;
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
    private final AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, MatchRepository matchRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
    }

    public MessageOutputDto writeMessage(Long matchId, Long accountId, MessageInputDto messageInputDto) throws RecordNotFoundException, AccountNotAssociatedException {
//todo als ik de principle heb, dan hoef ik alleen maar de matchid mee te geven om bij de writer en receiver te komen van de review.
        //ik ga op zoek naar de match
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
        //ik ga op zoek naar de schrijver van de message.
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));
        //als de schrijver message niet overeenkomt met gever of receiver in de match, mag het niet.
        if (!match.getHelpReceiver().equals(account) && !match.getHelpGiver().equals(account)) {
            throw new AccountNotAssociatedException("Je kunt alleen naar iemand van je matches een bericht sturen.");
        }

        Message message = transferInputDtoToMessage(messageInputDto);
        message.setCreatedAt(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        message.setCreatedAtDate(LocalDate.now());
        message.setWrittenByName(account.getName());
        message.setMatch(match);
        message = messageRepository.save(message);
        match.getMessages().add(message);
        matchRepository.save(match);
        return transferMessageOutputToDto(message);
    }

    public List<MessageOutputDto> getAllMessagesWithMatchId(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
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