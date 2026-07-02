package com.mindbloom.repository;

import com.mindbloom.entity.WellnessExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WellnessExerciseRepository extends JpaRepository<WellnessExercise, Long> {

    List<WellnessExercise> findByActiveTrue();

    List<WellnessExercise> findByTypeAndActiveTrue(
            WellnessExercise.ExerciseType type
    );

    @Query("SELECT w FROM WellnessExercise w WHERE w.active = true AND w.suitableForPaths LIKE %:path%")
    List<WellnessExercise> findByHealingPath(@Param("path") String path);
}