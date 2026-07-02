package com.mindbloom.controller;

import com.mindbloom.dto.*;
import com.mindbloom.service.MoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mood")
@RequiredArgsConstructor
@Tag(name = "Mood Tracking")
public class MoodController {

    private final MoodService moodService;

    @PostMapping
    public ApiResponse<MoodDtos.MoodLogResponse> logMood(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody MoodDtos.MoodLogRequest request) {

        return ApiResponse.success(
                "Mood logged 🌱",
                moodService.logMood(user.getUsername(), request));
    }

    @GetMapping("/summary")
    public ApiResponse<MoodDtos.MoodSummary> summary(
            @AuthenticationPrincipal UserDetails user) {

        return ApiResponse.success(
                moodService.getMoodSummary(user.getUsername()));
    }
}