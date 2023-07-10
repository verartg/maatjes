package com.example.maatjes.controllers;

import com.example.maatjes.dtos.outputDtos.AppointmentOutputDto;
import com.example.maatjes.dtos.inputDtos.AppointmentInputDto;
import com.example.maatjes.services.AppointmentService;
import com.example.maatjes.util.FieldErrorHandling;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/addappointment")
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody AppointmentInputDto appointmentInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(appointmentService.createAppointment(appointmentInputDto), HttpStatus.CREATED);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<AppointmentOutputDto>> getAppointmentsByMatchId(@PathVariable Long matchId) {
        List<AppointmentOutputDto> appointments = appointmentService.getAppointmentsByMatchId(matchId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<List<AppointmentOutputDto>> getAppointmentsByAccountId(@PathVariable Long accountId) {
        List<AppointmentOutputDto> appointments = appointmentService.getAppointmentsByAccountId(accountId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentOutputDto> getAppointment(@PathVariable Long appointmentId) {
        AppointmentOutputDto appointment = appointmentService.getAppointment(appointmentId);
        return ResponseEntity.ok().body(appointment);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<Object> updateAppointment(@PathVariable Long appointmentId, @Valid @RequestBody AppointmentInputDto appointmentInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(appointmentService.updateAppointment(appointmentId, appointmentInputDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<String> removeAppointment(@PathVariable Long appointmentId) {
        String message = appointmentService.removeAppointment(appointmentId);
        return ResponseEntity.ok().body(message);
    }
}
