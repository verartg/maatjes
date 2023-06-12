//package com.example.maatjes.services;
//
//import com.example.maatjes.exceptions.RecordNotFoundException;
//import com.example.maatjes.models.Appointment;
//import com.example.maatjes.models.Calendar;
//import com.example.maatjes.repositories.AppointmentRepository;
//import com.example.maatjes.repositories.CalendarRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CalendarService {
//    private final CalendarRepository calendarRepository;
//    private final AppointmentRepository appointmentRepository;
//
//    public CalendarService(CalendarRepository calendarRepository, AppointmentRepository appointmentRepository) {
//        this.calendarRepository = calendarRepository;
//        this.appointmentRepository = appointmentRepository;
//    }
//
//    public String assignAppointmentToCalendar(Long id, Long appointment_id) {
//        Optional<Calendar> calendarOptional = calendarRepository.findById(id);
//        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointment_id);
//        if (calendarOptional.isPresent() && appointmentOptional.isPresent()) {
//            Calendar calendar1 = calendarOptional.get();
//            Appointment appointment1 = appointmentOptional.get();
//            List<Appointment> appointmentList = calendar1.getAppointments();
//            appointmentList.add(appointment1);
//            calendar1.setAppointments(appointmentList);
//            calendarRepository.save(calendar1);
//            return "hoera";
//        } else {
//            throw new RecordNotFoundException("Kalender en/of afspraak niet gevonden");
//        }
//    }
//}
