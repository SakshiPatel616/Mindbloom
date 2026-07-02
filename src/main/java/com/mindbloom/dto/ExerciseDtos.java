package com.mindbloom.dto;

import com.mindbloom.entity.WellnessExercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class ExerciseDtos {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExerciseResponse {

        private Long id;

        private String title;

        private String description;

        private WellnessExercise.ExerciseType type;

        private String typeLabel;

        private Integer durationMinutes;

        private String steps;

        private Integer difficultyLevel;

        private String audioUrl;
    }

}