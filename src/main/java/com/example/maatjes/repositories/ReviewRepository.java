package com.example.maatjes.repositories;

import com.example.maatjes.models.Account;
import com.example.maatjes.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByWrittenBy(Account writtenBy);
    List<Review> findByWrittenFor(Account writtenFor);
}
