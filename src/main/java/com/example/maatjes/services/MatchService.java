package com.example.maatjes.services;

import com.example.maatjes.dtos.MatchDto;
import com.example.maatjes.models.Match;
import com.example.maatjes.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {this.matchRepository = matchRepository; }

    public List<MatchDto> getMatches() {
        List<Match> matches = matchRepository.findAll();
        List<MatchDto> matchDtos = new ArrayList<>();
        for (Match match : matches) {
            matchDtos.add(transferMatchToDto(match));
        }
        return matchDtos;
    }

    public MatchDto transferMatchToDto(Match match) {
        MatchDto matchDto = new MatchDto();
        matchDto.id = match.getId();
        matchDto.accepted = match.isAccepted();
        matchDto.startMatch = match.getStartMatch();
        matchDto.endMatch = match.getEndMatch();
        matchDto.frequency = match.getFrequency();

        return matchDto;
    }
}
