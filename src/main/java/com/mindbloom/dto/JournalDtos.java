package com.mindbloom.dto;

import com.mindbloom.entity.MoodLog;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter

@Builder
public class JournalDtos {
	 @Getter
	    @Setter
	    @NoArgsConstructor
	    @AllArgsConstructor
	    @Builder

    public static class JournalRequest {
        private String title;
        private String content;
        private MoodLog.MoodType mood;
        private String tags;
        private boolean requestAiReflection;
    }

	 @Getter
	    @Setter
	    @NoArgsConstructor
	    @AllArgsConstructor
	    @Builder
    public static class JournalResponse {
        private Long id;
        private String title;
        private String content;
        private MoodLog.MoodType mood;
        private String tags;
        private String aiReflection;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}