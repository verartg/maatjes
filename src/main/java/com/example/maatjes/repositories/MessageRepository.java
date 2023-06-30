package com.example.maatjes.repositories;

import com.example.maatjes.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByCreatedAtDateBefore(LocalDate date);
}
