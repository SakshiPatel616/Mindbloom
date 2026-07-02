package com.mindbloom.repository;

import com.mindbloom.entity.ChatMessage;
import com.mindbloom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByUserAndSessionIdOrderByCreatedAtAsc(
            User user,
            String sessionId
    );

    @Query("SELECT DISTINCT c.sessionId FROM ChatMessage c WHERE c.user = :user")
    List<String> findSessionIdsByUser(@Param("user") User user);

    long countByUserAndCrisisFlaggedTrue(User user);
}