package com.example.maatjes.services;

import com.example.maatjes.dtos.AppointmentDto;
import com.example.maatjes.dtos.AppointmentInputDto;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Appointment;
import com.example.maatjes.models.Match;
import com.example.maatjes.repositories.AccountRepository;
import com.example.maatjes.repositories.AppointmentRepository;
import com.example.maatjes.repositories.MatchRepository;
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

    public List<AppointmentDto> getAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(transferAppointmentToOutputDto(appointment));
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

    public AppointmentDto createAppointment(AppointmentInputDto appointmentInputDto) throws RecordNotFoundException {
        // Retrieve the Match based on the matchId in the inputDto
        Match match = matchRepository.findById(appointmentInputDto.getMatchId())
                .orElseThrow(() -> new RecordNotFoundException("Match not found"));

        Appointment appointment = transferInputDtoToAppointment(appointmentInputDto);
        appointment.setDate(appointmentInputDto.getDate());
        appointment.setStartTime(appointmentInputDto.getStartTime());
        appointment.setEndTime(appointmentInputDto.getEndTime());
        appointment.setDescription(appointmentInputDto.getDescription());

        // Set the Match and Account references
        appointment.setMatch(match);

        if (match.getHelpGiver().getId().equals(appointmentInputDto.getCreatedById())) {
            appointment.setCreatedBy(match.getHelpGiver());
            appointment.setCreatedFor(match.getHelpReceiver());
        } else if (match.getHelpReceiver().getId().equals(appointmentInputDto.getCreatedById())) {
            appointment.setCreatedBy(match.getHelpReceiver());
            appointment.setCreatedFor(match.getHelpGiver());
        } else {
            throw new RuntimeException("Invalid account ID for creating the appointment");
        }

        // Save the Appointment and update the Match's appointments list
        //save the Match and update the Accounts Matches list
        appointment = appointmentRepository.save(appointment);
        match.getAppointments().add(appointment);
        matchRepository.save(match);

        return transferAppointmentToOutputDto(appointment);
    }

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
            //nog match
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
        appointmentDto.createdForName = appointment.getCreatedFor().getName();
        appointmentDto.createdByName = appointment.getCreatedBy().getName();
        //nog match
        return appointmentDto;
    }

    public Appointment transferInputDtoToAppointment(AppointmentInputDto appointmentInputDto) {
        var appointment = new Appointment();
        appointment.setDate(appointmentInputDto.getDate());
        appointment.setStartTime(appointmentInputDto.getStartTime());
        appointment.setEndTime(appointmentInputDto.getEndTime());
        appointment.setDescription(appointmentInputDto.getDescription());
        //nog match
        return appointment;
    }

}
