package com.mindbloom.controller;

import com.mindbloom.dto.*;
import com.mindbloom.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Tag(name = "AI Companion")
public class ChatController {

    private final AiChatService aiChatService;

    @PostMapping
    @Operation(summary = "Send message to AI")
    public ApiResponse<ChatDtos.ChatResponse> chat(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody ChatDtos.ChatRequest request) {

        return ApiResponse.success(
                aiChatService.chat(user.getUsername(), request));
    }

    @GetMapping("/history/{sessionId}")
    public ApiResponse<ChatDtos.ChatHistoryResponse> history(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String sessionId) {

        return ApiResponse.success(
                aiChatService.getHistory(user.getUsername(), sessionId));
    }
}