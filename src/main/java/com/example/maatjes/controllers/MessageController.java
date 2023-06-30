package com.example.maatjes.controllers;

import com.example.maatjes.dtos.MessageDto;
import com.example.maatjes.dtos.MessageInputDto;
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
        MessageDto messageDto = messageService.writeMessage(matchId, accountId, messageInputDto);
        return new ResponseEntity<>(messageDto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MessageDto>> getAllMessagesWithMatchId(@PathVariable Long matchId) {
        List<MessageDto> messages = messageService.getAllMessagesWithMatchId(matchId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOldMessages() {
        messageService.deleteOldMessages();
        return ResponseEntity.ok("Berichten ouder dan een maand zijn verwijderd.");
    }
}
