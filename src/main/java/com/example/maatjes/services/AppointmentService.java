package com.example.maatjes.services;

import com.example.maatjes.dtos.AppointmentDto;
import com.example.maatjes.dtos.AppointmentInputDto;
import com.example.maatjes.exceptions.RecordNotFoundException;
import com.example.maatjes.models.Appointment;
import com.example.maatjes.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<AppointmentDto> getAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add(transferAppointmentToDto(appointment));
        }
        return appointmentDtos;
    }

    public AppointmentDto getAppointment(Long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            throw new RecordNotFoundException("Afspraak niet gevonden");}
        Appointment appointment = optionalAppointment.get();
        return transferAppointmentToDto(appointment);
        }

    public AppointmentDto saveAppointment(AppointmentInputDto appointmentDto) {
        Appointment appointment = transferToAppointment(appointmentDto);
        appointmentRepository.save(appointment);
        return transferAppointmentToDto(appointment);
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
            appointment1.setAccepted(newAppointment.isAccepted());
            Appointment returnAppointment = appointmentRepository.save(appointment1);
            return transferAppointmentToDto(returnAppointment);
        }
    }

    public AppointmentDto transferAppointmentToDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.id = appointment.getId();
        appointmentDto.date = appointment.getDate();
        appointmentDto.startTime = appointment.getStartTime();
        appointmentDto.endTime = appointment.getEndTime();
        appointmentDto.description = appointment.getDescription();
        appointmentDto.accepted = appointment.isAccepted();
        return appointmentDto;
    }

    public Appointment transferToAppointment(AppointmentInputDto appointmentDto) {
        var appointment = new Appointment();
        appointment.setDate(appointmentDto.getDate());
        appointment.setStartTime(appointmentDto.getStartTime());
        appointment.setEndTime(appointmentDto.getEndTime());
        appointment.setDescription(appointmentDto.getDescription());
        appointment.setAccepted(appointmentDto.isAccepted());
        return appointment;
    }

}
