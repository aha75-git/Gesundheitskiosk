package de.aha.backend.repository.chat;

import de.aha.backend.model.chat.ChatSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = {"spring.mongodb.embedded.version=4.0.12"})
class ChatSessionRepositoryTest {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    private ChatSession activeSession1;
    private ChatSession activeSession2;
    private ChatSession inactiveSession;
    private ChatSession sessionForUser1;
    private ChatSession sessionForUser2;
    private ChatSession sessionForAdvisor1;
    private ChatSession sessionForAdvisor2;

    @BeforeEach
    void setUp() {
        chatSessionRepository.deleteAll();

        LocalDateTime now = LocalDateTime.now();

        // Aktive Sessions für verschiedene User/Advisor Kombinationen
        activeSession1 = ChatSession.builder()
                .userId("user1")
                .advisorId("advisor1")
                .advisorName("Advisor One")
                .lastMessage("Hello from user1")
                .lastMessageTime(now.minusHours(2))
                .unreadCount(3)
                .isActive(true)
                .build();
        activeSession1.setCreationDate(now.minusDays(1));
        activeSession1.setModifyDate(now.minusHours(2));

        activeSession2 = ChatSession.builder()
                .userId("user2")
                .advisorId("advisor1")
                .advisorName("Advisor One")
                .lastMessage("Hello from user2")
                .lastMessageTime(now.minusHours(1))
                .unreadCount(0)
                .isActive(true)
                .build();
        activeSession2.setCreationDate(now.minusDays(2));
        activeSession2.setModifyDate(now.minusHours(1));

        inactiveSession = ChatSession.builder()
                .userId("user3")
                .advisorId("advisor1")
                .advisorName("Advisor One")
                .lastMessage("Old message")
                .lastMessageTime(now.minusDays(10))
                .unreadCount(5)
                .isActive(false)
                .build();
        inactiveSession.setCreationDate(now.minusDays(10));
        inactiveSession.setModifyDate(now.minusDays(5));

        sessionForUser1 = ChatSession.builder()
                .userId("user1")
                .advisorId("advisor2")
                .advisorName("Advisor Two")
                .lastMessage("Another session")
                .lastMessageTime(now.minusHours(3))
                .unreadCount(1)
                .isActive(true)
                .build();
        sessionForUser1.setCreationDate(now.minusDays(3));
        sessionForUser1.setModifyDate(now.minusHours(3));

        sessionForAdvisor1 = ChatSession.builder()
                .userId("user3")
                .advisorId("advisor1")
                .advisorName("Advisor One")
                .lastMessage("Test message")
                .lastMessageTime(now.minusHours(4))
                .unreadCount(2)
                .isActive(true)
                .build();
        sessionForAdvisor1.setCreationDate(now.minusDays(4));
        sessionForAdvisor1.setModifyDate(now.minusHours(4));

        sessionForAdvisor2 = ChatSession.builder()
                .userId("user4")
                .advisorId("advisor2")
                .advisorName("Advisor Two")
                .lastMessage("Latest message")
                .lastMessageTime(now)
                .unreadCount(0)
                .isActive(true)
                .build();
        sessionForAdvisor2.setCreationDate(now.minusDays(1));
        sessionForAdvisor2.setModifyDate(now);

        // Speichere alle Testdaten
        chatSessionRepository.saveAll(List.of(
                activeSession1, activeSession2, inactiveSession,
                sessionForUser1, sessionForAdvisor1, sessionForAdvisor2
        ));
    }

    @Test
    void findByUserIdOrderByLastMessageTimeDesc_success() {
        // Act
        List<ChatSession> result = chatSessionRepository.findByUserIdOrderByLastMessageTimeDesc("user1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Überprüfe die Sortierung (absteigend nach lastMessageTime)
        assertTrue(result.get(0).getLastMessageTime().isAfter(result.get(1).getLastMessageTime()));

        // Überprüfe die enthaltenen Sessions
        List<String> sessionIds = result.stream().map(ChatSession::getId).toList();
        assertTrue(sessionIds.contains(activeSession1.getId()));
        assertTrue(sessionIds.contains(sessionForUser1.getId()));
    }

    @Test
    void findByUserIdOrderByLastMessageTimeDesc_userNotFound() {
        // Act
        List<ChatSession> result = chatSessionRepository.findByUserIdOrderByLastMessageTimeDesc("nonexistent");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByAdvisorIdOrderByLastMessageTimeDesc_success() {
        // Act
        List<ChatSession> result = chatSessionRepository.findByAdvisorIdOrderByLastMessageTimeDesc("advisor1");

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size()); // activeSession1, activeSession2, inactiveSession, sessionForAdvisor1

        // Überprüfe die Sortierung (absteigend nach lastMessageTime)
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getLastMessageTime().isAfter(result.get(i + 1).getLastMessageTime()));
        }
    }

    @Test
    void findByAdvisorIdOrderByLastMessageTimeDesc_advisorNotFound() {
        // Act
        List<ChatSession> result = chatSessionRepository.findByAdvisorIdOrderByLastMessageTimeDesc("nonexistent");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndAdvisorId_success() {
        // Act
        Optional<ChatSession> result = chatSessionRepository.findByUserIdAndAdvisorId("user1", "advisor1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUserId());
        assertEquals("advisor1", result.get().getAdvisorId());
        assertEquals(activeSession1.getId(), result.get().getId());
    }

    @Test
    void findByUserIdAndAdvisorId_notFound() {
        // Act
        Optional<ChatSession> result = chatSessionRepository.findByUserIdAndAdvisorId("nonexistent", "advisor1");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findActiveSessionsByUserId_success() {
        // Act
        List<ChatSession> result = chatSessionRepository.findActiveSessionsByUserId("user1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // activeSession1 und sessionForUser1

        // Überprüfe, dass nur aktive Sessions zurückgegeben werden
        result.forEach(session -> assertTrue(session.getIsActive()));

        List<String> sessionIds = result.stream().map(ChatSession::getId).toList();
        assertTrue(sessionIds.contains(activeSession1.getId()));
        assertTrue(sessionIds.contains(sessionForUser1.getId()));
    }

    @Test
    void findActiveSessionsByUserId_inactiveSessionExcluded() {
        // Act
        List<ChatSession> result = chatSessionRepository.findActiveSessionsByUserId("user3");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size()); // Nur sessionForAdvisor1, nicht inactiveSession
        assertTrue(result.get(0).getIsActive());
        assertEquals(sessionForAdvisor1.getId(), result.get(0).getId());
    }

    @Test
    void findActiveSessionsByAdvisorId_success() {
        // Act
        List<ChatSession> result = chatSessionRepository.findActiveSessionsByAdvisorId("advisor1");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // activeSession1, activeSession2 (nicht inactiveSession)

        // Überprüfe, dass nur aktive Sessions zurückgegeben werden
        result.forEach(session -> {
            assertTrue(session.getIsActive());
            assertEquals("advisor1", session.getAdvisorId());
        });
    }

    @Test
    void findActiveSessionsByAdvisorId_advisorNotFound() {
        // Act
        List<ChatSession> result = chatSessionRepository.findActiveSessionsByAdvisorId("nonexistent");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void existsByUserIdAndAdvisorId_true() {
        // Act
        boolean result = chatSessionRepository.existsByUserIdAndAdvisorId("user1", "advisor1");

        // Assert
        assertTrue(result);
    }

    @Test
    void existsByUserIdAndAdvisorId_false() {
        // Act
        boolean result = chatSessionRepository.existsByUserIdAndAdvisorId("nonexistent", "advisor1");

        // Assert
        assertFalse(result);
    }

    @Test
    void existsByUserIdAndAdvisorId_inactiveSessionStillExists() {
        // Act
        boolean result = chatSessionRepository.existsByUserIdAndAdvisorId("user3", "advisor1");

        // Assert
        assertTrue(result); // Sollte true zurückgeben, auch wenn die Session inaktiv ist
    }

    @Test
    void saveAndFindById_success() {
        // Arrange
        ChatSession newSession = ChatSession.builder()
                .userId("newUser")
                .advisorId("newAdvisor")
                .advisorName("New Advisor")
                .lastMessage("Test message")
                .lastMessageTime(LocalDateTime.now())
                .unreadCount(0)
                .isActive(true)
                .build();
        newSession.setCreationDate(LocalDateTime.now());
        newSession.setModifyDate(LocalDateTime.now());

        // Act
        ChatSession savedSession = chatSessionRepository.save(newSession);
        Optional<ChatSession> foundSession = chatSessionRepository.findById(savedSession.getId());

        // Assert
        assertTrue(foundSession.isPresent());
        assertEquals(savedSession.getId(), foundSession.get().getId());
        assertEquals("newUser", foundSession.get().getUserId());
        assertEquals("newAdvisor", foundSession.get().getAdvisorId());
    }

    @Test
    void deleteSession_success() {
        // Arrange
        String sessionId = activeSession1.getId();

        // Act
        chatSessionRepository.deleteById(sessionId);
        Optional<ChatSession> result = chatSessionRepository.findById(sessionId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAllSessions_success() {
        // Act
        List<ChatSession> result = chatSessionRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(6, result.size()); // Alle 6 Test-Sessions
    }

    @Test
    void findByUserIdOrderByLastMessageTimeDesc_verifyOrder() {
        // Arrange - Erstelle spezifische Sessions mit bekannten Zeitstempeln
        LocalDateTime baseTime = LocalDateTime.now();
        ChatSession session1 = ChatSession.builder()
                .userId("testUser")
                .advisorId("advisor1")
                .lastMessage("Message 1")
                .lastMessageTime(baseTime.minusHours(3))
                .isActive(true)
                .build();
        ChatSession session2 = ChatSession.builder()
                .userId("testUser")
                .advisorId("advisor2")
                .lastMessage("Message 2")
                .lastMessageTime(baseTime.minusHours(1))
                .isActive(true)
                .build();
        ChatSession session3 = ChatSession.builder()
                .userId("testUser")
                .advisorId("advisor3")
                .lastMessage("Message 3")
                .lastMessageTime(baseTime.minusHours(2))
                .isActive(true)
                .build();

        chatSessionRepository.saveAll(List.of(session1, session2, session3));

        // Act
        List<ChatSession> result = chatSessionRepository.findByUserIdOrderByLastMessageTimeDesc("testUser");

        // Assert - Überprüfe die korrekte Reihenfolge (neueste zuerst)
        assertEquals(3, result.size());
        assertEquals("Message 2", result.get(0).getLastMessage()); // Neueste
        assertEquals("Message 3", result.get(1).getLastMessage());
        assertEquals("Message 1", result.get(2).getLastMessage()); // Älteste
    }
}