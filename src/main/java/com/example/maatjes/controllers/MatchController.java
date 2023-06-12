package com.example.maatjes.controllers;

import com.example.maatjes.services.MatchService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    public MatchController(MatchService matchService) {this.matchService = matchService;}

}
