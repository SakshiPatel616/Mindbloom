package com.mindbloom.service;

import com.mindbloom.dto.ExerciseDtos;
import com.mindbloom.dto.OnboardingDtos;
import com.mindbloom.entity.User;
import com.mindbloom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardingService {

    private final UserRepository userRepository;
    private final WellnessExerciseService exerciseService;

    @Transactional
    public OnboardingDtos.OnboardingResponse completeOnboarding(
            String email,
            OnboardingDtos.OnboardingRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save onboarding data
        user.setHealingPath(request.getHealingPath());
        user.setOnboardingCompleted(true);

        userRepository.save(user);

        // Default empty recommendations
        List<ExerciseDtos.ExerciseResponse> exercises = Collections.emptyList();

        // Try to fetch recommended exercises
        try {
            exercises = exerciseService.getRecommendedExercises(user.getHealingPath());
        } catch (Exception e) {
            log.warn("Unable to load recommended exercises: {}", e.getMessage());
        }

        return OnboardingDtos.OnboardingResponse.builder()
                .healingPath(user.getHealingPath())
                .welcomeMessage(getWelcomeMessage(user.getHealingPath()))
                .recommendedExercises(exercises)
                .build();
    }

    private String getWelcomeMessage(User.HealingPath path) {

        return switch (path) {

            case STRESS ->
                    "Welcome! Together we'll learn practical ways to reduce stress and create more calm in your daily life.";

            case ANXIETY ->
                    "You're not alone. We'll practice grounding techniques and build confidence one step at a time.";

            case SELF_ESTEEM ->
                    "Your journey toward self-kindness begins today. Every small step matters.";

            case BURNOUT ->
                    "It's okay to rest. We'll help you recover your energy and rebuild healthy routines.";

            case GRIEF ->
                    "Healing from loss takes time. We'll walk beside you with compassion and support.";

            case GENERAL ->
                    "Welcome to MindBloom! Let's build healthy habits that support your emotional well-being every day.";
        };
    }
}