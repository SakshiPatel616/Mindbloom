package com.mindbloom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Guided wellness exercises: breathing, grounding, meditation, CBT prompts.
 */
@Entity
@Table(name = "wellness_exercises")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class WellnessExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseType type;

    /** Duration in minutes */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /** JSON array of steps */
    @Column(columnDefinition = "JSON")
    private String steps;

    /** Suitable healing paths (comma-separated) */
    @Column(name = "suitable_for_paths")
    private String suitableForPaths;

    @Column(name = "difficulty_level")
    @Builder.Default
    private Integer difficultyLevel = 1;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    public enum ExerciseType {
        BREATHING("Breathing Exercise"),
        GROUNDING("Grounding Technique"),
        MEDITATION("Guided Meditation"),
        CBT_REFRAME("CBT Thought Reframing"),
        BODY_SCAN("Body Scan"),
        JOURNALING_PROMPT("Journaling Prompt"),
        AFFIRMATION("Positive Affirmation");

        private final String label;
        ExerciseType(String label) { this.label = label; }
        public String getLabel() { return label; }
    }
}
