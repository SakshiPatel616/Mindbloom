package com.mindbloom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Anonymous journaling entry.
 * Content is stored as-is; in production, consider field-level encryption.
 */
@Entity
@Table(name = "journal_entries")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** Optional mood at time of writing */
    @Enumerated(EnumType.STRING)
    private MoodLog.MoodType mood;

    /** Tags like "anxiety", "work", "gratitude" */
    @Column(name = "tags")
    private String tags;

    /** AI-generated gentle reflection (optional, user-triggered) */
    @Column(name = "ai_reflection", columnDefinition = "TEXT")
    private String aiReflection;

    @Column(name = "is_private")
    @Builder.Default
    private boolean isPrivate = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
