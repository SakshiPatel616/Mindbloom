package com.mindbloom.repository;

import com.mindbloom.entity.MoodLog;
import com.mindbloom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {

    Optional<MoodLog> findByUserAndLogDate(User user, LocalDate logDate);

    List<MoodLog> findByUserAndLogDateBetweenOrderByLogDateDesc(
            User user,
            LocalDate from,
            LocalDate to
    );

    @Query("SELECT m.mood, COUNT(m) FROM MoodLog m WHERE m.user = :user GROUP BY m.mood ORDER BY COUNT(m) DESC")
    List<Object[]> findMoodFrequency(@Param("user") User user);

    @Query("SELECT AVG(m.intensity) FROM MoodLog m WHERE m.user = :user AND m.logDate >= :since")
    Double findAverageIntensitySince(@Param("user") User user,
                                     @Param("since") LocalDate since);
}