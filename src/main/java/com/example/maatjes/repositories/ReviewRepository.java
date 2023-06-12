package com.example.maatjes.repositories;

import com.example.maatjes.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Match, Long> {
}
