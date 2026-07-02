package com.mindbloom.service;

import com.mindbloom.dto.ChatDtos;
import com.mindbloom.entity.ChatMessage;
import com.mindbloom.entity.User;
import com.mindbloom.repository.ChatMessageRepository;
import com.mindbloom.repository.UserRepository;
import com.mindbloom.util.CrisisDetector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatService {

    private final WebClient.Builder webClientBuilder;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final CrisisDetector crisisDetector;

    @Value("${ai.ollama.base-url}")
    private String baseUrl;

    @Value("${ai.ollama.model}")
    private String model;

    @Value("${ai.system-prompt}")
    private String systemPrompt;

    @Value("${mindbloom.max-chat-history}")
    private int maxChatHistory;

    /**
     * Process a user message, detect crises, call the AI, and save the conversation.
     */
    @Transactional
    public ChatDtos.ChatResponse chat(String userEmail, ChatDtos.ChatRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String sessionId = request.getSessionId() != null
                ? request.getSessionId()
                : UUID.randomUUID().toString();

        boolean crisisDetected = crisisDetector.isCrisis(request.getMessage());

        // Save user message
        ChatMessage userMessage = ChatMessage.builder()
                .user(user)
                .sessionId(sessionId)
                .role(ChatMessage.MessageRole.USER)
                .content(request.getMessage())
                .crisisFlagged(crisisDetected)
                .build();
        chatMessageRepository.save(userMessage);

        // Build response
        String aiResponse;
        if (crisisDetected) {
            log.warn("⚠️ Crisis signal detected for user session: {}", sessionId);
            aiResponse = buildCrisisResponse();
        } else {
            aiResponse = callAnthropicApi(user, sessionId, request.getMessage());
        }

        // Save AI response
        ChatMessage assistantMessage = ChatMessage.builder()
                .user(user)
                .sessionId(sessionId)
                .role(ChatMessage.MessageRole.ASSISTANT)
                .content(aiResponse)
                .crisisFlagged(crisisDetected)
                .build();
        chatMessageRepository.save(assistantMessage);

        return ChatDtos.ChatResponse.builder()
                .sessionId(sessionId)
                .message(aiResponse)
                .crisisDetected(crisisDetected)
                .crisisResources(crisisDetected ? crisisDetector.getResources() : null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Retrieve the full conversation history for a session.
     */
    @Transactional(readOnly = true)
    public ChatDtos.ChatHistoryResponse getHistory(String userEmail, String sessionId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ChatMessage> messages = chatMessageRepository
                .findByUserAndSessionIdOrderByCreatedAtAsc(user, sessionId);

        List<ChatDtos.MessageDto> dtos = messages.stream()
                .map(m -> ChatDtos.MessageDto.builder()
                        .role(m.getRole().name().toLowerCase())
                        .content(m.getContent())
                        .crisisFlagged(m.isCrisisFlagged())
                        .timestamp(m.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ChatDtos.ChatHistoryResponse.builder()
                .sessionId(sessionId)
                .messages(dtos)
                .build();
    }

    /**
     * Request an AI reflection on a journal entry.
     */
    public String generateJournalReflection(String journalContent) {
        String prompt = """
                A user has written this journal entry. Please offer a brief, warm, \
                empathetic reflection (2-3 sentences). Focus on validating their feelings, \
                acknowledging their strength, and one gentle observation. \
                Do NOT diagnose or give clinical advice. Be human and kind.
                
                Journal entry:
                """ + journalContent;

        return callAnthropicApiDirect(prompt);
    }

    // =========== Private Helpers ===========

    private String callAnthropicApi(User user, String sessionId, String newMessage) {
        // Load recent history for context
        List<ChatMessage> history = chatMessageRepository
                .findByUserAndSessionIdOrderByCreatedAtAsc(user, sessionId);

        StringBuilder prompt = new StringBuilder(systemPrompt + "\n\n");

        int startIdx = Math.max(0, history.size() - maxChatHistory);

        for (int i = startIdx; i < history.size(); i++) {

            ChatMessage m = history.get(i);

            prompt.append(m.getRole().name())
                    .append(": ")
                    .append(m.getContent())
                    .append("\n");
        }

        prompt.append("USER: ").append(newMessage);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "prompt", prompt.toString(),
                "stream", false
        );

        try {
            Map<?, ?> response = webClientBuilder.build()
            		.post()
            		.uri(baseUrl + "/api/generate")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null) {
                return (String) response.get("response");
            }
        } catch (Exception e) {
            
            log.error("AI API call failed", e);
        }

        return "I'm here with you. I had a little trouble connecting just now, " +
               "but I want you to know you're not alone. Please try again in a moment. 🌱";
    }

    private String callAnthropicApiDirect(String userPrompt) {
    	Map<String, Object> requestBody = Map.of(
    	        "model", model,
    	        "prompt", systemPrompt + "\n\n" + userPrompt,
    	        "stream", false
    	);

        try {
            Map<?, ?> response = webClientBuilder.build()
            		.post()
            		.uri(baseUrl + "/api/generate")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null) {
                return (String) response.get("response");
            }
        } catch (Exception e) {
            log.error("AI reflection failed: {}", e.getMessage());
        }

        return "I couldn't generate a reflection right now, but your thoughts matter deeply. 🌱";
    }

    private String buildCrisisResponse() {
        StringBuilder sb = new StringBuilder(crisisDetector.getCrisisResponsePrefix());
        sb.append("\n\n");
        crisisDetector.getResources().forEach(r -> {
            sb.append("• **").append(r.getName()).append("**: ").append(r.getContact()).append("\n");
            sb.append("  ").append(r.getDescription()).append("\n\n");
        });
        sb.append("\nYou don't have to go through this alone. Please reach out to one of these services. 💙");
        return sb.toString();
    }
}
