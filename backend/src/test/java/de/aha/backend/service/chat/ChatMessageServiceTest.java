package de.aha.backend.service.chat;

import de.aha.backend.model.chat.ChatMessage;
import de.aha.backend.model.chat.MessageType;
import de.aha.backend.model.chat.SenderType;
import de.aha.backend.repository.chat.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    private final String sessionId = "session123";
    private ChatMessage testMessage;
    private ChatMessage advisorMessage;
    private ChatMessage userMessage;

    @BeforeEach
    void setUp() {
        testMessage = ChatMessage.builder()
                .id("msg123")
                .sessionId(sessionId)
                .content("Test message")
                .senderId("sender123")
                .senderName("Test User")
                .senderType(SenderType.USER)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .read(false)
                .build();

        advisorMessage = ChatMessage.builder()
                .id("msg456")
                .sessionId(sessionId)
                .content("Advisor message")
                .senderId("advisor456")
                .senderName("Test Advisor")
                .senderType(SenderType.ADVISOR)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .read(false)
                .build();

        userMessage = ChatMessage.builder()
                .id("msg789")
                .sessionId(sessionId)
                .content("User message")
                .senderId("user123")
                .senderName("Test User")
                .senderType(SenderType.USER)
                .timestamp(LocalDateTime.now())
                .type(MessageType.TEXT)
                .read(false)
                .build();
    }

    @Test
    void saveMessage_success() {
        // Arrange
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(testMessage);

        // Act
        ChatMessage result = chatMessageService.saveMessage(testMessage);

        // Assert
        assertNotNull(result);
        assertEquals("msg123", result.getId());
        assertEquals(sessionId, result.getSessionId());
        assertEquals("Test message", result.getContent());
        assertEquals(SenderType.USER, result.getSenderType());

        verify(chatMessageRepository).save(testMessage);
    }

    @Test
    void saveMessage_setsTimestampWhenNull() {
        // Arrange
        testMessage.setTimestamp(null);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(testMessage);

        // Act
        ChatMessage result = chatMessageService.saveMessage(testMessage);

        // Assert
        assertNotNull(result.getTimestamp());
        verify(chatMessageRepository).save(testMessage);
    }

    @Test
    void saveMessage_preservesTimestampWhenNotNull() {
        // Arrange
        LocalDateTime originalTimestamp = LocalDateTime.now().minusMinutes(5);
        testMessage.setTimestamp(originalTimestamp);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(testMessage);

        // Act
        ChatMessage result = chatMessageService.saveMessage(testMessage);

        // Assert
        assertEquals(originalTimestamp, result.getTimestamp());
        verify(chatMessageRepository).save(testMessage);
    }

    @Test
    void getChatHistory_success() {
        // Arrange
        List<ChatMessage> expectedMessages = Arrays.asList(testMessage, advisorMessage);
        when(chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId))
                .thenReturn(expectedMessages);

        // Act
        List<ChatMessage> result = chatMessageService.getChatHistory(sessionId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(chatMessageRepository).findBySessionIdOrderByTimestampAsc(sessionId);
    }

    @Test
    void getChatHistory_emptyList() {
        // Arrange
        when(chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId))
                .thenReturn(Arrays.asList());

        // Act
        List<ChatMessage> result = chatMessageService.getChatHistory(sessionId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(chatMessageRepository).findBySessionIdOrderByTimestampAsc(sessionId);
    }

    @Test
    void markMessagesAsRead_success() {
        // Arrange
        List<ChatMessage> unreadMessages = Arrays.asList(advisorMessage, userMessage);
        when(chatMessageRepository.findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.ADVISOR))
                .thenReturn(unreadMessages);
        when(chatMessageRepository.saveAll(any())).thenReturn(unreadMessages);

        // Act
        chatMessageService.markMessagesAsRead(sessionId, SenderType.ADVISOR);

        // Assert
        assertTrue(advisorMessage.isRead());
        assertTrue(userMessage.isRead());
        verify(chatMessageRepository).findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.ADVISOR);
        verify(chatMessageRepository).saveAll(unreadMessages);
    }

    @Test
    void markMessagesAsRead_noUnreadMessages() {
        // Arrange
        when(chatMessageRepository.findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER))
                .thenReturn(Arrays.asList());

        // Act
        chatMessageService.markMessagesAsRead(sessionId, SenderType.USER);

        // Assert
        verify(chatMessageRepository).findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER);
        verify(chatMessageRepository, never()).saveAll(any());
    }

    @Test
    void getUnreadCountForUser_success() {
        // Arrange
        long expectedCount = 3L;
        when(chatMessageRepository.countUnreadMessagesBySessionAndSenderType(sessionId, SenderType.ADVISOR))
                .thenReturn(expectedCount);

        // Act
        long result = chatMessageService.getUnreadCountForUser(sessionId);

        // Assert
        assertEquals(expectedCount, result);
        verify(chatMessageRepository).countUnreadMessagesBySessionAndSenderType(sessionId, SenderType.ADVISOR);
    }

    @Test
    void getUnreadCountForAdvisor_success() {
        // Arrange
        long expectedCount = 5L;
        when(chatMessageRepository.countUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER))
                .thenReturn(expectedCount);

        // Act
        long result = chatMessageService.getUnreadCountForAdvisor(sessionId);

        // Assert
        assertEquals(expectedCount, result);
        verify(chatMessageRepository).countUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER);
    }

    @Test
    void getUnreadAdvisorMessages_success() {
        // Arrange
        List<ChatMessage> expectedMessages = Arrays.asList(advisorMessage);
        when(chatMessageRepository.findUnreadAdvisorMessages(sessionId))
                .thenReturn(expectedMessages);

        // Act
        List<ChatMessage> result = chatMessageService.getUnreadAdvisorMessages(sessionId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SenderType.ADVISOR, result.get(0).getSenderType());
        verify(chatMessageRepository).findUnreadAdvisorMessages(sessionId);
    }

    @Test
    void getUnreadUserMessages_success() {
        // Arrange
        List<ChatMessage> expectedMessages = Arrays.asList(userMessage);
        when(chatMessageRepository.findUnreadUserMessages(sessionId))
                .thenReturn(expectedMessages);

        // Act
        List<ChatMessage> result = chatMessageService.getUnreadUserMessages(sessionId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SenderType.USER, result.get(0).getSenderType());
        verify(chatMessageRepository).findUnreadUserMessages(sessionId);
    }

    @Test
    void markMessagesAsRead_verifiesReadStatusChange() {
        // Arrange
        advisorMessage.setRead(false);
        userMessage.setRead(false);
        List<ChatMessage> unreadMessages = Arrays.asList(advisorMessage, userMessage);

        when(chatMessageRepository.findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER))
                .thenReturn(unreadMessages);
        when(chatMessageRepository.saveAll(any())).thenReturn(unreadMessages);

        // Act
        chatMessageService.markMessagesAsRead(sessionId, SenderType.USER);

        // Assert
        unreadMessages.forEach(message -> assertTrue(message.isRead()));
        verify(chatMessageRepository).saveAll(unreadMessages);
    }

    @Test
    void getUnreadCountForUser_zeroCount() {
        // Arrange
        when(chatMessageRepository.countUnreadMessagesBySessionAndSenderType(sessionId, SenderType.ADVISOR))
                .thenReturn(0L);

        // Act
        long result = chatMessageService.getUnreadCountForUser(sessionId);

        // Assert
        assertEquals(0L, result);
        verify(chatMessageRepository).countUnreadMessagesBySessionAndSenderType(sessionId, SenderType.ADVISOR);
    }

    @Test
    void getUnreadCountForAdvisor_zeroCount() {
        // Arrange
        when(chatMessageRepository.countUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER))
                .thenReturn(0L);

        // Act
        long result = chatMessageService.getUnreadCountForAdvisor(sessionId);

        // Assert
        assertEquals(0L, result);
        verify(chatMessageRepository).countUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER);
    }

    @Test
    void saveMessage_withAudioUrl() {
        // Arrange
        String audioUrl = "https://example.com/audio.mp3";
        testMessage.setAudioUrl(audioUrl);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(testMessage);

        // Act
        ChatMessage result = chatMessageService.saveMessage(testMessage);

        // Assert
        assertNotNull(result);
        assertEquals(audioUrl, result.getAudioUrl());
        verify(chatMessageRepository).save(testMessage);
    }

    @Test
    void markMessagesAsRead_differentSenderTypes() {
        // Test for USER sender type
        List<ChatMessage> userMessages = Arrays.asList(userMessage);
        when(chatMessageRepository.findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER))
                .thenReturn(userMessages);
        when(chatMessageRepository.saveAll(any())).thenReturn(userMessages);

        chatMessageService.markMessagesAsRead(sessionId, SenderType.USER);

        verify(chatMessageRepository).findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.USER);
        verify(chatMessageRepository).saveAll(userMessages);

        // Test for ADVISOR sender type
        List<ChatMessage> advisorMessages = Arrays.asList(advisorMessage);
        when(chatMessageRepository.findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.ADVISOR))
                .thenReturn(advisorMessages);
        when(chatMessageRepository.saveAll(any())).thenReturn(advisorMessages);

        chatMessageService.markMessagesAsRead(sessionId, SenderType.ADVISOR);

        verify(chatMessageRepository).findUnreadMessagesBySessionAndSenderType(sessionId, SenderType.ADVISOR);
        verify(chatMessageRepository).saveAll(advisorMessages);
    }

    @Test
    void getUnreadMessages_emptyLists() {
        // Arrange
        when(chatMessageRepository.findUnreadAdvisorMessages(sessionId))
                .thenReturn(Arrays.asList());
        when(chatMessageRepository.findUnreadUserMessages(sessionId))
                .thenReturn(Arrays.asList());

        // Act
        List<ChatMessage> advisorResult = chatMessageService.getUnreadAdvisorMessages(sessionId);
        List<ChatMessage> userResult = chatMessageService.getUnreadUserMessages(sessionId);

        // Assert
        assertTrue(advisorResult.isEmpty());
        assertTrue(userResult.isEmpty());

        verify(chatMessageRepository).findUnreadAdvisorMessages(sessionId);
        verify(chatMessageRepository).findUnreadUserMessages(sessionId);
    }
}