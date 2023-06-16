package com.example.maatjes.controllers;

import com.example.maatjes.models.AccountMatchKey;
import com.example.maatjes.models.MatchRequest;
import com.example.maatjes.services.AccountMatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accountmatch")
public class AccountMatchController {
    private AccountMatchService accountMatchService;

    public AccountMatchController(AccountMatchService accountMatchService) {
        this.accountMatchService = accountMatchService;
    }

    @PostMapping("/{accountId}/{matchId}")
    public ResponseEntity<AccountMatchKey> addAccountMatch(@PathVariable("accountId") Long accountId, @PathVariable("matchId") Long matchId) {
        AccountMatchKey key = accountMatchService.addAccountMatch(accountId, matchId);
        return ResponseEntity.created(null).body(key);
    }
    //die hierboven of die hieronder?

    public ResponseEntity<AccountMatchKey> addAccountMatch(@RequestBody MatchRequest matchRequest) {
        AccountMatchKey key = accountMatchService.addAccountMatch(matchRequest.getAccountId1(), matchRequest.getAccountId2());
        return ResponseEntity.created(null).body(key);
    }
}