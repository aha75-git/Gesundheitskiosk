package de.aha.backend.controller.chat;

import de.aha.backend.dto.chat.ApiResponse;
import de.aha.backend.dto.chat.ChatMessageDTO;
import de.aha.backend.dto.chat.ChatSessionDTO;
import de.aha.backend.dto.chat.SendMessageRequest;
import de.aha.backend.exception.NotFoundObjectException;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.chat.ChatMessage;
import de.aha.backend.model.chat.ChatSession;
import de.aha.backend.model.chat.MessageType;
import de.aha.backend.model.chat.SenderType;
import de.aha.backend.model.user.User;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.service.AdvisorService;
import de.aha.backend.service.UserService;
import de.aha.backend.service.chat.ChatMessageService;
import de.aha.backend.service.chat.ChatSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class AdvisorChatControllerTest {

    @Mock
    private ChatMessageService chatMessageService;

    @Mock
    private ChatSessionService chatSessionService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private AuthInterceptor authInterceptor;

    @Mock
    private UserService userService;

    @Mock
    private AdvisorService advisorService;

    @InjectMocks
    private AdvisorChatController advisorChatController;

    private final String advisorId = "advisor456";
    private final String userId = "user123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessageToUser_success() {
        // Arrange
        SendMessageRequest request = new SendMessageRequest();
        request.setContent("Test message");
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        Advisor advisor = Advisor.builder().name("Test Advisor").build();
        advisor.setId(advisorId);

        ChatSession session = ChatSession.builder()
                .userId(userId)
                .advisorId(advisorId)
                .build();
        session.setId("session123");

        ChatMessage message = ChatMessage.builder()
                .id("msg123")
                .sessionId(session.getId())
                .content(request.getContent())
                .senderId(advisorId)
                .senderName(advisor.getName())
                .senderType(SenderType.ADVISOR)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .read(false)
                .build();

        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(userService.findById(userId)).thenReturn(user);
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.of(advisor));
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.empty());
        when(chatSessionService.createOrUpdateSession(any(ChatSession.class))).thenReturn(session);
        when(chatMessageService.saveMessage(any(ChatMessage.class))).thenReturn(message);
        when(chatMessageService.getUnreadCountForUser(session.getId())).thenReturn(1L);

        // Act
        ResponseEntity<ApiResponse<ChatMessageDTO>> result = advisorChatController.sendMessageToUser(userId, request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("msg123", result.getBody().getData().getId());
        verify(messagingTemplate).convertAndSendToUser(
                eq(userId),
                eq("/queue/messages"),
                any(ChatMessageDTO.class)
        );
    }

    @Test
    void sendMessageToUser_advisorNotFound() {
        // Arrange
        SendMessageRequest request = new SendMessageRequest();
        request.setContent("Test message");
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(userService.findById(userId)).thenReturn(new User());
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            advisorChatController.sendMessageToUser(userId, request);
        } catch (NotFoundObjectException e) {
            assertEquals("Advisor not found with advisor id: " + advisorId, e.getMessage());
        }
    }

    @Test
    void sendMessageToUser_existingSession() {
        // Arrange
        SendMessageRequest request = new SendMessageRequest();
        request.setContent("Test message");
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        Advisor advisor = Advisor.builder().name("Test Advisor").build();
        advisor.setId(advisorId);
        ChatSession existingSession = ChatSession.builder()
                .userId(userId)
                .advisorId(advisorId)
                .build();
        existingSession.setId("existingSession123");
        ChatMessage message = ChatMessage.builder()
                .id("msg123")
                .sessionId(existingSession.getId())
                .content(request.getContent())
                .senderId(advisorId)
                .senderName(advisor.getName())
                .senderType(SenderType.ADVISOR)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .read(false)
                .build();

        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(userService.findById(userId)).thenReturn(user);
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.of(advisor));
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.of(existingSession));
        when(chatSessionService.createOrUpdateSession(any(ChatSession.class))).thenReturn(existingSession);
        when(chatMessageService.saveMessage(any(ChatMessage.class))).thenReturn(message);
        when(chatMessageService.getUnreadCountForUser(existingSession.getId())).thenReturn(1L);

        // Act
        ResponseEntity<ApiResponse<ChatMessageDTO>> result = advisorChatController.sendMessageToUser(userId, request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("msg123", result.getBody().getData().getId());
        verify(chatSessionService).getSessionByUserAndAdvisor(userId, advisorId);
    }

    @Test
    void getChatHistoryForAdvisor_success() {
        // Arrange
        ChatSession session = ChatSession.builder().build();
        session.setId("session123");
        ChatMessage message1 = ChatMessage.builder()
                .id("msg1")
                .content("User message")
                .senderType(SenderType.USER)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .build();
        ChatMessage message2 = ChatMessage.builder()
                .id("msg2")
                .content("Advisor message")
                .senderType(SenderType.ADVISOR)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .build();
        List<ChatMessage> messages = List.of(message1, message2);

        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.of(session));
        when(chatMessageService.getChatHistory(session.getId())).thenReturn(messages);

        // Act
        ResponseEntity<ApiResponse<List<ChatMessageDTO>>> result =
                advisorChatController.getChatHistoryForAdvisor(userId, advisorId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().getData().size());
        verify(chatMessageService).markMessagesAsRead(session.getId(), SenderType.USER);
    }

    @Test
    void getChatHistoryForAdvisor_sessionNotFound() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ApiResponse<List<ChatMessageDTO>>> result =
                advisorChatController.getChatHistoryForAdvisor(userId, advisorId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//        assertEquals("Failed to fetch chat history: Chat session not found", result.getBody().getError());
    }

    @Test
    void getAdvisorChatSessions_success() {
        // Arrange
        ChatSession session1 = ChatSession.builder()
                .userId("user1")
                .advisorId(advisorId)
                .unreadCount(2)
                .build();
        session1.setId("session1");
        ChatSession session2 = ChatSession.builder()
                .userId("user2")
                .advisorId(advisorId)
                .unreadCount(0)
                .build();
        session2.setId("session2");
        List<ChatSession> sessions = List.of(session1, session2);

        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(chatSessionService.getAdvisorSessions(advisorId)).thenReturn(sessions);

        // Act
        ResponseEntity<ApiResponse<List<ChatSessionDTO>>> result =
                advisorChatController.getAdvisorChatSessions(advisorId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().getData().size());
    }

    @Test
    void getAdvisorChatSessions_error() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(chatSessionService.getAdvisorSessions(advisorId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<ApiResponse<List<ChatSessionDTO>>> result =
                advisorChatController.getAdvisorChatSessions(advisorId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//        assertEquals("Failed to fetch sessions: Database error", result.getBody().getError());
    }

    @Test
    void markMessagesAsReadByAdvisor_success() {
        // Arrange
        ChatSession session = ChatSession.builder()
                .userId(userId)
                .advisorId(advisorId)
                .build();
        session.setId("session123");

        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.of(session));

        // Act
        ResponseEntity<ApiResponse<Void>> result =
                advisorChatController.markMessagesAsReadByAdvisor(userId, advisorId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(chatMessageService).markMessagesAsRead(session.getId(), SenderType.USER);
    }

    @Test
    void markMessagesAsReadByAdvisor_sessionNotFound() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ApiResponse<Void>> result =
                advisorChatController.markMessagesAsReadByAdvisor(userId, advisorId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//        assertEquals("Failed to mark messages as read: Chat session not found", result.getBody().getError());
    }

    @Test
    void sendMessageToUser_withAudioUrl() {
        // Arrange
        String audioUrl = "https://example.com/audio.mp3";
        SendMessageRequest request = new SendMessageRequest();
        request.setContent("Test message");
        request.setAudioUrl(audioUrl);

        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        Advisor advisor = Advisor.builder().name("Test Advisor").build();
        advisor.setId(advisorId);

        ChatSession session = ChatSession.builder()
                .userId(userId)
                .advisorId(advisorId)
                .build();
        session.setId("session123");

        ChatMessage message = ChatMessage.builder()
                .id("msg123")
                .sessionId(session.getId())
                .content(request.getContent())
                .senderId(advisorId)
                .senderName(advisor.getName())
                .senderType(SenderType.ADVISOR)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .read(false)
                .audioUrl(audioUrl)
                .build();

        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(userService.findById(userId)).thenReturn(user);
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.of(advisor));
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.of(session));
        when(chatSessionService.createOrUpdateSession(any(ChatSession.class))).thenReturn(session);
        when(chatMessageService.saveMessage(any(ChatMessage.class))).thenReturn(message);
        when(chatMessageService.getUnreadCountForUser(session.getId())).thenReturn(1L);

        // Act
        ResponseEntity<ApiResponse<ChatMessageDTO>> result = advisorChatController.sendMessageToUser(userId, request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("msg123", result.getBody().getData().getId());
        verify(chatMessageService).saveMessage(any(ChatMessage.class));
    }
}