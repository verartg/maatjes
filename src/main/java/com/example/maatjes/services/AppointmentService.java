package com.example.maatjes.services;

import com.example.maatjes.dtos.AppointmentOutputDto;
import com.example.maatjes.dtos.AppointmentInputDto;
import com.example.maatjes.exceptions.AccountNotAssociatedException;
import com.example.maatjes.exceptions.IllegalArgumentException;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Account;
import com.example.maatjes.models.Appointment;
import com.example.maatjes.models.Match;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.AppointmentRepository;
import com.example.maatjes.repositories.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
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

    public List<AppointmentOutputDto> getAppointmentsByMatchId(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));

        List<Appointment> appointments = match.getAppointments();
        List<AppointmentOutputDto> appointmentOutputDtos = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (Appointment appointment : appointments) {
            if (appointment.getDate().isAfter(currentDate)) {
                appointmentOutputDtos.add(transferAppointmentToOutputDto(appointment));
            }
        }
        return appointmentOutputDtos;
    }

    public List<AppointmentOutputDto> getAppointmentsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RecordNotFoundException("Account niet gevonden"));

        List<AppointmentOutputDto> appointmentOutputDtos = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        List<Match> matchesHelpReceiving = account.getHelpReceivers();
        for (Match match : matchesHelpReceiving) {
            List<Appointment> appointments = match.getAppointments();
            for (Appointment appointment : appointments) {
                if (appointment.getDate().isAfter(currentDate)) {
                    appointmentOutputDtos.add(transferAppointmentToOutputDto(appointment));
                }
            }
        }

        List<Match> matchesHelpGiving = account.getHelpGivers();
        for (Match match : matchesHelpGiving) {
            List<Appointment> appointments = match.getAppointments();
            for (Appointment appointment : appointments) {
                if (appointment.getDate().isAfter(currentDate)) {
                    appointmentOutputDtos.add(transferAppointmentToOutputDto(appointment));
                }
            }
        }
        return appointmentOutputDtos;
    }


    public AppointmentOutputDto getAppointment(Long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            throw new RecordNotFoundException("Afspraak niet gevonden");}
        Appointment appointment = optionalAppointment.get();
        return transferAppointmentToOutputDto(appointment);
        }

        //todo na authentication de check verwerken over wie de appointment maakt. zie request eronder
    public AppointmentOutputDto createAppointment(AppointmentInputDto appointmentInputDto) throws RecordNotFoundException, AccountNotAssociatedException, IllegalArgumentException {
        // Retrieve the Match based on the matchId in the inputDto
        Match match = matchRepository.findById(appointmentInputDto.getMatchId())
                .orElseThrow(() -> new RecordNotFoundException("Match niet gevonden"));
        if (!match.isGiverAccepted() || !match.isReceiverAccepted()) {
            throw new AccountNotAssociatedException("Match moet eerst worden geaccepteerd voordat een afspraak kan worden ingepland");}

        Appointment appointment = transferInputDtoToAppointment(appointmentInputDto);

            // Set the Match and Account references
            appointment.setMatch(match);

            //todo if principle is helpgiver
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

    public AppointmentOutputDto updateAppointment(Long id, AppointmentInputDto newAppointment) {
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

    public AppointmentOutputDto transferAppointmentToOutputDto(Appointment appointment) {
        AppointmentOutputDto appointmentOutputDto = new AppointmentOutputDto();
        appointmentOutputDto.id = appointment.getId();
        appointmentOutputDto.date = appointment.getDate();
        appointmentOutputDto.startTime = appointment.getStartTime();
        appointmentOutputDto.endTime = appointment.getEndTime();
        appointmentOutputDto.description = appointment.getDescription();
        appointmentOutputDto.createdForName = appointment.getCreatedForName();
        appointmentOutputDto.createdByName = appointment.getCreatedByName();
        //nog match vanwege de activiteiten
        return appointmentOutputDto;
    }

    public Appointment transferInputDtoToAppointment(AppointmentInputDto appointmentInputDto) {
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
