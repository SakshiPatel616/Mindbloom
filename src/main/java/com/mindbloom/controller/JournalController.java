package com.mindbloom.controller;

import com.mindbloom.dto.*;
import com.mindbloom.service.JournalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
@Tag(name = "Journaling")
public class JournalController {

    private final JournalService journalService;

    @PostMapping
    public ApiResponse<JournalDtos.JournalResponse> create(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody JournalDtos.JournalRequest request) {

        return ApiResponse.success(
                "Saved 🌿",
                journalService.create(user.getUsername(), request));
    }

    @GetMapping
    public ApiResponse<Page<JournalDtos.JournalResponse>> getAll(
            @AuthenticationPrincipal UserDetails user,
            Pageable pageable) {

        return ApiResponse.success(
                journalService.getAll(user.getUsername(), pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<JournalDtos.JournalResponse> getById(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {

        return ApiResponse.success(
                journalService.getById(user.getUsername(), id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {

        journalService.delete(user.getUsername(), id);
        return ApiResponse.success("Deleted", null);
    }
}