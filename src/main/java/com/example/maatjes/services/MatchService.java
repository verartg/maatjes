package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.MatchOutputDto;
import com.example.maatjes.dtos.inputDtos.MatchInputDto;
import com.example.maatjes.enums.Activities;
import com.example.maatjes.exceptions.*;
import com.example.maatjes.exceptions.IllegalArgumentException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public MatchService(MatchRepository matchRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    //todo nog nadenken over of je wel twee keer dezelfde match mag maken.
    public MatchOutputDto proposeMatch(MatchInputDto matchInputDto) throws RecordNotFoundException, BadRequestException {
        Account giver = accountRepository.findById(matchInputDto.helpGiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + matchInputDto.helpGiverId + " bestaat niet"));
        Account receiver = accountRepository.findById(matchInputDto.helpReceiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + matchInputDto.helpReceiverId + " bestaat niet"));
        List<Activities> giverActivitiesToGive = giver.getActivitiesToGive();
        List<Activities> receiverActivitiesToReceive = receiver.getActivitiesToReceive();
        List<Activities> sharedActivities = getSharedActivities(giverActivitiesToGive, receiverActivitiesToReceive);
        if (sharedActivities == null) {
            throw new BadRequestException("Geen gemeenschappelijke activiteiten gevonden voor beide accounts");
        } else {
            Match match = transferInputDtoToMatch(matchInputDto);
            match.setHelpGiver(giver);
            match.setHelpReceiver(receiver);
            match.setActivities(sharedActivities);
            match = matchRepository.save(match);
            return transferMatchToOutputDto(match); }
    }

    private List<Activities> getSharedActivities(List<Activities> giverActivitiesToGive, List<Activities> receiverActivitiesToReceive) {
        List<Activities> sharedActivities = new ArrayList<>();
        for (Activities activity : giverActivitiesToGive) {
            if (receiverActivitiesToReceive.contains(activity)) {
                sharedActivities.add(activity);}
        }
        return sharedActivities.isEmpty() ? null : sharedActivities;
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

//    public MatchOutputDto getMatch(Long matchId) throws RecordNotFoundException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//        boolean isSelf = authentication.getName().equals(username);
//
//        if (isAdmin || isSelf) {
//            Optional<Match> optionalMatch = matchRepository.findById(matchId);
//            if (optionalMatch.isEmpty()) {
//                throw new RecordNotFoundException("Match niet gevonden");}
//            Match match = optionalMatch.get();
//        return transferMatchToOutputDto(match);}
//    }

    public MatchOutputDto getMatch(Long matchId) throws RecordNotFoundException, AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String authenticatedUsername = authentication.getName();

        Optional<Match> optionalMatch = matchRepository.findById(matchId);
        if (optionalMatch.isEmpty()) {
            throw new RecordNotFoundException("Match niet gevonden");
        }
        Match match = optionalMatch.get();

        boolean isSelf = authenticatedUsername.equals(match.getHelpGiver().getUser().getUsername()) || authenticatedUsername.equals(match.getHelpReceiver().getUser().getUsername());

        if (isAdmin || isSelf) {
            return transferMatchToOutputDto(match);
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze match");
        }
    }

    //todo matches filteren op contactperson voor de admin om eigen matches op te halen.
    public List<MatchOutputDto> getAcceptedMatchesByUsername(String username) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(authentication.getName())) {
            throw new AccessDeniedException("Je kunt alleen je eigen matches inzien");
        }
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("De gebruiker met gebruikersnaam " + username + " bestaat niet."));
        Account account = accountRepository.findById(user.getAccount().getAccountId()).orElseThrow(() -> new RecordNotFoundException("De account met ID " + user.getAccount().getAccountId() + " bestaat niet."));
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

    public List<MatchOutputDto> getProposedMatchesByUsername(String username) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(authentication.getName())) {
            throw new AccessDeniedException("Je kunt alleen je eigen matches inzien");
        }
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("De gebruiker met gebruikersnaam " + username + " bestaat niet."));
        Account account = accountRepository.findById(user.getAccount().getAccountId()).orElseThrow(() -> new RecordNotFoundException("De account met ID " + user.getAccount().getAccountId() + " bestaat niet."));
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

    public MatchOutputDto acceptMatch(Long matchId) throws RecordNotFoundException, AccessDeniedException {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        boolean isGiver = authenticatedUsername.equals(match.getHelpGiver().getUser().getUsername());
        boolean isReceiver = authenticatedUsername.equals(match.getHelpReceiver().getUser().getUsername());

        if (!isGiver && !isReceiver) {
            throw new AccessDeniedException("Je bent niet geautoriseerd om deze match te accepteren");
        }

        if (isGiver) {
            if (!match.isGiverAccepted()) {
                match.setGiverAccepted(true);
            } else {
                throw new BadRequestException("De match is al geaccepteerd door jou");
            }
        } else {
            if (!match.isReceiverAccepted()) {
                match.setReceiverAccepted(true);
            } else {
                throw new BadRequestException("De match is al geaccepteerd door jou");
            }
        }
        matchRepository.save(match);
        return transferMatchToOutputDto(match);
    }

    public MatchOutputDto updateMatch(Long matchId, MatchInputDto matchInputDto) throws RecordNotFoundException {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("De match met ID " + matchId + " is niet gevonden"));
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

    public String removeMatch(@RequestBody Long matchId) throws RecordNotFoundException {
        Optional<Match> optionalMatch = matchRepository.findById(matchId);
        if (optionalMatch.isPresent()) {
            matchRepository.deleteById(matchId);
        } else {
            throw new RecordNotFoundException("Match niet gevonden");
        }
        return "Match succesvol verwijderd";
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

    public Match transferInputDtoToMatch(MatchInputDto matchInputDto) throws IllegalArgumentException {
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

