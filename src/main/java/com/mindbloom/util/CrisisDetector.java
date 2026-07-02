package com.mindbloom.util;

import com.mindbloom.dto.ChatDtos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Crisis detection – the most ethically critical component of MindBloom.
 *
 * When crisis signals are detected, we ALWAYS show resources and
 * encourage professional help. We NEVER provide harmful instructions
 * or engage with the crisis details directly.
 */
@Component
public class CrisisDetector {

    @Value("${mindbloom.crisis-keywords:self-harm,suicide,kill myself,end my life}")
    private List<String> crisisKeywords;
    private static final List<ChatDtos.CrisisResource> CRISIS_RESOURCES = List.of(
            ChatDtos.CrisisResource.builder()
                    .name("Crisis Services Canada")
                    .contact("1-833-456-4566")
                    .description("24/7 crisis support, available in English and French")
                    .build(),
            ChatDtos.CrisisResource.builder()
                    .name("988 Suicide & Crisis Lifeline (US)")
                    .contact("Call or text 988")
                    .description("Free, confidential support 24/7")
                    .build(),
            ChatDtos.CrisisResource.builder()
                    .name("International Association for Suicide Prevention")
                    .contact("https://www.iasp.info/resources/Crisis_Centres/")
                    .description("Find crisis centers worldwide")
                    .build(),
            ChatDtos.CrisisResource.builder()
                    .name("Crisis Text Line")
                    .contact("Text HOME to 741741")
                    .description("Free, 24/7 text-based crisis support")
                    .build()
    );

    /**
     * Checks whether the message contains crisis-level language.
     */
    public boolean isCrisis(String message) {
        if (message == null || message.isBlank()) return false;
        String lower = message.toLowerCase();
        return crisisKeywords.stream().anyMatch(lower::contains);
    }

    /**
     * Returns the list of crisis support resources to show the user.
     */
    public List<ChatDtos.CrisisResource> getResources() {
        return CRISIS_RESOURCES;
    }

    /**
     * A safe, compassionate prefix to prepend when crisis is detected.
     * This replaces any potentially harmful AI response.
     */
    public String getCrisisResponsePrefix() {
        return """
                I can hear that you're going through something incredibly painful right now, \
                and I'm really glad you reached out. What you're feeling matters deeply. 🌱

                I want to be honest with you: I'm an AI companion, and right now you deserve \
                to speak with a real human who can truly support you.

                **Please reach out to one of these free, confidential services:**
                """;
    }
}
