package com.example.maatjes.repositories;

import com.example.maatjes.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByCityEqualsIgnoreCase(String city);
    List<Account> findAllByCityAndGivesHelp(String city, Boolean givesHelp);
    List<Account> findAllByGivesHelp(Boolean givesHelp);
}

