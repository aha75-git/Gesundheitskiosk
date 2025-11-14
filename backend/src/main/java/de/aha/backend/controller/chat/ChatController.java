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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@AuthRequired(AuthInterceptor.class)
@Tag(name = "chat", description = "chat endpoints")
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatSessionService chatSessionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuthInterceptor authInterceptor;
    private final UserService userService;
    private final AdvisorService advisorService;

    // Nachricht an Advisor senden
    @PostMapping("/{advisorId}/message")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> sendMessage(
            @PathVariable String advisorId,
            @Valid @RequestBody SendMessageRequest request) {

        var userId = authInterceptor.getUserId();
        User user = userService.findById(userId);
        var advisor = advisorService.getAdvisorById(advisorId).
                orElseThrow(() -> new NotFoundObjectException("Advisor not found with advisor id: " + advisorId));

        try {
            // Session finden oder erstellen
            ChatSession session = chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)
                    .orElseGet(() -> {
                        // In einer echten Implementierung würden wir hier Advisor-Daten laden
                        ChatSession newSession = ChatSession.builder()
                                .userId(userId)
                                .advisorId(advisorId)
                                .advisorName(advisor.getName()) // "Advisor Name" Würde aus Advisor-Service geladen
                                .lastMessage(request.getContent())
                                .lastMessageTime(LocalDateTime.now())
                                .unreadCount(0)
                                .build();
                        return chatSessionService.createOrUpdateSession(newSession);
                    });

            // Nachricht erstellen
            ChatMessage message = ChatMessage.builder()
                    .sessionId(session.getId())
                    .content(request.getContent())
                    .senderId(userId)
                    .senderName(user.getUsername()) // "Current user" Würde aus User-Service geladen
                    .senderType(SenderType.USER)
                    .timestamp(LocalDateTime.now())
                    .type(MessageType.TEXT)
                    .read(false)
                    .audioUrl(request.getAudioUrl())
                    .build();

            ChatMessage savedMessage = chatMessageService.saveMessage(message);

            // Session aktualisieren
            session.setLastMessage(request.getContent());
            session.setLastMessageTime(savedMessage.getTimestamp());
            session.setUnreadCount((int) chatMessageService.getUnreadCountForAdvisor(session.getId()));
            chatSessionService.createOrUpdateSession(session);

            // Nachricht über WebSocket an Advisor senden
            ChatMessageDTO messageDTO = convertToDTO(savedMessage);
            messagingTemplate.convertAndSendToUser(
                    advisorId,
                    "/queue/messages",
                    messageDTO
            );

            log.info("Message sent from user {} to advisor {}", userId, advisorId);

            return ResponseEntity.ok(ApiResponse.success(messageDTO));

        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to send message: " + e.getMessage()));
        }
    }

    // Chat-Historie abrufen
    @GetMapping("/{advisorId}/history")
    public ResponseEntity<ApiResponse<List<ChatMessageDTO>>> getChatHistory(
            @PathVariable String advisorId,
            @AuthenticationPrincipal String userId) {

        try {
            ChatSession session = chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)
                    .orElseThrow(() -> new RuntimeException("Chat session not found"));

            List<ChatMessage> messages = chatMessageService.getChatHistory(session.getId());

            // Nachrichten des Advisors als gelesen markieren
            chatMessageService.markMessagesAsRead(session.getId(), SenderType.ADVISOR);

            // Unread Count aktualisieren
            session.setUnreadCount(0);
            chatSessionService.createOrUpdateSession(session);

            List<ChatMessageDTO> messageDTOs = messages.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(messageDTOs));

        } catch (Exception e) {
            log.error("Error fetching chat history: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch chat history: " + e.getMessage()));
        }
    }

    // Chat-Sessions für Benutzer abrufen
    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<ChatSessionDTO>>> getUserChatSessions(
            @AuthenticationPrincipal String userId) {

        try {
            List<ChatSession> sessions = chatSessionService.getUserSessions(userId);
            List<ChatSessionDTO> sessionDTOs = sessions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(sessionDTOs));

        } catch (Exception e) {
            log.error("Error fetching user sessions: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch sessions: " + e.getMessage()));
        }
    }

    // Nachrichten als gelesen markieren
    @PostMapping("/{advisorId}/read")
    public ResponseEntity<ApiResponse<Void>> markMessagesAsRead(
            @PathVariable String advisorId,
            @AuthenticationPrincipal String userId) {

        try {
            ChatSession session = chatSessionService.getSessionByUserAndAdvisor(userId, advisorId)
                    .orElseThrow(() -> new RuntimeException("Chat session not found"));

            chatMessageService.markMessagesAsRead(session.getId(), SenderType.ADVISOR);

            // Unread Count zurücksetzen
            session.setUnreadCount(0);
            chatSessionService.createOrUpdateSession(session);

            log.info("Messages marked as read for session: {}", session.getId());

            return ResponseEntity.ok(ApiResponse.success(null));

        } catch (Exception e) {
            log.error("Error marking messages as read: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to mark messages as read: " + e.getMessage()));
        }
    }

    // WebSocket Message Handling
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        try {
            // Nachricht speichern
            ChatMessage message = convertToEntity(chatMessageDTO);
            ChatMessage savedMessage = chatMessageService.saveMessage(message);

            // An Empfänger senden
            String destination = "/queue/messages";
            messagingTemplate.convertAndSendToUser(
                    chatMessageDTO.getSenderId().equals("user") ?
                            chatMessageDTO.getSessionId() : chatMessageDTO.getSenderId(),
                    destination,
                    convertToDTO(savedMessage)
            );

        } catch (Exception e) {
            log.error("Error in WebSocket message handling: {}", e.getMessage());
        }
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessageDTO chatMessageDTO) {
        // Benutzer zu Chat hinzufügen
        log.info("User added to chat: {}", chatMessageDTO.getSenderId());
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

    private ChatMessage convertToEntity(ChatMessageDTO dto) {
        return ChatMessage.builder()
                .id(dto.getId())
                .sessionId(dto.getSessionId())
                .content(dto.getContent())
                .senderId(dto.getSenderId())
                .senderName(dto.getSenderName())
                .senderType(SenderType.valueOf(dto.getSenderType()))
                .timestamp(dto.getTimestamp())
                .type(MessageType.valueOf(dto.getType()))
                .read(dto.isRead())
                .audioUrl(dto.getAudioUrl())
                .build();
    }
}