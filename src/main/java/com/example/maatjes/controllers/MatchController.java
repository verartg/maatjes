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

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<MatchDto>> getMatches(){
        return new ResponseEntity<>(matchService.getMatches(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getMatch(@PathVariable("id") Long id) {
        return new ResponseEntity<>(matchService.getMatch(id), HttpStatus.OK);
    }

    @GetMapping("/{accountId}/accepted")
    public ResponseEntity<List<MatchDto>> getAcceptedMatchesByAccountId(@PathVariable("accountId") Long accountId) {
        return new ResponseEntity<>(matchService.getAcceptedMatchesByAccountId(accountId), HttpStatus.OK);
    }

    @GetMapping("/{accountId}/proposed")
    public ResponseEntity<List<MatchDto>> getProposedMatchesByAccountId(@PathVariable("accountId") Long accountId) {
        return new ResponseEntity<>(matchService.getProposedMatchesByAccountId(accountId), HttpStatus.OK);
    }

    @PostMapping("/{helpGiverId}/{helpReceiverId}")
    public ResponseEntity<Object> proposeMatch(@PathVariable("helpGiverId") Long helpGiverId, @PathVariable("helpReceiverId") Long helpReceiverId, @Valid @RequestBody MatchInputDto matchInputDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(matchService.proposeMatch(helpGiverId, helpReceiverId, matchInputDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeMatch(@PathVariable Long id) {
        matchService.removeMatch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{matchId}/{accountId}")
    public ResponseEntity<Object> acceptMatch(@PathVariable("matchId") Long matchId, @PathVariable("accountId") Long accountId) {
        return new ResponseEntity<>(matchService.acceptMatch(matchId, accountId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMatch(@PathVariable Long id, @Valid @RequestBody MatchInputDto match, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(matchService.updateMatch(id, match), HttpStatus.ACCEPTED);
    }
}
