package de.aha.backend.service.chat;

import de.aha.backend.model.chat.ChatMessage;
import de.aha.backend.model.chat.SenderType;
import de.aha.backend.repository.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }
        ChatMessage savedMessage = chatMessageRepository.save(message);
        log.info("Message saved: {} from {}", savedMessage.getId(), savedMessage.getSenderType());
        return savedMessage;
    }

    public List<ChatMessage> getChatHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }

    public void markMessagesAsRead(String sessionId, SenderType senderType) {
        List<ChatMessage> unreadMessages = chatMessageRepository
                .findUnreadMessagesBySessionAndSenderType(sessionId, senderType);

        if (!unreadMessages.isEmpty()) {
            unreadMessages.forEach(msg -> msg.setRead(true));
            chatMessageRepository.saveAll(unreadMessages);
            log.info("Marked {} messages as read in session {}", unreadMessages.size(), sessionId);
        }
    }

    public long getUnreadCountForUser(String sessionId) {
        return chatMessageRepository.countUnreadMessagesBySessionAndSenderType(
                sessionId, SenderType.ADVISOR);
    }

    public long getUnreadCountForAdvisor(String sessionId) {
        return chatMessageRepository.countUnreadMessagesBySessionAndSenderType(
                sessionId, SenderType.USER);
    }

    public List<ChatMessage> getUnreadAdvisorMessages(String sessionId) {
        return chatMessageRepository.findUnreadAdvisorMessages(sessionId);
    }

    public List<ChatMessage> getUnreadUserMessages(String sessionId) {
        return chatMessageRepository.findUnreadUserMessages(sessionId);
    }
}