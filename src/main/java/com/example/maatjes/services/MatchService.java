package com.example.maatjes.services;

import com.example.maatjes.dtos.MatchDto;
import com.example.maatjes.dtos.MatchInputDto;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Match;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final AccountRepository accountRepository;

    public MatchService(MatchRepository matchRepository, AccountRepository accountRepository) {
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
    }

//    public List<MatchDto> getMatchesByAccountId(Long accountId) {
//        List<MatchDto> dtos = new HashSet<>();
//
//    }

//    public Collection<TelevisionDto> getTelevisionsByWallBracketId(Long wallBracketId) {
//        Collection<TelevisionDto> dtos = new HashSet<>();
//        Collection<TelevisionWallBracket> televisionWallbrackets = televisionWallBracketRepository.findAllByWallBracketId(wallBracketId);
//        for (TelevisionWallBracket televisionWallbracket : televisionWallbrackets) {
//            Television television = televisionWallbracket.getTelevision();
//            TelevisionDto televisionDto = new TelevisionDto();
//
//            televisionDto.setId(television.getId());
//            televisionDto.setType(television.getType());
//            televisionDto.setBrand(television.getBrand());
//            televisionDto.setName(television.getName());
//            televisionDto.setPrice(television.getPrice());
//            televisionDto.setAvailableSize(television.getAvailableSize());
//            televisionDto.setRefreshRate(television.getRefreshRate());
//            televisionDto.setScreenType(television.getScreenType());
//            televisionDto.setScreenQuality(television.getScreenQuality());
//            televisionDto.setSmartTv(television.getSmartTv());
//            televisionDto.setWifi(television.getWifi());
//            televisionDto.setVoiceControl(television.getVoiceControl());
//            televisionDto.setHdr(television.getHdr());
//            televisionDto.setBluetooth(television.getBluetooth());
//            televisionDto.setAmbiLight(television.getAmbiLight());
//            televisionDto.setOriginalStock(television.getOriginalStock());
//            televisionDto.setSold(television.getSold());
//
//            dtos.add(televisionDto);
//        }
//        return dtos;
//    }

    public List<MatchDto> getMatches() {
        List<Match> matches = matchRepository.findAll();
        List<MatchDto> matchDtos = new ArrayList<>();
        for (Match match : matches) {
            matchDtos.add(transferMatchToDto(match));
        }
        return matchDtos;
    }

    public MatchDto createMatch(Long helpGiverId, Long helpReceiverId, MatchInputDto matchInputDto) throws RecordNotFoundException {
        Account giver = accountRepository.findById(helpGiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + helpGiverId + " bestaat niet"));
        Account receiver = accountRepository.findById(helpReceiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + helpReceiverId + " bestaat niet"));
        Match match = transferInputDtoToMatch(matchInputDto);
        match.setHelpGiver(giver);
        match.setHelpReceiver(receiver);
        matchRepository.save(match);
        return transferMatchToDto(match);
    }

    public MatchDto transferMatchToDto(Match match) {
        MatchDto matchDto = new MatchDto();
        matchDto.id = match.getId();
        matchDto.accepted = match.isAccepted();
        matchDto.startMatch = match.getStartMatch();
        matchDto.contactPerson = match.getContactPerson();
        matchDto.endMatch = match.getEndMatch();
        matchDto.frequency = match.getFrequency();
        matchDto.availability = match.getAvailability();
        matchDto.nameGiver = match.getHelpGiver().getName();
        matchDto.nameReceiver = match.getHelpReceiver().getName();
        return matchDto;
    }

    public Match transferInputDtoToMatch(MatchInputDto matchInputDto) {
        var match = new Match();
        match.setId(matchInputDto.getId());
        match.setAccepted(matchInputDto.isAccepted());
        match.setContactPerson(matchInputDto.getContactPerson());
        match.setStartMatch(matchInputDto.getStartMatch());
        match.setEndMatch(matchInputDto.getEndMatch());
        match.setAvailability(matchInputDto.getAvailability());
        match.setFrequency(matchInputDto.getFrequency());
        return match;
    }
}

