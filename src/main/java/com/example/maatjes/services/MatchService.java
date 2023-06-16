package com.example.maatjes.services;

import com.example.maatjes.dtos.MatchDto;
import com.example.maatjes.models.Match;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public MatchService(MatchRepository matchRepository, AccountService accountService, AccountRepository accountRepository) {
        this.matchRepository = matchRepository;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

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
//        matchDto.frequency = match.getFrequency();

        return matchDto;
    }

//    public void assignAccountToMatch(Long id, Long ciModuleId) {
//        var optionalTelevision = televisionRepository.findById(id);
//        var optionalCIModule = ciModuleRepository.findById(ciModuleId);
//
//        if(optionalTelevision.isPresent() && optionalCIModule.isPresent()) {
//            var television = optionalTelevision.get();
//            var ciModule = optionalCIModule.get();
//
//            television.setCiModule(ciModule);
//            televisionRepository.save(television);
//        } else {
//            throw new RecordNotFoundException();
//        }
//    }
}

