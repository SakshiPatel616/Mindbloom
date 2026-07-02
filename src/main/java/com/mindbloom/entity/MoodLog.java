package com.mindbloom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Daily mood check-in log.
 */
@Entity
@Table(name = "mood_logs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "log_date"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MoodLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MoodType mood;

    /** 1–10 intensity scale */
    @Column(nullable = false)
    private Integer intensity;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum MoodType {
        JOYFUL("😄", "Joyful"),
        CALM("😌", "Calm"),
        NEUTRAL("😐", "Neutral"),
        ANXIOUS("😰", "Anxious"),
        SAD("😢", "Sad"),
        ANGRY("😤", "Angry"),
        OVERWHELMED("😩", "Overwhelmed"),
        HOPEFUL("🌱", "Hopeful"),
        EXHAUSTED("😴", "Exhausted"),
        GRATEFUL("🙏", "Grateful");

        private final String emoji;
        private final String label;

        MoodType(String emoji, String label) {
            this.emoji = emoji;
            this.label = label;
        }

        public String getEmoji() { return emoji; }
        public String getLabel() { return label; }
    }
}
