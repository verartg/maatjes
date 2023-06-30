package com.example.maatjes.controllers;

import com.example.maatjes.dtos.MessageDto;
import com.example.maatjes.dtos.MessageInputDto;
import com.example.maatjes.models.Message;
import com.example.maatjes.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

//    @PostMapping("/{matchId}/{accountId}")
//    public ResponseEntity<Object> writeMessage(@PathVariable("matchId") Long matchId, @PathVariable ("accountId") Long accountId, @RequestBody MessageInputDto messageInputDto) {
//        MessageDto messageDto = messageService.writeMessage(matchId, accountId, messageInputDto);
//        return new ResponseEntity<>(messageDto, HttpStatus.ACCEPTED);
//    }

    //eerste is meer frontend? tweede eronder meer backend.
    @PostMapping("/{matchId}/{accountId}")
    public ResponseEntity<String> writeMessage(@PathVariable("matchId") Long matchId, @PathVariable("accountId") Long accountId, @RequestBody MessageInputDto messageInputDto) {
        MessageDto messageDto = messageService.writeMessage(matchId, accountId, messageInputDto);
        String formattedMessage = messageDto.getFormattedMessage();
        return new ResponseEntity<>(formattedMessage, HttpStatus.ACCEPTED);
    }

//    public MessageDto writeMessage(@RequestBody MessageInputDto messageInputDto) {
//        Message message = messageService.writeMessage(
//                messageInputDto.getAccountId(),
//                messageInputDto.getMatchId(),
//                messageInputDto.getContent()
//        );
//
//        return new MessageDto(message);
//    }

//    @GetMapping("/messages/{matchId}")
//    public List<MessageDto> getAllMessagesWithMatch(@PathVariable Long matchId) {
//        List<Message> messages = messageService.getAllMessagesWithMatch(matchId);
//        return messages.stream().map(MessageDto::new).collect(Collectors.toList());
//    }
//
//    @GetMapping("/messages/{matchId}/last-twenty")
//    public List<MessageDto> getTheLastTwentyMessagesWithMatch(@PathVariable Long matchId) {
//        List<Message> messages = messageService.getTheLastTwentyMessagesWithMatch(matchId);
//        return messages.stream().map(MessageDto::new).collect(Collectors.toList());
//    }
//
//    @DeleteMapping("/messages/{messageId}")
//    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
//        messageService.deleteMessage(messageId);
//        return ResponseEntity.noContent().build();
//    }
}
