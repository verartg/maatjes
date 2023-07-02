package com.example.maatjes.controllers;

import com.example.maatjes.dtos.outputDtos.MatchOutputDto;
import com.example.maatjes.dtos.inputDtos.MatchInputDto;
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

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/{helpGiverId}/{helpReceiverId}")
    public ResponseEntity<Object> proposeMatch(@PathVariable("helpGiverId") Long helpGiverId, @PathVariable("helpReceiverId") Long helpReceiverId, @Valid @RequestBody MatchInputDto matchInputDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(matchService.proposeMatch(helpGiverId, helpReceiverId, matchInputDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MatchOutputDto>> getMatches(){
        return new ResponseEntity<>(matchService.getMatches(), HttpStatus.OK);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchOutputDto> getMatch(@PathVariable("matchId") Long matchId) {
        return new ResponseEntity<>(matchService.getMatch(matchId), HttpStatus.OK);
    }

    @GetMapping("/{accountId}/accepted")
    public ResponseEntity<List<MatchOutputDto>> getAcceptedMatchesByAccountId(@PathVariable("accountId") Long accountId) {
        return new ResponseEntity<>(matchService.getAcceptedMatchesByAccountId(accountId), HttpStatus.OK);
    }

    @GetMapping("/{accountId}/proposed")
    public ResponseEntity<List<MatchOutputDto>> getProposedMatchesByAccountId(@PathVariable("accountId") Long accountId) {
        return new ResponseEntity<>(matchService.getProposedMatchesByAccountId(accountId), HttpStatus.OK);
    }

    @PutMapping("/{matchId}/{accountId}")
    public ResponseEntity<Object> acceptMatch(@PathVariable("matchId") Long matchId, @PathVariable("accountId") Long accountId) {
        return new ResponseEntity<>(matchService.acceptMatch(matchId, accountId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<Object> updateMatch(@PathVariable Long matchId, @Valid @RequestBody MatchInputDto matchInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(matchService.updateMatch(matchId, matchInputDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<Object> removeMatch(@PathVariable Long matchId) {
        matchService.removeMatch(matchId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
