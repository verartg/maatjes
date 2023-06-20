package com.example.maatjes.controllers;

import com.example.maatjes.dtos.MatchDto;
import com.example.maatjes.dtos.MatchInputDto;
import com.example.maatjes.services.MatchService;
import com.example.maatjes.util.FieldErrorHandling;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final FieldErrorHandling fieldErrorHandling;

    public MatchController(MatchService matchService, FieldErrorHandling fieldErrorHandling) {this.matchService = matchService;
        this.fieldErrorHandling = fieldErrorHandling;
    }

    @GetMapping
    public ResponseEntity<List<MatchDto>> getMatches(){
        return new ResponseEntity<>(matchService.getMatches(), HttpStatus.OK);
    }

    @GetMapping("/match/{id}")
    public ResponseEntity<MatchDto> getMatch(@PathVariable("id") Long id) {
        return new ResponseEntity<>(matchService.getMatch(id), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<MatchDto>> getMatchesByAccountId(@PathVariable("accountId") Long accountId) {
        return new ResponseEntity<>(matchService.getMatchesByAccountId(accountId), HttpStatus.OK);
    }

    @PostMapping("/{helpGiverId}/{helpReceiverId}")
    public ResponseEntity<Object> createMatch(@PathVariable("helpGiverId") Long helpGiverId, @PathVariable("helpReceiverId") Long helpReceiverId, @Valid @RequestBody MatchInputDto matchInputDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(matchService.createMatch(helpGiverId, helpReceiverId, matchInputDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeMatch(@PathVariable Long id) {
        matchService.removeMatch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMatch(@PathVariable Long id, @Valid @RequestBody MatchInputDto match) {
        return new ResponseEntity<>(matchService.updateMatch(id, match), HttpStatus.ACCEPTED);
    }
}
