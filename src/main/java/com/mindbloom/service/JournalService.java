package com.mindbloom.service;

import com.mindbloom.dto.JournalDtos;
import com.mindbloom.entity.JournalEntry;
import com.mindbloom.entity.User;
import com.mindbloom.repository.JournalEntryRepository;
import com.mindbloom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JournalService {

    private final JournalEntryRepository journalRepository;
    private final UserRepository userRepository;
    private final AiChatService aiChatService;

    @Transactional
    public JournalDtos.JournalResponse create(
            String email,
            JournalDtos.JournalRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String reflection = null;

        if (request.isRequestAiReflection()) {
            try {
                reflection = aiChatService.generateJournalReflection(request.getContent());
            } catch (Exception e) {
                log.error("AI reflection failed", e);
            }
        }

        JournalEntry entry = JournalEntry.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .mood(request.getMood())
                .tags(request.getTags())
                .aiReflection(reflection)
                .build();

        entry = journalRepository.save(entry);

        return map(entry);
    }

    @Transactional(readOnly = true)
    public Page<JournalDtos.JournalResponse> getAll(
            String email,
            Pageable pageable) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return journalRepository
                .findByUserOrderByCreatedAtDesc(user, pageable)
                .map(this::map);
    }

    @Transactional(readOnly = true)
    public JournalDtos.JournalResponse getById(
            String email,
            Long id) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JournalEntry entry = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return map(entry);
    }

    @Transactional
    public void delete(
            String email,
            Long id) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JournalEntry entry = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        journalRepository.delete(entry);
    }

    private JournalDtos.JournalResponse map(JournalEntry entry) {

        JournalDtos.JournalResponse response = new JournalDtos.JournalResponse();

        response.setId(entry.getId());
        response.setTitle(entry.getTitle());
        response.setContent(entry.getContent());
        response.setMood(entry.getMood());
        response.setTags(entry.getTags());
        response.setAiReflection(entry.getAiReflection());
        response.setCreatedAt(entry.getCreatedAt());
        response.setUpdatedAt(entry.getUpdatedAt());

        return response;
    }
}