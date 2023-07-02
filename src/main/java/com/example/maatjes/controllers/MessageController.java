package com.example.maatjes.controllers;

import com.example.maatjes.dtos.outputDtos.MessageOutputDto;
import com.example.maatjes.dtos.inputDtos.MessageInputDto;
import com.example.maatjes.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/{matchId}/{accountId}")
    public ResponseEntity<Object> writeMessage(@PathVariable("matchId") Long matchId, @PathVariable ("accountId") Long accountId, @RequestBody MessageInputDto messageInputDto) {
        MessageOutputDto messageOutputDto = messageService.writeMessage(matchId, accountId, messageInputDto);
        return new ResponseEntity<>(messageOutputDto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MessageOutputDto>> getAllMessagesWithMatchId(@PathVariable Long matchId) {
        List<MessageOutputDto> messages = messageService.getAllMessagesWithMatchId(matchId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOldMessages() {
        messageService.deleteOldMessages();
        return ResponseEntity.ok("Berichten ouder dan een maand zijn verwijderd.");
    }
}