package com.example.maatjes.services;

import com.example.maatjes.dtos.MatchDto;
import com.example.maatjes.dtos.MatchInputDto;
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

    public List<MatchDto> getMatches() {
        List<Match> matches = matchRepository.findAll();
        List<MatchDto> matchDtos = new ArrayList<>();
        for (Match match : matches) {
            MatchDto matchDto = transferMatchToDto(match);
            matchDtos.add(matchDto);
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
//todo eigenlijk moet ik helpreceiver omnoemen naar account1 en helpgiver account2 bijv.
    public MatchDto proposeMatch(Long helpGiverId, Long helpReceiverId, MatchInputDto matchInputDto) throws RecordNotFoundException {
        // we halen de gever op
        Account giver = accountRepository.findById(helpGiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + helpGiverId + " bestaat niet"));
        //we halen de ontvanger op
        Account receiver = accountRepository.findById(helpReceiverId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met id " + helpReceiverId + " bestaat niet"));
        // we halen de activiteiten op van de gever. account1
        List<Activities> giverActivitiesToGive = giver.getActivitiesToGive(); //kan goed Frans
        List<Activities> giverActivitiesToReceive = giver.getActivitiesToReceive(); //kan niet goed tuinieren
        // we halen de activiteiten op van de ontvanger.
        List<Activities> receiverActivitiesToGive = receiver.getActivitiesToGive(); //kan goed tuinieren
        List<Activities> receiverActivitiesToReceive = receiver.getActivitiesToReceive(); //kan niet goed administratie


        List<Activities> sharedActivities = getSharedActivities(giverActivitiesToGive, receiverActivitiesToReceive, giverActivitiesToReceive, receiverActivitiesToGive);
        //                                                      account1toGive          account2toreceive                       tuinieren               tuinieren
        //                                                              frans               admin
//        List<Activities> sharedActivitiesOtherWay = getSharedActivities(giverActivitiesToReceive, receiverActivitiesToGive);
        //                                                      account1toReceive          account2toGive
        //                                                              tuinieren               tuinieren

        if (sharedActivities == null) {
            throw new RuntimeException("No matching activities found for the accounts");
        }

        Match match = transferInputDtoToMatch(matchInputDto);
        match.setHelpGiver(giver);
        match.setHelpReceiver(receiver);
        match.setGiverAccepted(false);
        match.setReceiverAccepted(false);
        match.setActivities(sharedActivities);
        match = matchRepository.save(match);
        return transferMatchToDto(match);
    }
    private List<Activities> getSharedActivities(List<Activities> giverActivitiesToGive, List<Activities> receiverActivitiesToReceive, List<Activities> giverActivitiesToReceive, List<Activities> receiverActivitiesToGive) {
        //                                                          frans                   admin
        List<Activities> sharedActivities = new ArrayList<>();
        // nieuwe lijst aanmaken voor overeenkomstige activiteiten
        for (Activities activity : giverActivitiesToGive) {
            // voor iedere activiteit uit lijst1
            if (receiverActivitiesToReceive.contains(activity)) {
                // als lijst2 een activiteit van lijst1 bevat
                sharedActivities.add(activity);
            }
        }

        for (Activities activity : giverActivitiesToReceive) {
            // voor iedere activiteit uit lijst1
            if (receiverActivitiesToGive.contains(activity)) {
                // als lijst2 een activiteit van lijst1 bevat
                sharedActivities.add(activity);
            }
        }
        return sharedActivities.isEmpty() ? null : sharedActivities;
    }


//        Match match = transferInputDtoToMatch(matchInputDto);
//        match.setHelpGiver(giver);
//        match.setHelpReceiver(receiver);
//        match.setGiverAccepted(false);
//        match.setReceiverAccepted(false);
//
//        match = matchRepository.save(match);
//
//        return transferMatchToDto(match);
    //}







    public void removeMatch(@RequestBody Long id) {
        Optional<Match> optionalMatch = matchRepository.findById(id);
        if (optionalMatch.isPresent()) {
            matchRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Match niet gevonden");
        }
    }

    public MatchDto acceptMatch(Long matchId, Long accountId) throws RecordNotFoundException {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match not found"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RecordNotFoundException("Account not found"));
        if (!match.getHelpGiver().equals(account) && !match.getHelpReceiver().equals(account)) {
            throw new RecordNotFoundException("Account is not associated with the Match");
            //AccountNotAssociatedException werkt niet, deze wel...
        }
            if (match.getHelpGiver().equals(account)) {
                match.setGiverAccepted(true);
            } else {
                match.setReceiverAccepted(true);
            }

            matchRepository.save(match);
            return transferMatchToDto(match);
        }

    public MatchDto updateMatch(Long id, MatchInputDto matchInputDto) throws RecordNotFoundException {
        Match match = matchRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The match with ID " + id + " doesn't exist"));

            match.setReceiverAccepted(matchInputDto.isReceiverAccepted());
            match.setGiverAccepted(matchInputDto.isGiverAccepted());
            match.setContactPerson(matchInputDto.getContactPerson());
            match.setStartMatch(matchInputDto.getStartMatch());
            match.setEndMatch(matchInputDto.getEndMatch());
            match.setAvailability(matchInputDto.getAvailability());
            match.setFrequency(matchInputDto.getFrequency());

            Match returnMatch = matchRepository.save(match);
            return transferMatchToDto(returnMatch);
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
        matchDto.helpGiverName = match.getHelpGiver().getName();
        matchDto.helpReceiverName = match.getHelpReceiver().getName();
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
        return match;
    }
}

