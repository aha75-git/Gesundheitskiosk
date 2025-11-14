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

class ChatControllerTest {

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
    private ChatController chatController;

    private final String userId = "user123";
    private final String advisorId = "advisor456";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_success() {
        // Arrange
        SendMessageRequest request = new SendMessageRequest();
        request.setContent("Test message");
//        User user = User.builder().id(userId).username("testuser").build();
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
                .senderId(userId)
                .senderName(user.getUsername())
                .senderType(SenderType.USER)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .read(false)
                .build();

        when(authInterceptor.getUserId()).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(user);
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.of(advisor));
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.empty());
        when(chatSessionService.createOrUpdateSession(any(ChatSession.class))).thenReturn(session);
        when(chatMessageService.saveMessage(any(ChatMessage.class))).thenReturn(message);
        when(chatMessageService.getUnreadCountForAdvisor(session.getId())).thenReturn(0L);

        // Act
        ResponseEntity<ApiResponse<ChatMessageDTO>> result = chatController.sendMessage(advisorId, request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("msg123", result.getBody().getData().getId());
        verify(messagingTemplate).convertAndSendToUser(
                eq(advisorId),
                eq("/queue/messages"),
                any(ChatMessageDTO.class)
        );
    }

    @Test
    void sendMessage_advisorNotFound() {
        // Arrange
        SendMessageRequest request = new SendMessageRequest();
        request.setContent("Test message");
        when(authInterceptor.getUserId()).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(new User());
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            chatController.sendMessage(advisorId, request);
        } catch (NotFoundObjectException e) {
            assertEquals("Advisor not found with advisor id: " + advisorId, e.getMessage());
        }
    }

    @Test
    void getChatHistory_success() {
        // Arrange
        ChatSession session = ChatSession.builder().build();
        session.setId("session123");

        ChatMessage message = ChatMessage.builder()
                .id("msg123")
                .content("Test message")
                .timestamp(LocalDateTime.now())
                .senderType(SenderType.ADVISOR)
                .type(MessageType.TEXT)
                .build();
        List<ChatMessage> messages = List.of(message);

        when(authInterceptor.getUserId()).thenReturn(userId);
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.of(session));
        when(chatMessageService.getChatHistory(session.getId())).thenReturn(messages);

        // Act
        ResponseEntity<ApiResponse<List<ChatMessageDTO>>> result = chatController.getChatHistory(advisorId, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().getData().size());
        verify(chatMessageService).markMessagesAsRead(session.getId(), SenderType.ADVISOR);
        verify(chatSessionService).createOrUpdateSession(session);
    }

    @Test
    void getChatHistory_sessionNotFound() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(userId);
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ApiResponse<List<ChatMessageDTO>>> result = chatController.getChatHistory(advisorId, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//        assertEquals("Failed to fetch chat history: Chat session not found", result.getBody().getError());
    }

    @Test
    void getUserChatSessions_success() {
        // Arrange
        ChatSession session = ChatSession.builder()
                .userId(userId)
                .advisorId(advisorId)
                .build();
        session.setId("session123");
        List<ChatSession> sessions = List.of(session);

        when(authInterceptor.getUserId()).thenReturn(userId);
        when(chatSessionService.getUserSessions(userId)).thenReturn(sessions);

        // Act
        ResponseEntity<ApiResponse<List<ChatSessionDTO>>> result = chatController.getUserChatSessions(userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().getData().size());
        assertEquals("session123", result.getBody().getData().get(0).getId());
    }

    @Test
    void getUserChatSessions_error() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(userId);
        when(chatSessionService.getUserSessions(userId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<ApiResponse<List<ChatSessionDTO>>> result = chatController.getUserChatSessions(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//        assertEquals("Failed to fetch sessions: Database error", result.getBody().getError());
    }

    @Test
    void markMessagesAsRead_success() {
        // Arrange
        ChatSession session = ChatSession.builder()
                .unreadCount(5)
                .build();
        session.setId("session123");

        when(authInterceptor.getUserId()).thenReturn(userId);
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.of(session));

        // Act
        ResponseEntity<ApiResponse<Void>> result = chatController.markMessagesAsRead(advisorId, userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(chatMessageService).markMessagesAsRead(session.getId(), SenderType.ADVISOR);
        verify(chatSessionService).createOrUpdateSession(session);
    }

    @Test
    void markMessagesAsRead_sessionNotFound() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(userId);
        when(chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ApiResponse<Void>> result = chatController.markMessagesAsRead(advisorId, userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//        assertEquals("Failed to mark messages as read: Chat session not found", result.getBody().getError());
    }
}