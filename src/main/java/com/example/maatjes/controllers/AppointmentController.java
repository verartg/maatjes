package com.example.maatjes.controllers;

import com.example.maatjes.dtos.AppointmentDto;
import com.example.maatjes.dtos.AppointmentInputDto;
import com.example.maatjes.services.AccountService;
import com.example.maatjes.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AccountService accountService;

    public AppointmentController(AppointmentService appointmentService, AccountService accountService) {
        this.appointmentService = appointmentService;
        this.accountService = accountService;
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByMatchId(@PathVariable Long matchId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByMatchId(matchId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByAccountId(@PathVariable Long accountId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByAccountId(accountId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable("id") Long id) {
        AppointmentDto appointment = appointmentService.getAppointment(id);
        return ResponseEntity.ok().body(appointment);
    }

    @PostMapping("/addappointment")
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentInputDto appointmentInputDto) {
        return new ResponseEntity<>(appointmentService.createAppointment(appointmentInputDto), HttpStatus.CREATED);
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
