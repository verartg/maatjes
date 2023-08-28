package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.MatchOutputDto;
import com.example.maatjes.dtos.inputDtos.MatchInputDto;
import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.ContactPerson;
import com.example.maatjes.exceptions.*;
import com.example.maatjes.exceptions.IllegalArgumentException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Match;
import com.example.maatjes.models.User;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.MatchRepository;
import com.example.maatjes.repositories.UserRepository;
import com.example.maatjes.util.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public MatchOutputDto proposeMatch(MatchInputDto matchInputDto) throws RecordNotFoundException, BadRequestException {
        Account giver = accountRepository.findById(matchInputDto.helpGiverId)
                .orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + matchInputDto.helpGiverId + " bestaat niet"));

        Account receiver = accountRepository.findById(matchInputDto.helpReceiverId)
                .orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + matchInputDto.helpReceiverId + " bestaat niet"));

        List<Activities> giverActivitiesToGive = giver.getActivitiesToGive();
        List<Activities> receiverActivitiesToReceive = receiver.getActivitiesToReceive();
        List<Activities> sharedActivities = getSharedActivities(giverActivitiesToGive, receiverActivitiesToReceive);

        if (sharedActivities == null) {
            throw new BadRequestException("Geen gemeenschappelijke activiteiten gevonden voor beide accounts");
        } else {
            List<Match> matches = matchRepository.findAll();
            boolean isDuplicateMatch = false;

            for (Match match : matches) {
                if (match.getHelpGiver().equals(giver) && match.getHelpReceiver().equals(receiver)
                        && new HashSet<>(match.getActivities()).containsAll(sharedActivities)) {
                    isDuplicateMatch = true;
                    break;
                }
            }

            if (isDuplicateMatch) {
                throw new BadRequestException("Dezelfde match bestaat al of is al voorgesteld.");
            }

            Match match = transferInputDtoToMatch(matchInputDto);
            match.setHelpGiver(giver);
            match.setHelpReceiver(receiver);
            match.setActivities(sharedActivities);

            match = matchRepository.save(match);
            return transferMatchToOutputDto(match);
        }
    }

    public List<Activities> getSharedActivities(List<Activities> giverActivitiesToGive, List<Activities> receiverActivitiesToReceive) {
        List<Activities> sharedActivities = new ArrayList<>();
        for (Activities activity : giverActivitiesToGive) {
            if (receiverActivitiesToReceive.contains(activity)) {
                sharedActivities.add(activity);}
        }
        return sharedActivities.isEmpty() ? null : sharedActivities;
    }

    public List<MatchOutputDto> getMatches(Optional<ContactPerson> contactPerson) {
        List<Match> matches;
        if (contactPerson.isPresent()) {
            matches = matchRepository.findAllByContactPerson(contactPerson.get());
        } else {
            matches = matchRepository.findAll();
        }

        List<MatchOutputDto> matchOutputDtos = new ArrayList<>();
        for (Match match : matches) {
            MatchOutputDto matchOutputDto = transferMatchToOutputDto(match);
            matchOutputDtos.add(matchOutputDto);
        }
        return matchOutputDtos;
    }

    public MatchOutputDto getMatch(Long matchId) throws RecordNotFoundException, AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        boolean isSelf = username.equals(match.getHelpGiver().getUser().getUsername()) || username.equals(match.getHelpReceiver().getUser().getUsername());

        if (isAdmin || isSelf) {
            return transferMatchToOutputDto(match);
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze match");
        }
    }

    public List<MatchOutputDto> getMatchesByUsername(String username, boolean accepted) throws RecordNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException("De gebruiker met gebruikersnaam " + username + " bestaat niet."));
        Account account = accountRepository.findById(user.getAccount().getAccountId())
                .orElseThrow(() -> new RecordNotFoundException("De account met ID " + user.getAccount().getAccountId() + " bestaat niet."));

        List<Match> helpGivers = account.getMatchesWhereAccountIsHelpGiver();
        List<Match> helpReceivers = account.getMatchesWhereAccountIsHelpReceiver();

        List<MatchOutputDto> accountMatchOutputDtos = new ArrayList<>();

        for (Match m : helpGivers) {
            if (accepted ? (m.isGiverAccepted() && m.isReceiverAccepted()) : (!m.isGiverAccepted() || !m.isReceiverAccepted())) {
                MatchOutputDto accountMatchOutputDto = transferMatchToOutputDto(m);
                accountMatchOutputDtos.add(accountMatchOutputDto);
            }
        }

        for (Match m : helpReceivers) {
            if (accepted ? (m.isGiverAccepted() && m.isReceiverAccepted()) : (!m.isGiverAccepted() || !m.isReceiverAccepted())) {
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
        String username = authentication.getName();

        boolean isGiver = username.equals(match.getHelpGiver().getUser().getUsername());
        boolean isReceiver = username.equals(match.getHelpReceiver().getUser().getUsername());

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

        LocalDateTime startMatch = matchInputDto.getStartMatch().atStartOfDay();
        LocalDateTime endMatch = matchInputDto.getEndMatch().atStartOfDay();

        if (startMatch.isAfter(endMatch)) {
            throw new IllegalArgumentException("De einddatum moet na de startdatum liggen.");
        }

        match.setReceiverAccepted(matchInputDto.isReceiverAccepted());
        match.setGiverAccepted(matchInputDto.isGiverAccepted());
        match.setContactPerson(matchInputDto.getContactPerson());
        match.setStartMatch(LocalDate.from(startMatch));
        match.setEndMatch(LocalDate.from(endMatch));
        match.setAvailability(matchInputDto.getAvailability());
        match.setFrequency(matchInputDto.getFrequency());

        Match returnMatch = matchRepository.save(match);
        return transferMatchToOutputDto(returnMatch);
    }
    public Match transferInputDtoToMatch(MatchInputDto matchInputDto) throws IllegalArgumentException {
        Match match = new Match();
        LocalDateTime startMatch = matchInputDto.getStartMatch().atStartOfDay();
        LocalDateTime endMatch = matchInputDto.getEndMatch().atStartOfDay();

        if (startMatch.isAfter(endMatch)) {
            throw new IllegalArgumentException("De einddatum moet na de startdatum liggen.");
        }
        match.setReceiverAccepted(matchInputDto.isReceiverAccepted());
        match.setGiverAccepted(matchInputDto.isGiverAccepted());
        match.setContactPerson(matchInputDto.getContactPerson());
        match.setStartMatch(LocalDate.from(startMatch));
        match.setEndMatch(LocalDate.from(endMatch));
        match.setAvailability(matchInputDto.getAvailability());
        match.setFrequency(matchInputDto.getFrequency());
        return match;
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

    public String removeExpiredMatches() {
        LocalDate currentDate = LocalDate.now();
        List<Match> expiredMatches = matchRepository.findAllByEndMatchBefore(currentDate);
        matchRepository.deleteAll(expiredMatches);
        return "Verlopen matches succesvol verwijderd.";
    }

    public MatchOutputDto transferMatchToOutputDto(Match match) {
        MatchOutputDto matchOutputDto = new MatchOutputDto();
        matchOutputDto.matchId = match.getMatchId();
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


}

