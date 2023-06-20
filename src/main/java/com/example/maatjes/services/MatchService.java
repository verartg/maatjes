package com.example.maatjes.services;

import com.example.maatjes.dtos.MatchDto;
import com.example.maatjes.dtos.MatchInputDto;
import com.example.maatjes.exceptions.AccountNotAssociatedException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Match;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final AccountRepository accountRepository;

    public MatchService(MatchRepository matchRepository, AccountRepository accountRepository) {
        this.matchRepository = matchRepository;
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

    public MatchDto getMatch(Long id) {
        Optional<Match> optionalMatch = matchRepository.findById(id);
        if (optionalMatch.isEmpty()) {
            throw new RecordNotFoundException("Match niet gevonden");}
        Match match = optionalMatch.get();
        return transferMatchToDto(match);
    }

    public List<MatchDto> getAcceptedMatchesByAccountId(Long accountId) throws RecordNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("The Account with ID " + accountId + " doesn't exist."));
        List<Match> helpGivers = account.getHelpGivers();
        List<Match> helpReceivers = account.getHelpReceivers();
        List<MatchDto> accountMatchDtos = new ArrayList<>();
        for (Match m : helpGivers) {
        if (m.isGiverAccepted() && m.isReceiverAccepted()) {
            MatchDto accountMatchDto = transferMatchToDto(m);
            accountMatchDtos.add(accountMatchDto);
        }
    }
        for (Match m : helpReceivers) {
        if (m.isGiverAccepted() && m.isReceiverAccepted()) {
            MatchDto accountMatchDto = transferMatchToDto(m);
            accountMatchDtos.add(accountMatchDto);
        }
    }
        return accountMatchDtos;
    }

    public List<MatchDto> getProposedMatchesByAccountId(Long accountId) throws RecordNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("The Account with ID " + accountId + " doesn't exist."));
        List<Match> helpGivers = account.getHelpGivers();
        List<Match> helpReceivers = account.getHelpReceivers();
        List<MatchDto> accountMatchDtos = new ArrayList<>();
        for (Match m : helpGivers) {
            if (!m.isGiverAccepted() || !m.isReceiverAccepted()) {
                MatchDto accountMatchDto = transferMatchToDto(m);
                accountMatchDtos.add(accountMatchDto);
            }
        }
        for (Match m : helpReceivers) {
            if (!m.isGiverAccepted() || !m.isReceiverAccepted()) {
                MatchDto accountMatchDto = transferMatchToDto(m);
                accountMatchDtos.add(accountMatchDto);
            }
        }
        return accountMatchDtos;
    }

    public MatchDto proposeMatch(Long helpGiverId, Long helpReceiverId, MatchInputDto matchInputDto) throws RecordNotFoundException {
        Account giver = accountRepository.findById(helpGiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + helpGiverId + " bestaat niet"));
        Account receiver = accountRepository.findById(helpReceiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + helpReceiverId + " bestaat niet"));
        Match match = transferInputDtoToMatch(matchInputDto);
        match.setHelpGiver(giver);
        match.setHelpReceiver(receiver);
        match.setGiverAccepted(false);
        match.setReceiverAccepted(false);

        matchRepository.save(match);
        return transferMatchToDto(match);
    }

    public void removeMatch(@RequestBody Long id) {
        Optional<Match> optionalMatch = matchRepository.findById(id);
        if (optionalMatch.isPresent()) {
            matchRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Match niet gevonden");
        }
    }

//    public MatchDto acceptMatch(Long matchId, Long accountId, MatchInputDto newMatch) throws RecordNotFoundException, AccountNotAssociatedException {
//        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match not found"));
//        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("Account not found"));
//        if (!match.getHelpGiver().equals(account) && !match.getHelpReceiver().equals(account)) {
//            throw new AccountNotAssociatedException("Account is not associated with the Match");
//        }
//            if (match.getHelpGiver().equals(account)) {
//                match.setGiverAccepted(newMatch.isGiverAccepted());
//            } else {
//                match.setReceiverAccepted(newMatch.isReceiverAccepted());
//            }
//
//            matchRepository.save(match);
//            return transferMatchToDto(match);
//        }

//    public MatchDto updateMatch(Long id, Long accountId, MatchInputDto newMatch) {
//        Optional<Match> matchOptional = matchRepository.findById(id);
//        Optional<Account> accountOptional = accountRepository.findById(accountId);
//        if (matchOptional.isPresent()) {
//            Match match = matchOptional.get();
//            if (accountOptional.isPresent()) {
//                Account account = accountOptional.get();
//                if (!match.getHelpGiver().equals(account) && !match.getHelpReceiver().equals(account)) {
//                    throw new AccountNotAssociatedException("Account is not associated with the Match");
//                }
//                if (match.getHelpGiver().equals(account)) {
//                    match.setGiverAccepted(newMatch.isGiverAccepted());
//                } else {
//                    match.setReceiverAccepted(newMatch.isReceiverAccepted());
//                }
//            }
//                Match returnMatch = matchRepository.save(match);
//                return transferMatchToDto(returnMatch);
//            } else {
//                throw new RecordNotFoundException("Match niet gevonden");
//            }
//        }

    public MatchDto updateMatch(Long id, MatchInputDto newMatch) {
        Optional<Match> matchOptional = matchRepository.findById(id);
        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();

            match.setReceiverAccepted(newMatch.isReceiverAccepted());
            match.setGiverAccepted(newMatch.isGiverAccepted());
            match.setContactPerson(newMatch.getContactPerson());
            match.setStartMatch(newMatch.getStartMatch());
            match.setEndMatch(newMatch.getEndMatch());
            match.setAvailability(newMatch.getAvailability());
            match.setFrequency(newMatch.getFrequency());
            match.setHelpGiver(newMatch.getHelpGiver());
            match.setHelpReceiver(newMatch.getHelpReceiver());

            Match returnMatch = matchRepository.save(match);
            return transferMatchToDto(returnMatch);
        } else {
            throw new RecordNotFoundException("Match niet gevonden");
        }
    }

    public MatchDto transferMatchToDto(Match match) {
        MatchDto matchDto = new MatchDto();
        matchDto.id = match.getId();
        matchDto.giverAccepted = match.isGiverAccepted();
        matchDto.receiverAccepted = match.isReceiverAccepted();
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
        match.setGiverAccepted(matchInputDto.isGiverAccepted());
        match.setReceiverAccepted(matchInputDto.isReceiverAccepted());
        match.setContactPerson(matchInputDto.getContactPerson());
        match.setStartMatch(matchInputDto.getStartMatch());
        match.setEndMatch(matchInputDto.getEndMatch());
        match.setAvailability(matchInputDto.getAvailability());
        match.setFrequency(matchInputDto.getFrequency());
        match.setHelpGiver(matchInputDto.getHelpGiver());
        match.setHelpReceiver(matchInputDto.getHelpReceiver());
        return match;
    }
}

