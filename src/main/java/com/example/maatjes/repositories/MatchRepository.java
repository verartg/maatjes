package com.example.maatjes.repositories;

import com.example.maatjes.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
//    List<Match> findAllByAccountId(Long accountId);
}
