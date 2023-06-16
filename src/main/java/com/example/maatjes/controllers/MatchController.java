package com.example.maatjes.controllers;

import com.example.maatjes.dtos.AccountDto;
import com.example.maatjes.services.AccountMatchService;
import com.example.maatjes.services.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final AccountMatchService accountMatchService;

    public MatchController(MatchService matchService, AccountMatchService accountMatchService) {this.matchService = matchService;
        this.accountMatchService = accountMatchService;
    }

    // Deze methode haalt alle accounts op die aan een bepaalde match gekoppeld zijn.
    // Deze methode maakt gebruikt van de accountMatchService.
    @GetMapping("/accounts/{matchId}")
    public ResponseEntity<Collection<AccountDto>> getAccountsByMatchId(@PathVariable("matchId") Long matchId){
        return ResponseEntity.ok(accountMatchService.getAccountsByMatchId(matchId));
    }
//    @PutMapping("/televisions/{id}/{ciModuleId}")
//    public ResponseEntity<Object> assignCIModuleToTelevision(@PathVariable("id") Long id, @PathVariable("ciModuleId") Long ciModuleId) {
//        matchService.assignCIModuleToTelevision(id, ciModuleId);
//        return ResponseEntity.noContent().build();
//    }
}
