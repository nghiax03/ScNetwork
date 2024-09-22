package com.nghianguyen.scnetwork.repositories;

import com.nghianguyen.scnetwork.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverId(Long receiverId);
    List<Message> findBySenderId(Long senderId);
}
