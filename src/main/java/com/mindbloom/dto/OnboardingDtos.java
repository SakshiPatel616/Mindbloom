package com.mindbloom.dto;

import com.mindbloom.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter

@Builder
public class OnboardingDtos {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OnboardingRequest {

        @NotNull
        private User.HealingPath healingPath;

        private String primaryConcern;

        private List<String> goals;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OnboardingResponse {

        private User.HealingPath healingPath;

        private String welcomeMessage;

        private List<ExerciseDtos.ExerciseResponse> recommendedExercises;
    }
}