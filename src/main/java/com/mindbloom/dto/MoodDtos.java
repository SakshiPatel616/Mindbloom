package com.mindbloom.dto;

import com.mindbloom.entity.MoodLog;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter  @Builder
public class MoodDtos {
	
	 @Getter
	    @Setter
	    @NoArgsConstructor
	    @AllArgsConstructor
	    @Builder

    public static class MoodLogRequest {
        @NotNull(message = "Mood is required")
        private MoodLog.MoodType mood;

        @NotNull
        @Min(1) @Max(10)
        private Integer intensity;

        @Size(max = 500)
        private String note;
    }
	 
	 @Getter
	    @Setter
	    @NoArgsConstructor
	    @AllArgsConstructor
	    @Builder

    public static class MoodLogResponse {
        private Long id;
        private MoodLog.MoodType mood;
        private String moodLabel;
        private String moodEmoji;
        private Integer intensity;
        private String note;
        private LocalDate logDate;
        private LocalDateTime createdAt;
    }
	 
	 @Getter
	    @Setter
	    @NoArgsConstructor
	    @AllArgsConstructor
	    @Builder

    public static class MoodSummary {
        private List<MoodLogResponse> last7Days;
        private MoodLog.MoodType dominantMood;
        private double averageIntensity;
        private String insight;
    }
}