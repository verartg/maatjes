//package com.example.maatjes.models;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import org.hibernate.annotations.LazyCollection;
//import org.hibernate.annotations.LazyCollectionOption;
//
//import java.util.List;
//
//@Entity
//public class Calendar {
//    @Id
//    @GeneratedValue
//    private Long id;
//
//    @OneToMany(mappedBy = "calendar")
//    @LazyCollection(LazyCollectionOption.TRUE)
//    private List<Appointment> appointments;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public List<Appointment> getAppointments() {
//        return appointments;
//    }
//
//    public void setAppointments(List<Appointment> appointments) {
//        this.appointments = appointments;
//    }
//}
