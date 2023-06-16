package com.example.maatjes.repositories;

import com.example.maatjes.models.AccountMatch;
import com.example.maatjes.models.AccountMatchKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountMatchRepository extends JpaRepository<AccountMatch, AccountMatchKey> {
    List<AccountMatch> findAllByAccountId(Long accountId);
    List<AccountMatch> findAllByMatchId(Long matchId);
}