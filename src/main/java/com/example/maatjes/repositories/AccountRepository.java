package com.example.maatjes.repositories;

import com.example.maatjes.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllAccountsByCityEqualsIgnoreCase(String city);
}
