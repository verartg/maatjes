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
import com.example.maatjes.util.SecurityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public AppointmentOutputDto createAppointment(AppointmentInputDto appointmentInputDto) throws RecordNotFoundException, BadRequestException, AccessDeniedException {
        Match match = matchRepository.findById(appointmentInputDto.getMatchId())
                .orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        String username1 = match.getHelpGiver().getUser().getUsername();
        String username2 = match.getHelpReceiver().getUser().getUsername();

        SecurityUtils.validateUsernames(username1, username2, "account");

        if (!match.isGiverAccepted() || !match.isReceiverAccepted()) {
            throw new BadRequestException("Match moet eerst worden geaccepteerd voordat een afspraak kan worden ingepland");
        }

        Appointment appointment = transferInputDtoToAppointment(appointmentInputDto);
        appointment.setMatch(match);
        appointment.setCreatedByName(SecurityContextHolder.getContext().getAuthentication().getName());
        appointment.setCreatedForName(SecurityContextHolder.getContext().getAuthentication().getName().equals(username1) ?
                username2 : username1);
        appointment = appointmentRepository.save(appointment);
        match.getAppointments().add(appointment);
        matchRepository.save(match);

        return transferAppointmentToOutputDto(appointment);
    }

    public List<AppointmentOutputDto> getAppointmentsByMatchId(Long matchId) throws RecordNotFoundException {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        String username1 = match.getHelpGiver().getUser().getUsername();
        String username2 = match.getHelpReceiver().getUser().getUsername();

        SecurityUtils.validateUsernames(username1, username2, "account");

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
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));

        String username = account.getUser().getUsername();

        SecurityUtils.validateUsername(username, "account");

        LocalDate currentDate = LocalDate.now();

        List<AppointmentOutputDto> appointmentOutputDtos = Stream.concat(
                        account.getMatchesWhereAccountIsHelpReceiver().stream().flatMap(match -> match.getAppointments().stream()),
                        account.getMatchesWhereAccountIsHelpGiver().stream().flatMap(match -> match.getAppointments().stream()))
                .filter(appointment -> appointment.getDate().isAfter(currentDate))
                .map(this::transferAppointmentToOutputDto)
                .collect(Collectors.toList());

        return appointmentOutputDtos;
    }

    public AppointmentOutputDto getAppointment(Long appointmentId) throws RecordNotFoundException, AccessDeniedException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Afspraak niet gevonden"));

        String username1 = appointment.getCreatedForName();
        String username2 = appointment.getCreatedByName();

        SecurityUtils.validateUsernames(username1, username2, "afspraken");

        return transferAppointmentToOutputDto(appointment);
    }

    public AppointmentOutputDto updateAppointment(Long appointmentId, AppointmentInputDto appointmentInputDto) throws RecordNotFoundException, AccessDeniedException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Afspraak niet gevonden"));

        String createdForName = appointment.getCreatedForName();
        String createdByName = appointment.getCreatedByName();

        SecurityUtils.validateUsernames(createdForName, createdByName, "afspraken");

        appointment = appointmentRepository.save(transferInputDtoToAppointment(appointment, appointmentInputDto));
        return transferAppointmentToOutputDto(appointment);
    }

    public String removeAppointment(Long appointmentId) throws RecordNotFoundException, AccessDeniedException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Afspraak niet gevonden"));

        String createdByName = appointment.getCreatedByName();
        String createdForName = appointment.getCreatedForName();

        SecurityUtils.validateUsernames(createdForName, createdByName, "afspraken");

        appointmentRepository.deleteById(appointmentId);
        return "Afspraak succesvol verwijderd";
    }

    public Appointment transferInputDtoToAppointment(AppointmentInputDto appointmentInputDto) throws IllegalArgumentException {
        Appointment appointment = new Appointment();
        return transferInputDtoToAppointment(appointment, appointmentInputDto);
    }

    public Appointment transferInputDtoToAppointment(Appointment appointment, AppointmentInputDto appointmentInputDto) throws IllegalArgumentException {
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

    public AppointmentOutputDto transferAppointmentToOutputDto(Appointment appointment) {
        AppointmentOutputDto appointmentOutputDto = new AppointmentOutputDto();
        appointmentOutputDto.id = appointment.getId();
        appointmentOutputDto.date = appointment.getDate();
        appointmentOutputDto.startTime = appointment.getStartTime();
        appointmentOutputDto.endTime = appointment.getEndTime();
        appointmentOutputDto.description = appointment.getDescription();
        appointmentOutputDto.createdForName = appointment.getCreatedForName();
        appointmentOutputDto.createdByName = appointment.getCreatedByName();
        return appointmentOutputDto;
    }

}
