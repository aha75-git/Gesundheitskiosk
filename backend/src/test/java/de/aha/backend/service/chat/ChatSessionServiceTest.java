package de.aha.backend.service.chat;

import de.aha.backend.model.chat.ChatSession;
import de.aha.backend.repository.chat.ChatSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatSessionServiceTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private ChatMessageService chatMessageService;

    @InjectMocks
    private ChatSessionService chatSessionService;

    private final String userId = "user123";
    private final String advisorId = "advisor456";
    private final String sessionId = "session789";

    private ChatSession testSession;
    private ChatSession existingSession;

    @BeforeEach
    void setUp() {
        testSession = ChatSession.builder()
                .userId(userId)
                .advisorId(advisorId)
                .lastMessage("Test message")
                .lastMessageTime(LocalDateTime.now())
                .unreadCount(5)
                .build();

        existingSession = ChatSession.builder()
                .userId(userId)
                .advisorId(advisorId)
                .lastMessage("Old message")
                .lastMessageTime(LocalDateTime.now().minusHours(1))
                .unreadCount(2)
                .isActive(true)
                .build();
        existingSession.setId(sessionId);
        existingSession.setCreationDate(LocalDateTime.now().minusDays(1));
        existingSession.setModifyDate(LocalDateTime.now().minusHours(1));
    }

    @Test
    void createOrUpdateSession_createNewSession() {
        // Arrange
        when(chatSessionRepository.findByUserIdAndAdvisorId(userId, advisorId))
                .thenReturn(Optional.empty());
        when(chatSessionRepository.save(any(ChatSession.class))).thenAnswer(invocation -> {
            ChatSession session = invocation.getArgument(0);
            session.setId(sessionId);
            return session;
        });

        // Act
        ChatSession result = chatSessionService.createOrUpdateSession(testSession);

        // Assert
        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals(advisorId, result.getAdvisorId());
        assertTrue(result.getIsActive());
        assertEquals(0, result.getUnreadCount()); // Should be reset to 0 for new session
        assertNotNull(result.getCreationDate());
        assertNotNull(result.getModifyDate());

        verify(chatSessionRepository).findByUserIdAndAdvisorId(userId, advisorId);
        verify(chatSessionRepository).save(any(ChatSession.class));
    }

    @Test
    void createOrUpdateSession_updateExistingSession() {
        // Arrange
        when(chatSessionRepository.findByUserIdAndAdvisorId(userId, advisorId))
                .thenReturn(Optional.of(existingSession));
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(existingSession);

        // Act
        ChatSession result = chatSessionService.createOrUpdateSession(testSession);

        // Assert
        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        assertEquals(testSession.getLastMessage(), result.getLastMessage());
        assertEquals(testSession.getUnreadCount(), result.getUnreadCount());
        assertTrue(result.getIsActive());
        assertNotNull(result.getModifyDate());

        verify(chatSessionRepository).findByUserIdAndAdvisorId(userId, advisorId);
        verify(chatSessionRepository).save(existingSession);
    }

    @Test
    void getUserSessions_success() {
        // Arrange
        List<ChatSession> sessions = List.of(existingSession);
        when(chatSessionRepository.findByUserIdOrderByLastMessageTimeDesc(userId))
                .thenReturn(sessions);
        when(chatMessageService.getUnreadCountForUser(sessionId)).thenReturn(3L);
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(existingSession);

        // Act
        List<ChatSession> result = chatSessionService.getUserSessions(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getUnreadCount()); // Updated unread count

        verify(chatSessionRepository).findByUserIdOrderByLastMessageTimeDesc(userId);
        verify(chatMessageService).getUnreadCountForUser(sessionId);
        verify(chatSessionRepository).save(existingSession);
    }

    @Test
    void getUserSessions_noUpdateWhenUnreadCountSame() {
        // Arrange
        List<ChatSession> sessions = List.of(existingSession);
        when(chatSessionRepository.findByUserIdOrderByLastMessageTimeDesc(userId))
                .thenReturn(sessions);
        when(chatMessageService.getUnreadCountForUser(sessionId)).thenReturn(2L); // Same as existing

        // Act
        List<ChatSession> result = chatSessionService.getUserSessions(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getUnreadCount()); // Unchanged

        verify(chatSessionRepository).findByUserIdOrderByLastMessageTimeDesc(userId);
        verify(chatMessageService).getUnreadCountForUser(sessionId);
        verify(chatSessionRepository, never()).save(any(ChatSession.class));
    }

    @Test
    void getAdvisorSessions_success() {
        // Arrange
        List<ChatSession> sessions = List.of(existingSession);
        when(chatSessionRepository.findByAdvisorIdOrderByLastMessageTimeDesc(advisorId))
                .thenReturn(sessions);
        when(chatMessageService.getUnreadCountForAdvisor(sessionId)).thenReturn(4L);
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(existingSession);

        // Act
        List<ChatSession> result = chatSessionService.getAdvisorSessions(advisorId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getUnreadCount()); // Updated unread count

        verify(chatSessionRepository).findByAdvisorIdOrderByLastMessageTimeDesc(advisorId);
        verify(chatMessageService).getUnreadCountForAdvisor(sessionId);
        verify(chatSessionRepository).save(existingSession);
    }

    @Test
    void getAdvisorSessions_noUpdateWhenUnreadCountSame() {
        // Arrange
        List<ChatSession> sessions = List.of(existingSession);
        when(chatSessionRepository.findByAdvisorIdOrderByLastMessageTimeDesc(advisorId))
                .thenReturn(sessions);
        when(chatMessageService.getUnreadCountForAdvisor(sessionId)).thenReturn(2L); // Same as existing

        // Act
        List<ChatSession> result = chatSessionService.getAdvisorSessions(advisorId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getUnreadCount()); // Unchanged

        verify(chatSessionRepository).findByAdvisorIdOrderByLastMessageTimeDesc(advisorId);
        verify(chatMessageService).getUnreadCountForAdvisor(sessionId);
        verify(chatSessionRepository, never()).save(any(ChatSession.class));
    }

    @Test
    void getSession_success() {
        // Arrange
        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.of(existingSession));

        // Act
        Optional<ChatSession> result = chatSessionService.getSession(sessionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sessionId, result.get().getId());
        verify(chatSessionRepository).findById(sessionId);
    }

    @Test
    void getSession_notFound() {
        // Arrange
        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act
        Optional<ChatSession> result = chatSessionService.getSession(sessionId);

        // Assert
        assertFalse(result.isPresent());
        verify(chatSessionRepository).findById(sessionId);
    }

    @Test
    void getSessionByUserAndAdvisor_success() {
        // Arrange
        when(chatSessionRepository.findByUserIdAndAdvisorId(userId, advisorId))
                .thenReturn(Optional.of(existingSession));

        // Act
        Optional<ChatSession> result = chatSessionService.getSessionByUserAndAdvisor(userId, advisorId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sessionId, result.get().getId());
        verify(chatSessionRepository).findByUserIdAndAdvisorId(userId, advisorId);
    }

    @Test
    void getSessionByUserAndAdvisor_notFound() {
        // Arrange
        when(chatSessionRepository.findByUserIdAndAdvisorId(userId, advisorId))
                .thenReturn(Optional.empty());

        // Act
        Optional<ChatSession> result = chatSessionService.getSessionByUserAndAdvisor(userId, advisorId);

        // Assert
        assertFalse(result.isPresent());
        verify(chatSessionRepository).findByUserIdAndAdvisorId(userId, advisorId);
    }

    @Test
    void deactivateSession_success() {
        // Arrange
        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.of(existingSession));
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(existingSession);

        // Act
        chatSessionService.deactivateSession(sessionId);

        // Assert
        assertFalse(existingSession.getIsActive());
        assertNotNull(existingSession.getModifyDate());
        verify(chatSessionRepository).findById(sessionId);
        verify(chatSessionRepository).save(existingSession);
    }

    @Test
    void deactivateSession_sessionNotFound() {
        // Arrange
        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act
        chatSessionService.deactivateSession(sessionId);

        // Assert
        verify(chatSessionRepository).findById(sessionId);
        verify(chatSessionRepository, never()).save(any(ChatSession.class));
    }

    @Test
    void sessionExists_true() {
        // Arrange
        when(chatSessionRepository.existsByUserIdAndAdvisorId(userId, advisorId)).thenReturn(true);

        // Act
        boolean result = chatSessionService.sessionExists(userId, advisorId);

        // Assert
        assertTrue(result);
        verify(chatSessionRepository).existsByUserIdAndAdvisorId(userId, advisorId);
    }

    @Test
    void sessionExists_false() {
        // Arrange
        when(chatSessionRepository.existsByUserIdAndAdvisorId(userId, advisorId)).thenReturn(false);

        // Act
        boolean result = chatSessionService.sessionExists(userId, advisorId);

        // Assert
        assertFalse(result);
        verify(chatSessionRepository).existsByUserIdAndAdvisorId(userId, advisorId);
    }

    @Test
    void createOrUpdateSession_verifyTimestampsForNewSession() {
        // Arrange
        LocalDateTime beforeTest = LocalDateTime.now();
        when(chatSessionRepository.findByUserIdAndAdvisorId(userId, advisorId))
                .thenReturn(Optional.empty());
        when(chatSessionRepository.save(any(ChatSession.class))).thenAnswer(invocation -> {
            ChatSession session = invocation.getArgument(0);
            session.setId(sessionId);
            return session;
        });

        // Act
        ChatSession result = chatSessionService.createOrUpdateSession(testSession);
        LocalDateTime afterTest = LocalDateTime.now();

        // Assert
        assertNotNull(result.getCreationDate());
        assertNotNull(result.getModifyDate());
        assertFalse(result.getCreationDate().isBefore(beforeTest));
        assertFalse(result.getCreationDate().isAfter(afterTest));
        assertFalse(result.getModifyDate().isBefore(beforeTest));
        assertFalse(result.getModifyDate().isAfter(afterTest));
    }

    @Test
    void createOrUpdateSession_verifyTimestampsForUpdatedSession() {
        // Arrange
        LocalDateTime originalCreationDate = existingSession.getCreationDate();
        when(chatSessionRepository.findByUserIdAndAdvisorId(userId, advisorId))
                .thenReturn(Optional.of(existingSession));
        when(chatSessionRepository.save(any(ChatSession.class))).thenReturn(existingSession);

        // Act
        ChatSession result = chatSessionService.createOrUpdateSession(testSession);

        // Assert
        assertEquals(originalCreationDate, result.getCreationDate()); // Creation date should not change
        assertNotNull(result.getModifyDate());
        assertTrue(result.getModifyDate().isAfter(originalCreationDate)); // Modify date should be updated
    }
}