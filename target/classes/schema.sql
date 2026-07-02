-- ============================================================
--  MindBloom Database Schema
--  Run this manually or let Hibernate create it via ddl-auto=update
-- ============================================================

CREATE DATABASE IF NOT EXISTS mindbloom_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE mindbloom_db;

CREATE TABLE IF NOT EXISTS users (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    email                VARCHAR(255) NOT NULL UNIQUE,
    password             VARCHAR(255) NOT NULL,
    display_name         VARCHAR(100) NOT NULL,
    role                 ENUM('USER','ADMIN','PROFESSIONAL') DEFAULT 'USER',
    healing_path         ENUM('STRESS','ANXIETY','SELF_ESTEEM','BURNOUT','GRIEF','GENERAL'),
    anonymous_mode       BOOLEAN DEFAULT FALSE,
    onboarding_completed BOOLEAN DEFAULT FALSE,
    profile_avatar       VARCHAR(500),
    last_active_at       DATETIME,
    created_at           DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mood_logs (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    mood       ENUM('JOYFUL','CALM','NEUTRAL','ANXIOUS','SAD','ANGRY','OVERWHELMED','HOPEFUL','EXHAUSTED','GRATEFUL') NOT NULL,
    intensity  TINYINT NOT NULL CHECK (intensity BETWEEN 1 AND 10),
    note       TEXT,
    log_date   DATE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_date (user_id, log_date),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS journal_entries (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    title         VARCHAR(200) NOT NULL,
    content       TEXT NOT NULL,
    mood          ENUM('JOYFUL','CALM','NEUTRAL','ANXIOUS','SAD','ANGRY','OVERWHELMED','HOPEFUL','EXHAUSTED','GRATEFUL'),
    tags          VARCHAR(500),
    ai_reflection TEXT,
    is_private    BOOLEAN DEFAULT TRUE,
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chat_messages (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    session_id    VARCHAR(36) NOT NULL,
    role          ENUM('USER','ASSISTANT') NOT NULL,
    content       TEXT NOT NULL,
    crisis_flagged BOOLEAN DEFAULT FALSE,
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_session (user_id, session_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS wellness_exercises (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    title               VARCHAR(200) NOT NULL,
    description         TEXT,
    type                ENUM('BREATHING','GROUNDING','MEDITATION','CBT_REFRAME','BODY_SCAN','JOURNALING_PROMPT','AFFIRMATION') NOT NULL,
    duration_minutes    INT,
    steps               JSON,
    suitable_for_paths  VARCHAR(200),
    difficulty_level    TINYINT DEFAULT 1,
    audio_url           VARCHAR(500),
    is_active           BOOLEAN DEFAULT TRUE,
    INDEX idx_type (type),
    INDEX idx_active (is_active)
);
