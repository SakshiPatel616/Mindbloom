package com.mindbloom.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;



@Getter @Setter @Builder
public class ChatDtos {

  
	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ChatRequest {

        @NotBlank(message = "Message cannot be empty")
        @Size(max = 2000)
        private String message;

        private String sessionId;  // ✅ ADD THIS
    }
	
	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder

    public static class ChatResponse {
        private String sessionId;
        private String message;
        private boolean crisisDetected;
        private List<CrisisResource> crisisResources;
        private LocalDateTime timestamp;
    }
	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CrisisResource {
        private String name;
        private String contact;
        private String description;
    }
	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ChatHistoryResponse {
        private String sessionId;
        private List<MessageDto> messages;
    }
	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class MessageDto {
        private String role;
        private String content;
        private boolean crisisFlagged;
        private LocalDateTime timestamp;
    }
}