package com.mindbloom.repository;

import com.mindbloom.entity.JournalEntry;
import com.mindbloom.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    Page<JournalEntry> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Optional<JournalEntry> findByIdAndUser(Long id, User user);

    long countByUser(User user);

    List<JournalEntry> findByUserAndCreatedAtAfterOrderByCreatedAtDesc(
            User user,
            LocalDateTime since
    );
}