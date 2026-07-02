# 🌱 MindBloom — Mental Wellness Platform

> *"We are not here to fix you. We are here to walk alongside you."*

MindBloom is a compassionate, AI-powered mental wellness platform built with Spring Boot.
It provides a safe, private space for emotional expression, guided self-help, and
empathetic AI support — while always being honest that it is a **companion, not a therapist**.

---

## Core Principles

| Principle | What it means in practice |
|-----------|--------------------------|
| 🌱 Non-judgmental | Every response is warm, never preachy or clinical |
| 🔒 Privacy-first | JWT auth, anonymous mode, encrypted passwords, no data selling |
| 🧠 Science-backed | CBT, mindfulness, somatic techniques — real psychology |
| 🤝 Support, not replacement | AI always encourages professional help when needed |
| 🚨 Safety-aware | Crisis detection is mandatory and always active |

---

## Features

### 1. AI Companion 
- Empathetic, non-judgmental conversation
- Full session history with context memory
- **Crisis detection** — automatically shows emergency resources when needed
- Journal reflection — gentle AI insights on your writing

### 2. Mood Tracking
- Daily mood check-in with 10 emotion types
- 1–10 intensity scale
- 7-day pattern analysis with generated insights
- Dominant mood detection

### 3. Anonymous Journaling
- Private by default, up to 5,000 characters
- Tag entries for reflection
- Optional AI-generated gentle reflection

### 4. Guided Wellness Exercises
- Box Breathing, 4-7-8 Breathing
- 5-4-3-2-1 Grounding, Safe Place Visualization
- CBT Thought Challenge, Gratitude Anchoring
- Body Scan Meditation, Affirmations, Morning Pages

### 5. Personalized Healing Paths
- Stress · Anxiety · Low Self-Esteem · Burnout · Grief · General
- Recommended exercises tailored to your path
- Onboarding flow with welcome message

---

## Tech Stack

```
Backend:   Spring Boot 3.2 · Spring Security · Spring Data JPA
Database:  MySQL 8+ (Hibernate auto-schema)
Auth:      JWT (io.jsonwebtoken / JJWT)
AI:       Ollama + Llama 3.2 (Local AI Model)
HTTP:      Spring WebFlux WebClient
Docs:      SpringDoc OpenAPI / Swagger UI
```

---

## Project Structure

```
mindbloom/
├── config/
│   ├── SecurityConfig.java        JWT security chain + CORS
│   ├── WebClientConfig.java       WebClient bean
│   └── DataSeeder.java            Seeds 9 wellness exercises on startup
├── controller/
│   └── Controllers.java           Auth · Chat · Mood · Journal · Exercise · Onboarding
├── dto/
│   └── Dtos.java                  All request/response DTOs
├── entity/
│   ├── User.java                  User with HealingPath enum
│   ├── MoodLog.java               Daily mood entry
│   ├── JournalEntry.java          Private journal entry
│   ├── ChatMessage.java           AI conversation messages
│   └── WellnessExercise.java      Guided exercises
├── repository/
│   └── Repositories.java          All Spring Data JPA repos
├── security/
│   ├── JwtUtil.java               Token generation + validation
│   └── JwtAuthenticationFilter.java  Request filter
├── service/
│   ├── AiChatService.java         Anthropic API calls + crisis handling
│   └── Services.java              Auth · Mood · Journal · Exercise · Onboarding
├── util/
│   └── CrisisDetector.java        🚨 Safety-critical crisis detection
├── exception/
│   └── GlobalExceptionHandler.java
└── MindBloomApplication.java
```

---

## Quick Start

### 1. Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.8+
-Ollama (Local AI Runtime)
Llama 3 Local AI Model

### 2. Create the database
```sql
CREATE DATABASE mindbloom_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure environment variables
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
export JWT_SECRET=your-very-long-random-secret-key-here
export ANTHROPIC_API_KEY=sk-ant-your-key-here
```

Or edit `src/main/resources/application.yml` directly.

### 4. Run
```bash
mvn spring-boot:run
```

The app starts at `http://localhost:8080/api`
Swagger UI: `http://localhost:8080/api/swagger-ui.html`

---

## API Overview

### Authentication
```
POST /api/auth/register     Create account
POST /api/auth/login        Login → get JWT
```

### Onboarding
```
POST /api/onboarding        Choose healing path, get personalized plan
```

### Mood
```
POST /api/mood              Log today's mood
GET  /api/mood/summary      7-day summary + insight
```

### Journal
```
POST /api/journal           New entry (optional AI reflection)
GET  /api/journal           All entries (paginated)
GET  /api/journal/{id}      Single entry
DELETE /api/journal/{id}    Delete
```

### AI Companion
```
POST /api/chat              Send message → get response
GET  /api/chat/history/{sessionId}   Conversation history
```

### Exercises
```
GET /api/exercises                    All exercises
GET /api/exercises/type/{TYPE}        By type (BREATHING, GROUNDING, etc.)
GET /api/exercises/recommended        Tailored to your healing path
```

---

## Authentication Flow

```
POST /auth/register → 201 { accessToken, user }
POST /auth/login    → 200 { accessToken, user }

All protected routes → Authorization: Bearer <token>
```

---

## Crisis Handling (Ethical Core)

When a user message contains crisis language (configurable in `application.yml`),
the platform **never** continues the conversation as normal. Instead it:

1. Flags the message in the database for awareness
2. Stops the AI from generating a response to the crisis details
3. Sends a compassionate acknowledgment
4. Lists real crisis resources (Canada, US, International)
5. Encourages contacting a human professional

**This cannot be disabled.** It is a core ethical requirement.

Crisis keywords are checked in `CrisisDetector.java` and configured in:
```yaml
mindbloom:
  crisis-keywords:
    - "kill myself"
    - "end my life"
    # ... see application.yml
```

---

## Disclaimer

MindBloom is **not a medical device** and **not a replacement for therapy or professional care**.
It provides emotional support, psychoeducation, and guided self-help tools only.

Users in crisis should contact emergency services or a crisis line immediately.

---

## What Makes MindBloom Different

1. **Healing first, features second** — every design decision asks "does this help someone heal?"
2. **Ethical AI** — transparent, bounded, always deferring to professionals for serious situations
3. **Privacy by design** — anonymous mode, encrypted passwords, no tracking
4. **Celebrates small wins** — "6-day streak" is celebrated the same as a marathon
5. **Honest** — never promises "cure" or "happiness"; normalizes struggle

---

*Built with care. For people who are just trying to get through the day. 🌱*
