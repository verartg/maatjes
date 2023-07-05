package com.example.maatjes.repositories;

import com.example.maatjes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
