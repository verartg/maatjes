package com.example.maatjes.services;

import com.example.maatjes.dtos.outputDtos.AppointmentOutputDto;
import com.example.maatjes.dtos.inputDtos.AppointmentInputDto;
import com.example.maatjes.exceptions.*;
import com.example.maatjes.exceptions.IllegalArgumentException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Appointment;
import com.example.maatjes.models.Match;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.AppointmentRepository;
import com.example.maatjes.repositories.MatchRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MatchRepository matchRepository;
    private final AccountRepository accountRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, MatchRepository matchRepository, AccountRepository accountRepository) {
        this.appointmentRepository = appointmentRepository;
        this.matchRepository = matchRepository;
        this.accountRepository = accountRepository;
    }

    public AppointmentOutputDto createAppointment(AppointmentInputDto appointmentInputDto) throws RecordNotFoundException, BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Match match = matchRepository.findById(appointmentInputDto.getMatchId())
                .orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        boolean isAssociatedUser = username.equals(match.getHelpGiver().getUser().getUsername())
                || username.equals(match.getHelpReceiver().getUser().getUsername());

        if (!isAssociatedUser) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze match");
        }

        if (!match.isGiverAccepted() || !match.isReceiverAccepted()) {
            throw new BadRequestException("Match moet eerst worden geaccepteerd voordat een afspraak kan worden ingepland");
        }

        Appointment appointment = transferInputDtoToAppointment(appointmentInputDto);
        appointment.setMatch(match);
        appointment.setCreatedByName(username);
        appointment.setCreatedForName(match.getHelpGiver().getUser().getUsername().equals(username) ? match.getHelpReceiver().getUser().getUsername() : match.getHelpGiver().getUser().getUsername());
        appointment = appointmentRepository.save(appointment);
        match.getAppointments().add(appointment);
        matchRepository.save(match);

        return transferAppointmentToOutputDto(appointment);
    }


    //    private Account determineLoggedInAccount() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof Account) {
//            return (Account) authentication.getPrincipal();
//        }
//        throw new RuntimeException("No logged-in account found");
//    }

    public List<AppointmentOutputDto> getAppointmentsByMatchId(Long matchId) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        if (!username.equals(match.getHelpGiver().getUser().getUsername()) && !username.equals(match.getHelpReceiver().getUser().getUsername())) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze gebruiker");
        }

        List<AppointmentOutputDto> appointmentOutputDtos = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (Appointment appointment : match.getAppointments()) {
            if (appointment.getDate().isAfter(currentDate)) {
                appointmentOutputDtos.add(transferAppointmentToOutputDto(appointment));
            }
        }

        return appointmentOutputDtos;
    }

    public List<AppointmentOutputDto> getAppointmentsByAccountId(Long accountId) throws RecordNotFoundException, AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));

        if (!username.equals(account.getUser().getUsername())) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze gebruiker");
        }

        LocalDate currentDate = LocalDate.now();

        List<AppointmentOutputDto> appointmentOutputDtos = Stream.concat(
                        account.getHelpReceivers().stream().flatMap(match -> match.getAppointments().stream()),
                        account.getHelpGivers().stream().flatMap(match -> match.getAppointments().stream()))
                .filter(appointment -> appointment.getDate().isAfter(currentDate))
                .map(this::transferAppointmentToOutputDto)
                .collect(Collectors.toList());

        return appointmentOutputDtos;
    }

//todo check of wel klopt met apointment/accountid
    public AppointmentOutputDto getAppointment(Long appointmentId) throws RecordNotFoundException, AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Afspraak niet gevonden"));

        if (!username.equals(appointment.getCreatedByName()) && !username.equals(appointment.getCreatedForName())) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze afspraak");
        }

        return transferAppointmentToOutputDto(appointment);
    }

    public AppointmentOutputDto updateAppointment(Long appointmentId, AppointmentInputDto appointmentInputDto) throws RecordNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Afspraak niet gevonden"));

        if (!username.equals(appointment.getCreatedByName()) && !username.equals(appointment.getCreatedForName())) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze afspraak");
        }

        appointment.setDate(appointmentInputDto.getDate());
        appointment.setStartTime(appointmentInputDto.getStartTime());
        appointment.setEndTime(appointmentInputDto.getEndTime());
        appointment.setDescription(appointmentInputDto.getDescription());
        Appointment returnAppointment = appointmentRepository.save(appointment);

        return transferAppointmentToOutputDto(returnAppointment);
    }

    public String removeAppointment(Long appointmentId) throws RecordNotFoundException, AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isEmpty()) {
            throw new RecordNotFoundException("Afspraak niet gevonden");
        }

        Appointment appointment = optionalAppointment.get();
        String createdByName = appointment.getCreatedByName();
        String createdForName = appointment.getCreatedForName();

        if (!username.equals(createdByName) && !username.equals(createdForName)) {
            throw new AccessDeniedException("Je hebt geen toegang tot deze afspraak");
        }

        appointmentRepository.deleteById(appointmentId);
        return "Afspraak succesvol verwijderd";
    }

//    public void removeAppointment(@RequestBody Long appointmentId) throws RecordNotFoundException {
//        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
//        if (optionalAppointment.isEmpty()) {
//            throw new RecordNotFoundException("Afspraak niet gevonden");}
//        appointmentRepository.deleteById(appointmentId);
//    }

    public AppointmentOutputDto transferAppointmentToOutputDto(Appointment appointment) {
        AppointmentOutputDto appointmentOutputDto = new AppointmentOutputDto();
        appointmentOutputDto.date = appointment.getDate();
        appointmentOutputDto.startTime = appointment.getStartTime();
        appointmentOutputDto.endTime = appointment.getEndTime();
        appointmentOutputDto.description = appointment.getDescription();
        appointmentOutputDto.createdForName = appointment.getCreatedForName();
        appointmentOutputDto.createdByName = appointment.getCreatedByName();
        //todo nog match vanwege de activiteiten
        return appointmentOutputDto;
    }

    public Appointment transferInputDtoToAppointment(AppointmentInputDto appointmentInputDto) throws IllegalArgumentException {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentInputDto.getDate());
        appointment.setStartTime(appointmentInputDto.getStartTime());
        int value = appointmentInputDto.getStartTime().compareTo(appointmentInputDto.getEndTime());
        if (value >= 0) {
            throw new IllegalArgumentException("De eindtijd moet na de starttijd liggen.");
        }
        appointment.setEndTime(appointmentInputDto.getEndTime());
        appointment.setDescription(appointmentInputDto.getDescription());
        return appointment;
    }
}
