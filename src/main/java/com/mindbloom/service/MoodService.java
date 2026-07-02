package com.mindbloom.service;

import com.mindbloom.dto.MoodDtos;
import com.mindbloom.entity.MoodLog;
import com.mindbloom.entity.User;
import com.mindbloom.repository.MoodLogRepository;
import com.mindbloom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoodService {

    private final MoodLogRepository moodLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public MoodDtos.MoodLogResponse logMood(
            String email,
            MoodDtos.MoodLogRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MoodLog moodLog = moodLogRepository
                .findByUserAndLogDate(user, LocalDate.now())
                .orElse(new MoodLog());

        moodLog.setUser(user);
        moodLog.setMood(request.getMood());
        moodLog.setIntensity(request.getIntensity());
        moodLog.setNote(request.getNote());
        moodLog.setLogDate(LocalDate.now());

        moodLog = moodLogRepository.save(moodLog);

        return convertToDto(moodLog);
    }

    @Transactional(readOnly = true)
    public MoodDtos.MoodSummary getMoodSummary(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<MoodLog> logs = moodLogRepository
                .findByUserAndLogDateBetweenOrderByLogDateDesc(
                        user,
                        LocalDate.now().minusDays(6),
                        LocalDate.now()
                );

        List<MoodDtos.MoodLogResponse> responses =
                logs.stream()
                        .map(this::convertToDto)
                        .toList();

        MoodLog.MoodType dominantMood = null;

        List<Object[]> frequency =
                moodLogRepository.findMoodFrequency(user);

        if (!frequency.isEmpty()) {
            dominantMood = (MoodLog.MoodType) frequency.get(0)[0];
        }

        Double avg =
                moodLogRepository.findAverageIntensitySince(
                        user,
                        LocalDate.now().minusDays(6)
                );

        MoodDtos.MoodSummary summary =
                new MoodDtos.MoodSummary();

        summary.setLast7Days(responses);
        summary.setDominantMood(dominantMood);
        summary.setAverageIntensity(avg == null ? 0 : avg);
        summary.setInsight(generateInsight(dominantMood));

        return summary;
    }

    private MoodDtos.MoodLogResponse convertToDto(MoodLog mood) {

        MoodDtos.MoodLogResponse dto =
                new MoodDtos.MoodLogResponse();

        dto.setId(mood.getId());
        dto.setMood(mood.getMood());
        dto.setMoodLabel(mood.getMood().getLabel());
        dto.setMoodEmoji(mood.getMood().getEmoji());
        dto.setIntensity(mood.getIntensity());
        dto.setNote(mood.getNote());
        dto.setLogDate(mood.getLogDate());
        dto.setCreatedAt(mood.getCreatedAt());

        return dto;
    }

    private String generateInsight(MoodLog.MoodType mood) {

        if (mood == null) {
            return "Keep tracking your mood daily to receive personalized insights.";
        }

        return switch (mood) {
            case JOYFUL ->
                    "You've been feeling joyful lately. Keep doing what supports your happiness.";

            case CALM ->
                    "Your recent mood has been calm. That's a great sign of emotional balance.";

            case ANXIOUS ->
                    "You've reported anxiety recently. Consider trying breathing exercises.";

            case SAD ->
                    "You've been feeling sad recently. Journaling or talking to someone may help.";

            case OVERWHELMED ->
                    "You've been feeling overwhelmed. Take breaks and focus on one thing at a time.";

            case ANGRY ->
                    "Notice what triggers your anger and practice grounding exercises.";

            case EXHAUSTED ->
                    "Rest is important. Prioritize sleep and self-care.";

            case HOPEFUL ->
                    "Your hopeful mindset is encouraging. Keep nurturing positive habits.";

            case GRATEFUL ->
                    "Practicing gratitude has positive effects on emotional wellbeing.";

            case NEUTRAL ->
                    "A neutral mood is perfectly normal. Continue checking in with yourself.";
        };
    }
}