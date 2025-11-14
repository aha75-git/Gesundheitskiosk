package de.aha.backend.repository.chat;

import de.aha.backend.model.chat.ChatMessage;
import de.aha.backend.model.chat.MessageType;
import de.aha.backend.model.chat.SenderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = {"spring.mongodb.embedded.version=4.0.12"})
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private final String sessionId1 = "session123";
    private final String sessionId2 = "session456";

    private ChatMessage userMessageRead;
    private ChatMessage userMessageUnread;
    private ChatMessage advisorMessageRead;
    private ChatMessage advisorMessageUnread;
    private ChatMessage userMessageSession2;
    private ChatMessage advisorMessageSession2;

    @BeforeEach
    void setUp() {
        chatMessageRepository.deleteAll();

        LocalDateTime now = LocalDateTime.now();

        // Nachrichten für Session 1
        userMessageRead = ChatMessage.builder()
                .sessionId(sessionId1)
                .content("User message read")
                .senderId("user1")
                .senderName("User One")
                .senderType(SenderType.USER)
                .timestamp(now.minusMinutes(30))
                .type(MessageType.TEXT)
                .read(true)
                .build();

        userMessageUnread = ChatMessage.builder()
                .sessionId(sessionId1)
                .content("User message unread")
                .senderId("user1")
                .senderName("User One")
                .senderType(SenderType.USER)
                .timestamp(now.minusMinutes(20))
                .type(MessageType.TEXT)
                .read(false)
                .build();

        advisorMessageRead = ChatMessage.builder()
                .sessionId(sessionId1)
                .content("Advisor message read")
                .senderId("advisor1")
                .senderName("Advisor One")
                .senderType(SenderType.ADVISOR)
                .timestamp(now.minusMinutes(15))
                .type(MessageType.TEXT)
                .read(true)
                .build();

        advisorMessageUnread = ChatMessage.builder()
                .sessionId(sessionId1)
                .content("Advisor message unread")
                .senderId("advisor1")
                .senderName("Advisor One")
                .senderType(SenderType.ADVISOR)
                .timestamp(now.minusMinutes(10))
                .type(MessageType.TEXT)
                .read(false)
                .build();

        // Nachrichten für Session 2
        userMessageSession2 = ChatMessage.builder()
                .sessionId(sessionId2)
                .content("User message session 2")
                .senderId("user2")
                .senderName("User Two")
                .senderType(SenderType.USER)
                .timestamp(now.minusMinutes(5))
                .type(MessageType.TEXT)
                .read(false)
                .build();

        advisorMessageSession2 = ChatMessage.builder()
                .sessionId(sessionId2)
                .content("Advisor message session 2")
                .senderId("advisor1")
                .senderName("Advisor One")
                .senderType(SenderType.ADVISOR)
                .timestamp(now)
                .type(MessageType.TEXT)
                .read(false)
                .build();

        // Speichere alle Testnachrichten
        chatMessageRepository.saveAll(List.of(
                userMessageRead, userMessageUnread, advisorMessageRead,
                advisorMessageUnread, userMessageSession2, advisorMessageSession2
        ));
    }

    @Test
    void findBySessionIdOrderByTimestampAsc_success() {
        // Act
        List<ChatMessage> result = chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId1);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());

        // Überprüfe die Sortierung (aufsteigend nach Timestamp)
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getTimestamp().isBefore(result.get(i + 1).getTimestamp()));
        }

        // Überprüfe, dass nur Nachrichten der richtigen Session zurückgegeben werden
        result.forEach(message -> assertEquals(sessionId1, message.getSessionId()));
    }

    @Test
    void findBySessionIdOrderByTimestampAsc_sessionNotFound() {
        // Act
        List<ChatMessage> result = chatMessageRepository.findBySessionIdOrderByTimestampAsc("nonexistent");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findUnreadMessagesBySessionAndSenderType_userMessages() {
        // Act
        List<ChatMessage> result = chatMessageRepository
                .findUnreadMessagesBySessionAndSenderType(sessionId1, SenderType.USER);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SenderType.USER, result.get(0).getSenderType());
        assertFalse(result.get(0).isRead());
        assertEquals("User message unread", result.get(0).getContent());
    }

    @Test
    void findUnreadMessagesBySessionAndSenderType_advisorMessages() {
        // Act
        List<ChatMessage> result = chatMessageRepository
                .findUnreadMessagesBySessionAndSenderType(sessionId1, SenderType.ADVISOR);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SenderType.ADVISOR, result.get(0).getSenderType());
        assertFalse(result.get(0).isRead());
        assertEquals("Advisor message unread", result.get(0).getContent());
    }

    @Test
    void findUnreadMessagesBySessionAndSenderType_noUnreadMessages() {
        // Arrange - Erstelle eine Session ohne ungelesene Nachrichten
        String emptySessionId = "emptySession";
        ChatMessage readUserMessage = ChatMessage.builder()
                .sessionId(emptySessionId)
                .content("Read message")
                .senderType(SenderType.USER)
                .timestamp(LocalDateTime.now())
                .read(true)
                .build();
        chatMessageRepository.save(readUserMessage);

        // Act
        List<ChatMessage> result = chatMessageRepository
                .findUnreadMessagesBySessionAndSenderType(emptySessionId, SenderType.USER);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void countUnreadMessagesBySessionAndSenderType_userMessages() {
        // Act
        long result = chatMessageRepository
                .countUnreadMessagesBySessionAndSenderType(sessionId1, SenderType.USER);

        // Assert
        assertEquals(1L, result);
    }

    @Test
    void countUnreadMessagesBySessionAndSenderType_advisorMessages() {
        // Act
        long result = chatMessageRepository
                .countUnreadMessagesBySessionAndSenderType(sessionId1, SenderType.ADVISOR);

        // Assert
        assertEquals(1L, result);
    }

    @Test
    void countUnreadMessagesBySessionAndSenderType_multipleUnread() {
        // Arrange - Füge weitere ungelesene User-Nachrichten hinzu
        ChatMessage additionalUnreadUserMessage = ChatMessage.builder()
                .sessionId(sessionId1)
                .content("Additional unread user message")
                .senderType(SenderType.USER)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();
        chatMessageRepository.save(additionalUnreadUserMessage);

        // Act
        long result = chatMessageRepository
                .countUnreadMessagesBySessionAndSenderType(sessionId1, SenderType.USER);

        // Assert
        assertEquals(2L, result);
    }

    @Test
    void countUnreadMessagesBySessionAndSenderType_noUnreadMessages() {
        // Act
        long result = chatMessageRepository
                .countUnreadMessagesBySessionAndSenderType("nonexistent", SenderType.USER);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void findUnreadAdvisorMessages_success() {
        // Act
        List<ChatMessage> result = chatMessageRepository.findUnreadAdvisorMessages(sessionId1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SenderType.ADVISOR, result.get(0).getSenderType());
        assertFalse(result.get(0).isRead());
        assertEquals("Advisor message unread", result.get(0).getContent());
    }

    @Test
    void findUnreadAdvisorMessages_multipleSessions() {
        // Act - Überprüfe Session 2
        List<ChatMessage> result = chatMessageRepository.findUnreadAdvisorMessages(sessionId2);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SenderType.ADVISOR, result.get(0).getSenderType());
        assertEquals(sessionId2, result.get(0).getSessionId());
    }

    @Test
    void findUnreadAdvisorMessages_noUnreadMessages() {
        // Arrange - Markiere alle Advisor-Nachrichten als gelesen
        advisorMessageUnread.setRead(true);
        advisorMessageSession2.setRead(true);
        chatMessageRepository.saveAll(List.of(advisorMessageUnread, advisorMessageSession2));

        // Act
        List<ChatMessage> result = chatMessageRepository.findUnreadAdvisorMessages(sessionId1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findUnreadUserMessages_success() {
        // Act
        List<ChatMessage> result = chatMessageRepository.findUnreadUserMessages(sessionId1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SenderType.USER, result.get(0).getSenderType());
        assertFalse(result.get(0).isRead());
        assertEquals("User message unread", result.get(0).getContent());
    }

    @Test
    void findUnreadUserMessages_multipleSessions() {
        // Act - Überprüfe Session 2
        List<ChatMessage> result = chatMessageRepository.findUnreadUserMessages(sessionId2);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(SenderType.USER, result.get(0).getSenderType());
        assertEquals(sessionId2, result.get(0).getSessionId());
    }

    @Test
    void findUnreadUserMessages_noUnreadMessages() {
        // Arrange - Markiere alle User-Nachrichten als gelesen
        userMessageUnread.setRead(true);
        userMessageSession2.setRead(true);
        chatMessageRepository.saveAll(List.of(userMessageUnread, userMessageSession2));

        // Act
        List<ChatMessage> result = chatMessageRepository.findUnreadUserMessages(sessionId1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void saveAndFindById_success() {
        // Arrange
        ChatMessage newMessage = ChatMessage.builder()
                .sessionId("newSession")
                .content("New message")
                .senderType(SenderType.USER)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();

        // Act
        ChatMessage savedMessage = chatMessageRepository.save(newMessage);
        ChatMessage foundMessage = chatMessageRepository.findById(savedMessage.getId()).orElse(null);

        // Assert
        assertNotNull(foundMessage);
        assertEquals(savedMessage.getId(), foundMessage.getId());
        assertEquals("New message", foundMessage.getContent());
        assertEquals(SenderType.USER, foundMessage.getSenderType());
    }

    @Test
    void deleteMessage_success() {
        // Arrange
        String messageId = userMessageRead.getId();

        // Act
        chatMessageRepository.deleteById(messageId);
        boolean exists = chatMessageRepository.existsById(messageId);

        // Assert
        assertFalse(exists);
    }

    @Test
    void findAllMessages_success() {
        // Act
        List<ChatMessage> result = chatMessageRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(6, result.size()); // Alle 6 Test-Nachrichten
    }

    @Test
    void findBySessionIdOrderByTimestampAsc_verifyOrderWithSpecificTimestamps() {
        // Arrange - Erstelle Nachrichten mit spezifischen Zeitstempeln
        LocalDateTime baseTime = LocalDateTime.now();
        ChatMessage message1 = ChatMessage.builder()
                .sessionId("orderTestSession")
                .content("First message")
                .senderType(SenderType.USER)
                .timestamp(baseTime.minusMinutes(10))
                .read(true)
                .build();
        ChatMessage message2 = ChatMessage.builder()
                .sessionId("orderTestSession")
                .content("Second message")
                .senderType(SenderType.ADVISOR)
                .timestamp(baseTime.minusMinutes(5))
                .read(true)
                .build();
        ChatMessage message3 = ChatMessage.builder()
                .sessionId("orderTestSession")
                .content("Third message")
                .senderType(SenderType.USER)
                .timestamp(baseTime)
                .read(true)
                .build();

        chatMessageRepository.saveAll(List.of(message2, message1, message3)); // In beliebiger Reihenfolge speichern

        // Act
        List<ChatMessage> result = chatMessageRepository.findBySessionIdOrderByTimestampAsc("orderTestSession");

        // Assert - Überprüfe die korrekte aufsteigende Reihenfolge
        assertEquals(3, result.size());
        assertEquals("First message", result.get(0).getContent());
        assertEquals("Second message", result.get(1).getContent());
        assertEquals("Third message", result.get(2).getContent());
    }

    @Test
    void findUnreadMessagesBySessionAndSenderType_withAudioMessages() {
        // Arrange - Erstelle eine ungelesene Nachricht mit Audio-URL
        ChatMessage audioMessage = ChatMessage.builder()
                .sessionId(sessionId1)
                .content("Audio message")
                .senderType(SenderType.USER)
                .timestamp(LocalDateTime.now())
                .read(false)
                .audioUrl("https://example.com/audio.mp3")
                .build();
        chatMessageRepository.save(audioMessage);

        // Act
        List<ChatMessage> result = chatMessageRepository
                .findUnreadMessagesBySessionAndSenderType(sessionId1, SenderType.USER);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Ursprüngliche + neue Audio-Nachricht

        // Überprüfe, dass die Audio-Nachricht enthalten ist
        boolean audioMessageFound = result.stream()
                .anyMatch(msg -> msg.getAudioUrl() != null && msg.getAudioUrl().equals("https://example.com/audio.mp3"));
        assertTrue(audioMessageFound);
    }

    @Test
    void countUnreadMessagesBySessionAndSenderType_differentMessageTypes() {
        // Arrange - Erstelle verschiedene Nachrichtentypen
        ChatMessage textMessage = ChatMessage.builder()
                .sessionId(sessionId1)
                .content("Text message")
                .senderType(SenderType.USER)
                .type(MessageType.TEXT)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();

        ChatMessage imageMessage = ChatMessage.builder()
                .sessionId(sessionId1)
                .content("Image message")
                .senderType(SenderType.USER)
                .type(MessageType.AUDIO)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();

        chatMessageRepository.saveAll(List.of(textMessage, imageMessage));

        // Act
        long result = chatMessageRepository
                .countUnreadMessagesBySessionAndSenderType(sessionId1, SenderType.USER);

        // Assert - Sollte alle Nachrichtentypen zählen
        assertEquals(3L, result); // Ursprüngliche + 2 neue
    }
}