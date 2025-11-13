package de.aha.backend.controller.chat;

import de.aha.backend.dto.chat.ApiResponse;
import de.aha.backend.dto.chat.ChatMessageDTO;
import de.aha.backend.dto.chat.ChatSessionDTO;
import de.aha.backend.dto.chat.SendMessageRequest;
import de.aha.backend.exception.NotFoundObjectException;
import de.aha.backend.model.chat.ChatMessage;
import de.aha.backend.model.chat.ChatSession;
import de.aha.backend.model.chat.MessageType;
import de.aha.backend.model.chat.SenderType;
import de.aha.backend.model.user.User;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.security.AuthRequired;
import de.aha.backend.service.AdvisorService;
import de.aha.backend.service.UserService;
import de.aha.backend.service.chat.ChatMessageService;
import de.aha.backend.service.chat.ChatSessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/advisor/chat")
@RequiredArgsConstructor
@AuthRequired(AuthInterceptor.class)
@Tag(name = "advisor-chat", description = "advisor chat endpoints")
public class AdvisorChatController {

    private final ChatMessageService chatMessageService;
    private final ChatSessionService chatSessionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuthInterceptor authInterceptor;
    private final UserService userService;
    private final AdvisorService advisorService;

    // Nachricht vom Advisor an User senden
    @PostMapping("/{userId}/message")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> sendMessageToUser(
            @PathVariable String userId,
            @Valid @RequestBody SendMessageRequest request) {

        User user = userService.findById(userId);
        var advisorId = authInterceptor.getUserId();
        var advisor = advisorService.getAdvisorById(advisorId).
                orElseThrow(() -> new NotFoundObjectException("Advisor not found with advisor id: " + advisorId));

        try {
            // Session finden oder erstellen
            ChatSession session = chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)
                    .orElseGet(() -> {
                        // In einer echten Implementierung würden wir hier User-Daten laden
                        ChatSession newSession = ChatSession.builder()
                                .userId(userId)
                                .advisorId(advisorId)
                                .advisorName(advisor.getName()) // "Advisor Name" Würde aus Advisor-Service geladen
                                .lastMessage(request.getContent())
                                .lastMessageTime(LocalDateTime.now())
                                .unreadCount(1) // User hat eine ungelesene Nachricht
                                .build();
                        return chatSessionService.createOrUpdateSession(newSession);
                    });

            // Nachricht erstellen
            ChatMessage message = ChatMessage.builder()
                    .sessionId(session.getId())
                    .content(request.getContent())
                    .senderId(advisorId)
                    .senderName(advisor.getName()) // "Advisor Name" Würde aus Advisor-Service geladen
                    .senderType(SenderType.ADVISOR)
                    .timestamp(LocalDateTime.now())
                    .type(MessageType.TEXT)
                    .read(false)
                    .audioUrl(request.getAudioUrl())
                    .build();

            ChatMessage savedMessage = chatMessageService.saveMessage(message);

            // Session aktualisieren
            session.setLastMessage(request.getContent());
            session.setLastMessageTime(savedMessage.getTimestamp());
            session.setUnreadCount((int) chatMessageService.getUnreadCountForUser(session.getId()));
            chatSessionService.createOrUpdateSession(session);

            // Nachricht über WebSocket an User senden
            ChatMessageDTO messageDTO = convertToDTO(savedMessage);
            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/messages",
                    messageDTO
            );

            log.info("Message sent from advisor {} to user {}", advisorId, userId);

            return ResponseEntity.ok(ApiResponse.success(messageDTO));

        } catch (Exception e) {
            log.error("Error sending message from advisor: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to send message: " + e.getMessage()));
        }
    }

    // Chat-Historie für Advisor abrufen
    @GetMapping("/{userId}/history")
    public ResponseEntity<ApiResponse<List<ChatMessageDTO>>> getChatHistoryForAdvisor(
            @PathVariable String userId,
            @AuthenticationPrincipal String advisorId) {

        try {
            ChatSession session = chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)
                    .orElseThrow(() -> new RuntimeException("Chat session not found"));

            List<ChatMessage> messages = chatMessageService.getChatHistory(session.getId());

            // Nachrichten des Users als gelesen markieren
            chatMessageService.markMessagesAsRead(session.getId(), SenderType.USER);

            List<ChatMessageDTO> messageDTOs = messages.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(messageDTOs));

        } catch (Exception e) {
            log.error("Error fetching chat history for advisor: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch chat history: " + e.getMessage()));
        }
    }

    // Chat-Sessions für Advisor abrufen
    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<ChatSessionDTO>>> getAdvisorChatSessions(
            @AuthenticationPrincipal String advisorId) {

        try {
            List<ChatSession> sessions = chatSessionService.getAdvisorSessions(advisorId);
            List<ChatSessionDTO> sessionDTOs = sessions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(sessionDTOs));

        } catch (Exception e) {
            log.error("Error fetching advisor sessions: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch sessions: " + e.getMessage()));
        }
    }

    // Nachrichten als gelesen markieren (vom Advisor)
    @PostMapping("/{userId}/read")
    public ResponseEntity<ApiResponse<Void>> markMessagesAsReadByAdvisor(
            @PathVariable String userId,
            @AuthenticationPrincipal String advisorId) {

        try {
            ChatSession session = chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)
                    .orElseThrow(() -> new RuntimeException("Chat session not found"));

            chatMessageService.markMessagesAsRead(session.getId(), SenderType.USER);

            log.info("Messages marked as read by advisor for session: {}", session.getId());

            return ResponseEntity.ok(ApiResponse.success(null));

        } catch (Exception e) {
            log.error("Error marking messages as read by advisor: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to mark messages as read: " + e.getMessage()));
        }
    }

    // Helper Methods
    private ChatMessageDTO convertToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .id(message.getId())
                .sessionId(message.getSessionId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .senderName(message.getSenderName())
                .senderType(message.getSenderType().name())
                .timestamp(message.getTimestamp())
                .type(message.getType().name())
                .read(message.isRead())
                .audioUrl(message.getAudioUrl())
                .build();
    }

    private ChatSessionDTO convertToDTO(ChatSession session) {
        return ChatSessionDTO.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .advisorId(session.getAdvisorId())
                .advisorName(session.getAdvisorName())
                .advisorImage(session.getAdvisorImage())
                .lastMessage(session.getLastMessage())
                .lastMessageTime(session.getLastMessageTime())
                .unreadCount(session.getUnreadCount())
                .isActive(session.getIsActive())
                .createdAt(session.getCreationDate())
                .build();
    }
}