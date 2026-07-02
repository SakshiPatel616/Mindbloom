package com.mindbloom.service;

import com.mindbloom.dto.ExerciseDtos;
import com.mindbloom.entity.User;
import com.mindbloom.entity.WellnessExercise;
import com.mindbloom.repository.UserRepository;
import com.mindbloom.repository.WellnessExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WellnessExerciseService {

    private final WellnessExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    /**
     * Get all exercises.
     */
    public List<ExerciseDtos.ExerciseResponse> getAll() {

        return exerciseRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * Get exercises by type.
     */
    public List<ExerciseDtos.ExerciseResponse> getByType(
            WellnessExercise.ExerciseType type) {

    	return exerciseRepository.findByTypeAndActiveTrue(type)
    	        .stream()
    	        .map(this::convertToDto)
    	        .toList();
    }

    /**
     * Get recommended exercises for a user.
     */
    public List<ExerciseDtos.ExerciseResponse> getRecommended(
            String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Later you can filter by healing path
        return getRecommendedExercises(user.getHealingPath());
    }

    /**
     * Get recommended exercises by healing path.
     */
    public List<ExerciseDtos.ExerciseResponse> getRecommendedExercises(
            User.HealingPath healingPath) {

        // TODO: Filter based on healingPath.
        // For now return all exercises.

        return exerciseRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    
    

    /**
     * Convert entity to DTO.
     */
    private ExerciseDtos.ExerciseResponse convertToDto(
            WellnessExercise exercise) {

        return ExerciseDtos.ExerciseResponse.builder()
                .id(exercise.getId())
                .title(exercise.getTitle())
                .description(exercise.getDescription())
                .type(exercise.getType())
                .typeLabel(exercise.getType().name())
                .durationMinutes(exercise.getDurationMinutes())
                .steps(exercise.getSteps())
                .difficultyLevel(exercise.getDifficultyLevel())
                .audioUrl(exercise.getAudioUrl())
                .build();
    }
}