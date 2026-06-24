// MessageRepository.java
package com.flightbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightbooking.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUserIdOrderByCreatedAtDesc(Integer userId);
    List<Message> findByUserIdAndIsReadOrderByCreatedAtDesc(Integer userId, Boolean isRead);
    List<Message> findByUserIdAndTypeOrderByCreatedAtDesc(Integer userId, String type);
    int countByUserIdAndIsRead(Integer userId, Boolean isRead);
}