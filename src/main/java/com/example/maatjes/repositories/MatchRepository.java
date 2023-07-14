package com.example.maatjes.repositories;

import com.example.maatjes.enums.ContactPerson;
import com.example.maatjes.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAllByEndMatchBefore(LocalDate currentDate);
    List<Match> findAllByContactPerson(ContactPerson contactPerson);
}
