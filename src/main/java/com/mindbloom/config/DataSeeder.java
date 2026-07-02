package com.mindbloom.config;

import com.mindbloom.entity.WellnessExercise;
import com.mindbloom.repository.WellnessExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds initial wellness exercises so the app works on first run.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final WellnessExerciseRepository exerciseRepository;

    @Override
    public void run(String... args) {
        if (exerciseRepository.count() > 0) return;

        log.info("🌱 Seeding wellness exercises...");

        List<WellnessExercise> exercises = List.of(

            // --- Breathing ---
            WellnessExercise.builder()
                .title("Box Breathing")
                .description("A simple technique used by Navy SEALs to calm the nervous system. "
                           + "Perfect for anxiety and stress.")
                .type(WellnessExercise.ExerciseType.BREATHING)
                .durationMinutes(5)
                .difficultyLevel(1)
                .suitableForPaths("ANXIETY,STRESS,BURNOUT")
                .steps("""
                    ["Sit comfortably and close your eyes",
                     "Inhale slowly for 4 counts",
                     "Hold your breath for 4 counts",
                     "Exhale slowly for 4 counts",
                     "Hold empty for 4 counts",
                     "Repeat 4 to 8 times",
                     "Open your eyes gently and notice how you feel"]
                    """)
                .build(),

            WellnessExercise.builder()
                .title("4-7-8 Breathing")
                .description("A calming breath pattern that activates the parasympathetic "
                           + "nervous system and helps with sleep and anxiety.")
                .type(WellnessExercise.ExerciseType.BREATHING)
                .durationMinutes(3)
                .difficultyLevel(1)
                .suitableForPaths("ANXIETY,STRESS,GENERAL")
                .steps("""
                    ["Exhale completely through your mouth",
                     "Close your mouth and inhale through your nose for 4 counts",
                     "Hold your breath for 7 counts",
                     "Exhale completely through your mouth for 8 counts",
                     "This completes one cycle",
                     "Repeat 3 to 4 cycles"]
                    """)
                .build(),

            // --- Grounding ---
            WellnessExercise.builder()
                .title("5-4-3-2-1 Grounding")
                .description("A sensory awareness technique that anchors you to the present "
                           + "moment when anxiety or overwhelm takes over.")
                .type(WellnessExercise.ExerciseType.GROUNDING)
                .durationMinutes(5)
                .difficultyLevel(1)
                .suitableForPaths("ANXIETY,STRESS,BURNOUT,GENERAL")
                .steps("""
                    ["Notice 5 things you can SEE around you",
                     "Notice 4 things you can TOUCH — feel their texture",
                     "Notice 3 things you can HEAR right now",
                     "Notice 2 things you can SMELL",
                     "Notice 1 thing you can TASTE",
                     "Take one slow breath and return to the present moment"]
                    """)
                .build(),

            WellnessExercise.builder()
                .title("Safe Place Visualization")
                .description("A guided imagery exercise to create a mental refuge during "
                           + "difficult moments.")
                .type(WellnessExercise.ExerciseType.GROUNDING)
                .durationMinutes(10)
                .difficultyLevel(2)
                .suitableForPaths("ANXIETY,GRIEF,SELF_ESTEEM,GENERAL")
                .steps("""
                    ["Close your eyes and take 3 slow breaths",
                     "Imagine a place where you feel completely safe and at peace",
                     "It can be real or imaginary — a forest, a beach, a cozy room",
                     "Notice the colors, sounds, and smells of this place",
                     "Feel the warmth and safety surrounding you",
                     "Spend a few minutes here — you can return anytime",
                     "Slowly open your eyes and carry that peace with you"]
                    """)
                .build(),

            // --- CBT Reframing ---
            WellnessExercise.builder()
                .title("Thought Challenge")
                .description("A CBT-inspired exercise to gently examine anxious or "
                           + "negative thoughts and see them more clearly.")
                .type(WellnessExercise.ExerciseType.CBT_REFRAME)
                .durationMinutes(10)
                .difficultyLevel(2)
                .suitableForPaths("ANXIETY,SELF_ESTEEM,STRESS,GENERAL")
                .steps("""
                    ["Write down the thought that's bothering you",
                     "Ask: Is this thought a fact, or is it a feeling?",
                     "Ask: What evidence supports this thought?",
                     "Ask: What evidence goes against this thought?",
                     "Ask: What would you say to a friend having this thought?",
                     "Write a more balanced, realistic version of the thought",
                     "Notice how your body feels after this reframe"]
                    """)
                .build(),

            WellnessExercise.builder()
                .title("Gratitude Anchoring")
                .description("A simple daily practice that rewires the brain toward "
                           + "noticing goodness, even in hard times.")
                .type(WellnessExercise.ExerciseType.CBT_REFRAME)
                .durationMinutes(5)
                .difficultyLevel(1)
                .suitableForPaths("SELF_ESTEEM,BURNOUT,GRIEF,GENERAL")
                .steps("""
                    ["Find a quiet moment and take 2 slow breaths",
                     "Think of 3 things you are grateful for today",
                     "They can be small: a warm drink, a kind word, sunlight",
                     "For each one, really feel why it matters to you",
                     "Write them down if you can",
                     "Notice the small warmth this brings — that warmth is real"]
                    """)
                .build(),

            // --- Meditation ---
            WellnessExercise.builder()
                .title("Body Scan Meditation")
                .description("A mindfulness practice that builds awareness of physical "
                           + "tension and teaches you to release it with compassion.")
                .type(WellnessExercise.ExerciseType.BODY_SCAN)
                .durationMinutes(15)
                .difficultyLevel(2)
                .suitableForPaths("STRESS,ANXIETY,BURNOUT,GENERAL")
                .steps("""
                    ["Lie down or sit comfortably",
                     "Close your eyes and take 5 slow breaths",
                     "Bring attention to the top of your head",
                     "Slowly scan downward — face, neck, shoulders",
                     "Notice any tension without judging it",
                     "As you breathe out, imagine the tension softening",
                     "Continue through chest, arms, belly, hips",
                     "Move through legs, knees, feet, and toes",
                     "Rest in the sensation of your whole body breathing",
                     "Gently open your eyes when you are ready"]
                    """)
                .build(),

            // --- Journaling Prompts ---
            WellnessExercise.builder()
                .title("Morning Pages Prompt")
                .description("A freewriting practice to clear mental clutter and "
                           + "start your day with self-awareness.")
                .type(WellnessExercise.ExerciseType.JOURNALING_PROMPT)
                .durationMinutes(10)
                .difficultyLevel(1)
                .suitableForPaths("STRESS,ANXIETY,SELF_ESTEEM,BURNOUT,GENERAL")
                .steps("""
                    ["Open your journal or a blank page",
                     "Set a timer for 10 minutes",
                     "Write continuously — do not stop, do not edit",
                     "Start with: 'Right now I am feeling...'",
                     "Let whatever comes, come — no filter, no judgment",
                     "When time is up, close the journal without rereading",
                     "This space belongs only to you"]
                    """)
                .build(),

            // --- Affirmation ---
            WellnessExercise.builder()
                .title("Self-Compassion Affirmations")
                .description("Gentle affirmations grounded in self-compassion research by Dr. Kristin Neff.")
                .type(WellnessExercise.ExerciseType.AFFIRMATION)
                .durationMinutes(5)
                .difficultyLevel(1)
                .suitableForPaths("SELF_ESTEEM,GRIEF,BURNOUT,GENERAL")
                .steps("""
                    ["Place one hand on your heart",
                     "Take a slow breath and feel your heartbeat",
                     "Repeat gently: 'This is a moment of suffering'",
                     "Repeat: 'Suffering is a part of being human'",
                     "Repeat: 'May I be kind to myself in this moment'",
                     "Repeat: 'May I give myself the compassion I need'",
                     "Stay here as long as you need — there is no rush"]
                    """)
                .build()
        );

        exerciseRepository.saveAll(exercises);
        log.info("✅ Seeded {} wellness exercises", exercises.size());
    }
}
