package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.AppointmentOutputDto;
import com.example.maatjes.dtos.outputDtos.MatchOutputDto;
import com.example.maatjes.dtos.inputDtos.MatchInputDto;
import com.example.maatjes.dtos.outputDtos.MessageOutputDto;
import com.example.maatjes.dtos.outputDtos.ReviewOutputDto;
import com.example.maatjes.enums.Activities;
import com.example.maatjes.enums.ContactPerson;
import com.example.maatjes.exceptions.*;
import com.example.maatjes.exceptions.IllegalArgumentException;
import com.example.maatjes.models.*;
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
    private final ReviewService reviewService;
    private final AppointmentService appointmentService;
    private final MessageService messageService;

    public MatchService(MatchRepository matchRepository, AccountRepository accountRepository, UserRepository userRepository, ReviewService reviewService, AppointmentService appointmentService, MessageService messageService) {
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.reviewService = reviewService;
        this.appointmentService = appointmentService;
        this.messageService = messageService;
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
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = authentication.getName().equals(match.getHelpGiver().getUser().getUsername()) || authentication.getName().equals(match.getHelpReceiver().getUser().getUsername());

        if (isAdmin || isSelf) {
            return transferMatchToOutputDto(match);
        } else {
            throw new AccessDeniedException("Je hebt geen toegang tot deze match");
        }
    }

    public List<MatchOutputDto> getMatchesByUsername(String username, boolean accepted) throws RecordNotFoundException {
        SecurityUtils.validateUsername(username, "matches");
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

        String helpGiverName = match.getHelpGiver().getUser().getUsername();
        String helpReceiverName = match.getHelpReceiver().getUser().getUsername();

        SecurityUtils.validateUsernames(helpGiverName, helpReceiverName, "matches");

        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(helpGiverName)) {
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
        match = matchRepository.save(transferInputDtoToMatch(match, matchInputDto));
        return transferMatchToOutputDto(match);
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

    public Match transferInputDtoToMatch(MatchInputDto matchInputDto) throws IllegalArgumentException {
        Match match = new Match();
        return transferInputDtoToMatch(match, matchInputDto);
    }

    public Match transferInputDtoToMatch(Match match, MatchInputDto matchInputDto) throws IllegalArgumentException {
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
        match.setMatchReviews(new ArrayList<>());
        match.setAppointments(new ArrayList<>());
        match.setMessages(new ArrayList<>());
        return match;
    }

    public MatchOutputDto transferMatchToOutputDto(Match match) {
        MatchOutputDto matchOutputDto = new MatchOutputDto();
        matchOutputDto.setMatchId(match.getMatchId());
        matchOutputDto.setGiverAccepted(match.isGiverAccepted());
        matchOutputDto.setReceiverAccepted(match.isReceiverAccepted());
        matchOutputDto.setContactPerson(match.getContactPerson());
        matchOutputDto.setStartMatch(match.getStartMatch());
        matchOutputDto.setEndMatch(match.getEndMatch());
        matchOutputDto.setAvailability(match.getAvailability());
        matchOutputDto.setFrequency(match.getFrequency());
        matchOutputDto.setHelpReceiverName(match.getHelpReceiver().getUser().getUsername());
        matchOutputDto.setHelpGiverName(match.getHelpGiver().getUser().getUsername());
        matchOutputDto.setActivities(match.getActivities());

        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review review : match.getMatchReviews()) {
            ReviewOutputDto reviewOutputDto = reviewService.transferReviewToOutputDto(review);
            reviewOutputDtos.add(reviewOutputDto);
        }
        matchOutputDto.setMatchReviews(reviewOutputDtos);

        List<AppointmentOutputDto> appointmentOutputDtos = new ArrayList<>();
        for (Appointment appointment : match.getAppointments()) {
            AppointmentOutputDto appointmentOutputDto = appointmentService.transferAppointmentToOutputDto(appointment);
            appointmentOutputDtos.add(appointmentOutputDto);
        }
        matchOutputDto.setAppointments(appointmentOutputDtos);

        List<MessageOutputDto> messageOutputDtos = new ArrayList<>();
        for (Message message : match.getMessages()) {
            MessageOutputDto messageOutputDto = messageService.transferMessageToOutputDto(message);
            messageOutputDtos.add(messageOutputDto);
        }
        matchOutputDto.setMessages(messageOutputDtos);

        return matchOutputDto;
    }
}

