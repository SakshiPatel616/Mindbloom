package com.mindbloom.controller;

import com.mindbloom.dto.*;
import com.mindbloom.entity.WellnessExercise;
import com.mindbloom.service.WellnessExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
@Tag(name = "Exercises")
public class ExerciseController {

    private final WellnessExerciseService exerciseService;

    @GetMapping
    public ApiResponse<List<ExerciseDtos.ExerciseResponse>> getAll() {
        return ApiResponse.success(exerciseService.getAll());
    }

    @GetMapping("/type/{type}")
    public ApiResponse<List<ExerciseDtos.ExerciseResponse>> getByType(
            @PathVariable WellnessExercise.ExerciseType type) {

        return ApiResponse.success(exerciseService.getByType(type));
    }

    @GetMapping("/recommended")
    public ApiResponse<List<ExerciseDtos.ExerciseResponse>> recommended(
            @AuthenticationPrincipal UserDetails user) {

        return ApiResponse.success(
                exerciseService.getRecommended(user.getUsername()));
    }
}