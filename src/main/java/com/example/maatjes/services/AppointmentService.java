package com.example.maatjes.services;

import com.example.maatjes.dtos.AppointmentDto;
import com.example.maatjes.dtos.AppointmentInputDto;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Appointment;
import com.example.maatjes.models.Match;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.AppointmentRepository;
import com.example.maatjes.repositories.MatchRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<AppointmentDto> getAppointmentsByMatchId(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RecordNotFoundException("Match not found"));

        List<Appointment> appointments = match.getAppointments();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(transferAppointmentToOutputDto(appointment));
        }
        return appointmentDtos;
    }

    public List<AppointmentDto> getAppointmentsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RecordNotFoundException("Account not found"));

        List<AppointmentDto> appointmentDtos = new ArrayList<>();

        List<Match> matchesHelpReceiving = account.getHelpReceivers();
        for (Match match : matchesHelpReceiving) {
            List<Appointment> appointments = match.getAppointments();
            for (Appointment appointment : appointments) {
                appointmentDtos.add(transferAppointmentToOutputDto(appointment));
            }
        }

        List<Match> matchesHelpGiving = account.getHelpGivers();
        for (Match match : matchesHelpGiving) {
            List<Appointment> appointments = match.getAppointments();
            for (Appointment appointment : appointments) {
                appointmentDtos.add(transferAppointmentToOutputDto(appointment));
            }
        }
        return appointmentDtos;
    }


    public AppointmentDto getAppointment(Long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            throw new RecordNotFoundException("Afspraak niet gevonden");}
        Appointment appointment = optionalAppointment.get();
        return transferAppointmentToOutputDto(appointment);
        }

        //todo na authentication de check verwerken over wie de appointment maakt. zie request eronder
    public AppointmentDto createAppointment(AppointmentInputDto appointmentInputDto) throws RecordNotFoundException {
        // Retrieve the Match based on the matchId in the inputDto
        Match match = matchRepository.findById(appointmentInputDto.getMatchId())
                .orElseThrow(() -> new RecordNotFoundException("Match not found"));
        if (!match.isGiverAccepted() || !match.isReceiverAccepted()) {
            throw new RecordNotFoundException("Match moet eerst worden geaccepteerd voordat een afspraak kan worden ingepland");}

            Appointment appointment = transferInputDtoToAppointment(appointmentInputDto);
            appointment.setDate(appointmentInputDto.getDate());
            appointment.setStartTime(appointmentInputDto.getStartTime());
            appointment.setEndTime(appointmentInputDto.getEndTime());
            appointment.setDescription(appointmentInputDto.getDescription());

            // Set the Match and Account references
            appointment.setMatch(match);

            //if principle is helpgiver
            appointment.setCreatedForName(match.getHelpReceiver().getName());
            appointment.setCreatedByName(match.getHelpGiver().getName());
            //if principle is helpreceiver
//        appointment.setCreatedForName(match.getHelpGiver().getName());
//        appointment.setCreatedByName(match.getHelpReceiver().getName());


        // Save the Appointment and update the Match's appointments list
        //save the Match and update the Accounts Matches list
        appointment = appointmentRepository.save(appointment);
        match.getAppointments().add(appointment);
        matchRepository.save(match);

        return transferAppointmentToOutputDto(appointment);
    }

//    public AppointmentDto createAppointment(AppointmentInputDto appointmentInputDto) throws RecordNotFoundException {
//        // Retrieve the Match based on the matchId in the inputDto
//        Match match = matchRepository.findById(appointmentInputDto.getMatchId())
//                .orElseThrow(() -> new RecordNotFoundException("Match not found"));
//
//        Account createdById = determineLoggedInAccount();
//        Account createdForId = determineCreatedForId(createdById, match);
//
//        Appointment appointment = transferInputDtoToAppointment(appointmentInputDto);
//        appointment.setDate(appointmentInputDto.getDate());
//        appointment.setStartTime(appointmentInputDto.getStartTime());
//        appointment.setEndTime(appointmentInputDto.getEndTime());
//        appointment.setDescription(appointmentInputDto.getDescription());
//
//        // Set the Match and Account references
//        appointment.setMatch(match);
//        appointment.setCreatedBy(createdById);
//        appointment.setCreatedFor(createdForId);
//
//        // Save the Appointment and update the Match's appointments list
//        appointment = appointmentRepository.save(appointment);
//        match.getAppointments().add(appointment);
//        matchRepository.save(match);
//
//        return transferAppointmentToOutputDto(appointment);
//    }
//
//    private Account determineLoggedInAccount() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof Account) {
//            return (Account) authentication.getPrincipal();
//        }
//        throw new RuntimeException("No logged-in account found");
//    }

    public void removeAppointment(@RequestBody Long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            throw new RecordNotFoundException("Afspraak niet gevonden");}
        appointmentRepository.deleteById(id);
    }

    public AppointmentDto updateAppointment(Long id, AppointmentInputDto newAppointment) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            throw new RecordNotFoundException("Afspraak niet gevonden");
        } else {
            Appointment appointment1 = optionalAppointment.get();
            appointment1.setDate(newAppointment.getDate());
            appointment1.setStartTime(newAppointment.getStartTime());
            appointment1.setEndTime(newAppointment.getEndTime());
            appointment1.setDescription(newAppointment.getDescription());
            Appointment returnAppointment = appointmentRepository.save(appointment1);
            return transferAppointmentToOutputDto(returnAppointment);
        }
    }

    public AppointmentDto transferAppointmentToOutputDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.id = appointment.getId();
        appointmentDto.date = appointment.getDate();
        appointmentDto.startTime = appointment.getStartTime();
        appointmentDto.endTime = appointment.getEndTime();
        appointmentDto.description = appointment.getDescription();
        appointmentDto.createdForName = appointment.getCreatedForName();
        appointmentDto.createdByName = appointment.getCreatedByName();
        //nog match vanwege de activiteiten
        return appointmentDto;
    }

    public Appointment transferInputDtoToAppointment(AppointmentInputDto appointmentInputDto) {
        var appointment = new Appointment();
        appointment.setDate(appointmentInputDto.getDate());
        appointment.setStartTime(appointmentInputDto.getStartTime());
        appointment.setEndTime(appointmentInputDto.getEndTime());
        appointment.setDescription(appointmentInputDto.getDescription());
        return appointment;
    }
}
