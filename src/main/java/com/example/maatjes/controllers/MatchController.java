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

import java.util.Collection;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final FieldErrorHandling fieldErrorHandling;

    public MatchController(MatchService matchService, FieldErrorHandling fieldErrorHandling) {this.matchService = matchService;
        this.fieldErrorHandling = fieldErrorHandling;
    }

    // Deze methode haalt alle accounts op die aan een bepaalde match gekoppeld zijn.
    // Deze methode maakt gebruikt van de accountMatchService.
//    @GetMapping("/accounts/{matchId}")
//    public ResponseEntity<Collection<AccountDto>> getAccountsByMatchId(@PathVariable("matchId") Long matchId){
//        return ResponseEntity.ok(accountMatchService.getAccountsByMatchId(matchId));
//    }

//    @GetMapping("/{accountId}")
//    public ResponseEntity<Collection<MatchDto>> getMatchesByAccountId(@PathVariable("accountId") Long accountId) {
//        return ResponseEntity.ok(matchService.getMatchesByAccountId(accountId));
//    }

    @PostMapping("/{helpGiverId}/{helpReceiverId}")
    public ResponseEntity<Object> createMatch(@PathVariable("helpGiverId") Long helpGiverId, @PathVariable("helpReceiverId") Long helpReceiverId, @Valid @RequestBody MatchInputDto matchInputDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(matchService.createMatch(helpGiverId, helpReceiverId, matchInputDto), HttpStatus.ACCEPTED);
    }


            //@PostMapping("/{accountId}/{matchId}")
    //    public ResponseEntity<AccountMatchKey> addAccountMatch(@PathVariable("accountId") Long accountId, @PathVariable("matchId") Long matchId) {
    //        AccountMatchKey key = accountMatchService.addAccountMatch(accountId, matchId);
    //        return ResponseEntity.created(null).body(key);
    //    }
//    @PutMapping("/televisions/{id}/{ciModuleId}")
//    public ResponseEntity<Object> assignCIModuleToTelevision(@PathVariable("id") Long id, @PathVariable("ciModuleId") Long ciModuleId) {
//        matchService.assignCIModuleToTelevision(id, ciModuleId);
//        return ResponseEntity.noContent().build();
//    }
}
