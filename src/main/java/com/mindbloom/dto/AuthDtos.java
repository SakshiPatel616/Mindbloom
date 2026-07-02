package com.mindbloom.dto;

import com.mindbloom.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter  @Builder
public class AuthDtos {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RegisterRequest {
        @Email(message = "Please provide a valid email")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;

        @NotBlank(message = "Display name is required")
        @Size(min = 2, max = 50, message = "Display name must be 2–50 characters")
        private String displayName;

        private boolean anonymousMode;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class LoginRequest {
        @NotBlank private String email;
        @NotBlank private String password;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AuthResponse {
        private String accessToken;
        private String tokenType = "Bearer";
        private UserProfileDto user;
    }
}