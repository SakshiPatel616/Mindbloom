package com.mindbloom.controller;

import com.mindbloom.dto.*;
import com.mindbloom.service.OnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
@Tag(name = "Onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping
    public ApiResponse<OnboardingDtos.OnboardingResponse> complete(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody OnboardingDtos.OnboardingRequest request) {

        return ApiResponse.success(
                onboardingService.completeOnboarding(user.getUsername(), request));
    }
}