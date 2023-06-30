package com.example.maatjes.repositories;

import com.example.maatjes.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
//    Message save(Message message);
//    List<Message> findByMatchId(Long matchId);
//    List<Message> findLastTwentyByMatchId(Long matchId);
//    void deleteOlderThanOneMonth();
}
