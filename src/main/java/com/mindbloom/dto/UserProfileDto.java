package com.mindbloom.dto;

import com.mindbloom.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfileDto {
    private Long id;
    private String displayName;
    private String email;
    private User.HealingPath healingPath;
    private boolean anonymousMode;
    private boolean onboardingCompleted;
    private LocalDateTime createdAt;
}