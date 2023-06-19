package com.example.maatjes.controllers;

import com.example.maatjes.dtos.AppointmentDto;
import com.example.maatjes.dtos.AppointmentInputDto;
import com.example.maatjes.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAppointments() {
        List<AppointmentDto> appointmentDtos;
        appointmentDtos = appointmentService.getAppointments();
        return ResponseEntity.ok().body(appointmentDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable("id") Long id) {
        AppointmentDto appointment = appointmentService.getAppointment(id);
        return ResponseEntity.ok().body(appointment);
    }

    @PostMapping("/addappointment")
    public ResponseEntity<AppointmentDto> saveAppointment(@RequestBody AppointmentInputDto appointment) {
        AppointmentDto dto = appointmentService.saveAppointment(appointment);
        return ResponseEntity.created(null).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeAppointment(@PathVariable Long id) {
        appointmentService.removeAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentInputDto appointment) {
        AppointmentDto dto = appointmentService.updateAppointment(id, appointment);
        return ResponseEntity.ok().body(dto);
    }
}
