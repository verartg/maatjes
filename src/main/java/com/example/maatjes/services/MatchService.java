package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.MatchOutputDto;
import com.example.maatjes.dtos.inputDtos.MatchInputDto;
import com.example.maatjes.enums.Activities;
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

    public List<MatchOutputDto> getMatches() {
        List<Match> matches = matchRepository.findAll();
        List<MatchOutputDto> matchOutputDtos = new ArrayList<>();
        for (Match match : matches) {
            MatchOutputDto matchOutputDto = transferMatchToOutputDto(match);
            matchOutputDtos.add(matchOutputDto);
        }
        return matchOutputDtos;
    }

    public MatchOutputDto getMatch(Long id) {
        Optional<Match> optionalMatch = matchRepository.findById(id);
        if (optionalMatch.isEmpty()) {
            throw new RecordNotFoundException("Match niet gevonden");}
        Match match = optionalMatch.get();
        return transferMatchToOutputDto(match);
    }

    //todo matches filteren op contactperson voor de admin om eigen matches op te halen.

    public List<MatchOutputDto> getAcceptedMatchesByAccountId(Long accountId) throws RecordNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("De account met ID " + accountId + " bestaat niet."));
        List<Match> helpGivers = account.getHelpGivers();
        List<Match> helpReceivers = account.getHelpReceivers();
        List<MatchOutputDto> accountMatchOutputDtos = new ArrayList<>();
        for (Match m : helpGivers) {
        if (m.isGiverAccepted() && m.isReceiverAccepted()) {
            MatchOutputDto accountMatchOutputDto = transferMatchToOutputDto(m);
            accountMatchOutputDtos.add(accountMatchOutputDto);
        }
    }
        for (Match m : helpReceivers) {
        if (m.isGiverAccepted() && m.isReceiverAccepted()) {
            MatchOutputDto accountMatchOutputDto = transferMatchToOutputDto(m);
            accountMatchOutputDtos.add(accountMatchOutputDto);
        }
    }
        return accountMatchOutputDtos;
    }

    public List<MatchOutputDto> getProposedMatchesByAccountId(Long accountId) throws RecordNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("De account met ID " + accountId + " bestaat niet."));
        List<Match> helpGivers = account.getHelpGivers();
        List<Match> helpReceivers = account.getHelpReceivers();
        List<MatchOutputDto> accountMatchOutputDtos = new ArrayList<>();
        for (Match m : helpGivers) {
            if (!m.isGiverAccepted() || !m.isReceiverAccepted()) {
                MatchOutputDto accountMatchOutputDto = transferMatchToOutputDto(m);
                accountMatchOutputDtos.add(accountMatchOutputDto);
            }
        }
        for (Match m : helpReceivers) {
            if (!m.isGiverAccepted() || !m.isReceiverAccepted()) {
                MatchOutputDto accountMatchOutputDto = transferMatchToOutputDto(m);
                accountMatchOutputDtos.add(accountMatchOutputDto);
            }
        }
        return accountMatchOutputDtos;
    }


//todo eigenlijk moet ik helpreceiver omnoemen naar account1 en helpgiver account2 bijv.
    public MatchOutputDto proposeMatch(Long helpGiverId, Long helpReceiverId, MatchInputDto matchInputDto) throws RecordNotFoundException {
        Account giver = accountRepository.findById(helpGiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + helpGiverId + " bestaat niet"));
        Account receiver = accountRepository.findById(helpReceiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + helpReceiverId + " bestaat niet"));

        List<Activities> giverActivitiesToGive = giver.getActivitiesToGive();
        List<Activities> giverActivitiesToReceive = giver.getActivitiesToReceive();

        List<Activities> receiverActivitiesToGive = receiver.getActivitiesToGive();
        List<Activities> receiverActivitiesToReceive = receiver.getActivitiesToReceive();
        List<Activities> sharedActivities = getSharedActivities(giverActivitiesToGive, receiverActivitiesToReceive, giverActivitiesToReceive, receiverActivitiesToGive);

        if (sharedActivities == null) {
            throw new RecordNotFoundException("Geen gemeenschappelijke activiteiten gevonden voor beide accounts");
        } else {

        Match match = transferInputDtoToMatch(matchInputDto);
        match.setHelpGiver(giver);
        match.setHelpReceiver(receiver);
        match.setActivities(sharedActivities);

        match = matchRepository.save(match);
        return transferMatchToOutputDto(match); }
    }
    private List<Activities> getSharedActivities(List<Activities> giverActivitiesToGive, List<Activities> receiverActivitiesToReceive, List<Activities> giverActivitiesToReceive, List<Activities> receiverActivitiesToGive) {
        List<Activities> sharedActivities = new ArrayList<>();

        for (Activities activity : giverActivitiesToGive) {
            if (receiverActivitiesToReceive.contains(activity)) {
                sharedActivities.add(activity);}
        }

        for (Activities activity : giverActivitiesToReceive) {
            if (receiverActivitiesToGive.contains(activity)) {
                sharedActivities.add(activity);}
        }
        return sharedActivities.isEmpty() ? null : sharedActivities;
    }

    public void removeMatch(@RequestBody Long id) {
        Optional<Match> optionalMatch = matchRepository.findById(id);
        if (optionalMatch.isPresent()) {
            matchRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Match niet gevonden");
        }
    }

    public MatchOutputDto acceptMatch(Long matchId, Long accountId) throws RecordNotFoundException {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));
        if (!match.getHelpGiver().equals(account) && !match.getHelpReceiver().equals(account)) {
            throw new AccountNotAssociatedException("Account is niet geassocieerd met de match");
        }
            if (match.getHelpGiver().equals(account)) {
                match.setGiverAccepted(true);
            } else {
                match.setReceiverAccepted(true);
            }

            matchRepository.save(match);
            return transferMatchToOutputDto(match);
        }

    public MatchOutputDto updateMatch(Long id, MatchInputDto matchInputDto) throws RecordNotFoundException {
        Match match = matchRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De match met ID " + id + " bestaat niet"));

            match.setReceiverAccepted(matchInputDto.isReceiverAccepted());
            match.setGiverAccepted(matchInputDto.isGiverAccepted());
            match.setContactPerson(matchInputDto.getContactPerson());
            match.setStartMatch(matchInputDto.getStartMatch());
            match.setEndMatch(matchInputDto.getEndMatch());
            match.setAvailability(matchInputDto.getAvailability());
            match.setFrequency(matchInputDto.getFrequency());

            Match returnMatch = matchRepository.save(match);
            return transferMatchToOutputDto(returnMatch);
        }

    public MatchOutputDto transferMatchToOutputDto(Match match) {
        MatchOutputDto matchOutputDto = new MatchOutputDto();
        matchOutputDto.id = match.getId();
        matchOutputDto.giverAccepted = match.isGiverAccepted();
        matchOutputDto.receiverAccepted = match.isReceiverAccepted();
        matchOutputDto.startMatch = match.getStartMatch();
        matchOutputDto.contactPerson = match.getContactPerson();
        matchOutputDto.endMatch = match.getEndMatch();
        matchOutputDto.frequency = match.getFrequency();
        matchOutputDto.availability = match.getAvailability();
        matchOutputDto.helpGiverName = match.getHelpGiver().getName();
        matchOutputDto.helpReceiverName = match.getHelpReceiver().getName();
        matchOutputDto.activities = match.getActivities();
        matchOutputDto.matchReviews = match.getMatchReviews();
        return matchOutputDto;
    }

    public Match transferInputDtoToMatch(MatchInputDto matchInputDto) {
        Match match = new Match();
        match.setGiverAccepted(matchInputDto.isGiverAccepted());
        match.setReceiverAccepted(matchInputDto.isReceiverAccepted());
        match.setContactPerson(matchInputDto.getContactPerson());
        match.setStartMatch(matchInputDto.getStartMatch());
        int value = matchInputDto.getStartMatch().compareTo(matchInputDto.getEndMatch());
        if (value >= 0) {
            throw new IllegalArgumentException("De einddatum moet na de startdatum liggen.");
        }
        match.setEndMatch(matchInputDto.getEndMatch());
        match.setAvailability(matchInputDto.getAvailability());
        match.setFrequency(matchInputDto.getFrequency());
        return match;
    }
}

