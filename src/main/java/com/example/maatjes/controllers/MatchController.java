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

    @PostMapping
    public ResponseEntity<Object> proposeMatch(@Valid @RequestBody MatchInputDto matchInputDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(matchService.proposeMatch(matchInputDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MatchOutputDto>> getMatches(){
        return new ResponseEntity<>(matchService.getMatches(), HttpStatus.OK);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchOutputDto> getMatch(@PathVariable Long matchId) {
        return new ResponseEntity<>(matchService.getMatch(matchId), HttpStatus.OK);
    }

    @GetMapping("/{username}/accepted")
    public ResponseEntity<List<MatchOutputDto>> getAcceptedMatchesByUsername(@PathVariable String username) {
        return new ResponseEntity<>(matchService.getAcceptedMatchesByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/{username}/proposed")
    public ResponseEntity<List<MatchOutputDto>> getProposedMatchesByUsername(@PathVariable String username) {
        return new ResponseEntity<>(matchService.getProposedMatchesByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/{matchId}/accept")
    public ResponseEntity<Object> acceptMatch(@PathVariable Long matchId) {
        return new ResponseEntity<>(matchService.acceptMatch(matchId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<Object> updateMatch(@PathVariable Long matchId, @Valid @RequestBody MatchInputDto matchInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(matchService.updateMatch(matchId, matchInputDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<String> removeMatch(@PathVariable Long matchId) {
        String message = matchService.removeMatch(matchId);
        return ResponseEntity.ok().body(message);
    }
}
