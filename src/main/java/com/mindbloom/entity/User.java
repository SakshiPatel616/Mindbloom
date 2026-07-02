package com.mindbloom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Core user entity.
 * Users may choose to remain anonymous (displayName only, no real name stored).
 */
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    /** Display name – can be a pseudonym to protect privacy */
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    /** Chosen healing path */
    @Enumerated(EnumType.STRING)
    @Column(name = "healing_path")
    private HealingPath healingPath;

    @Column(name = "anonymous_mode")
    @Builder.Default
    private boolean anonymousMode = false;

    @Column(name = "onboarding_completed")
    @Builder.Default
    private boolean onboardingCompleted = false;

    @Column(name = "profile_avatar")
    private String profileAvatar;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<JournalEntry> journalEntries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MoodLog> moodLogs = new ArrayList<>();

    public enum Role { USER, ADMIN, PROFESSIONAL }

    public enum HealingPath {
        STRESS("Managing Stress"),
        ANXIETY("Overcoming Anxiety"),
        SELF_ESTEEM("Building Self-Esteem"),
        BURNOUT("Recovering from Burnout"),
        GRIEF("Processing Grief"),
        GENERAL("General Wellness");

        private final String displayName;
        HealingPath(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }
}
