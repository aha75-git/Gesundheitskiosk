package de.aha.backend.service.chat;

import de.aha.backend.model.chat.ChatSession;
import de.aha.backend.repository.chat.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageService chatMessageService;

    public ChatSession createOrUpdateSession(ChatSession session) {
        Optional<ChatSession> existingSession = chatSessionRepository
                .findByUserIdAndAdvisorId(session.getUserId(), session.getAdvisorId());

        if (existingSession.isPresent()) {
            ChatSession updatedSession = existingSession.get();
            updatedSession.setLastMessage(session.getLastMessage());
            updatedSession.setLastMessageTime(LocalDateTime.now());
            updatedSession.setUnreadCount(session.getUnreadCount());
            updatedSession.setModifyDate(LocalDateTime.now());
            updatedSession.setIsActive(true);

            ChatSession savedSession = chatSessionRepository.save(updatedSession);
            log.info("Updated existing session: {}", savedSession.getId());
            return savedSession;
        } else {
            session.setCreationDate(LocalDateTime.now());
            session.setModifyDate(LocalDateTime.now());
            session.setIsActive(true);
            session.setUnreadCount(0);

            ChatSession savedSession = chatSessionRepository.save(session);
            log.info("Created new session: {}", savedSession.getId());
            return savedSession;
        }
    }

    public List<ChatSession> getUserSessions(String userId) {
        List<ChatSession> sessions = chatSessionRepository.findByUserIdOrderByLastMessageTimeDesc(userId);

        // Aktualisiere unread Count für jede Session
        sessions.forEach(session -> {
            long unreadCount = chatMessageService.getUnreadCountForUser(session.getId());
            if (session.getUnreadCount() != unreadCount) {
                session.setUnreadCount((int) unreadCount);
                chatSessionRepository.save(session);
            }
        });

        return sessions;
    }

    public List<ChatSession> getAdvisorSessions(String advisorId) {
        List<ChatSession> sessions = chatSessionRepository.findByAdvisorIdOrderByLastMessageTimeDesc(advisorId);

        // Aktualisiere unread Count für jede Session
        sessions.forEach(session -> {
            long unreadCount = chatMessageService.getUnreadCountForAdvisor(session.getId());
            if (session.getUnreadCount() != unreadCount) {
                session.setUnreadCount((int) unreadCount);
                chatSessionRepository.save(session);
            }
        });

        return sessions;
    }

    public Optional<ChatSession> getSession(String sessionId) {
        return chatSessionRepository.findById(sessionId);
    }

    public Optional<ChatSession> getSessionByUserAndAdvisor(String userId, String advisorId) {
        return chatSessionRepository.findByUserIdAndAdvisorId(userId, advisorId);
    }

    public void deactivateSession(String sessionId) {
        chatSessionRepository.findById(sessionId).ifPresent(session -> {
            session.setIsActive(false);
            session.setModifyDate(LocalDateTime.now());
            chatSessionRepository.save(session);
            log.info("Deactivated session: {}", sessionId);
        });
    }

    public boolean sessionExists(String userId, String advisorId) {
        return chatSessionRepository.existsByUserIdAndAdvisorId(userId, advisorId);
    }
}