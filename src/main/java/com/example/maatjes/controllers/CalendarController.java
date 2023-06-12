//package com.example.maatjes.controllers;

//import com.example.maatjes.services.CalendarService;
//import org.apache.coyote.Response;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/calendar")
//public class CalendarController {
//    private final CalendarService calendarService;
//
//    public CalendarController(CalendarService calendarService) {
//        this.calendarService = calendarService;
//    }
//
//    @PutMapping("/{id}/appointment/{appointment_id}")
//    public ResponseEntity<String> assignAppointmentToCalendar(@PathVariable Long id, @PathVariable Long appointment_id) {
//        return ResponseEntity.ok(calendarService.assignAppointmentToCalendar(id, appointment_id));
//    }
//
//
//    //Nog methode nodig om
//}
