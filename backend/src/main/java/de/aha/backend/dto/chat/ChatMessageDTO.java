package de.aha.backend.dto.chat;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String id;
    private String sessionId;
    private String content;
    private String senderId;
    private String senderName;
    private String senderType;
    private LocalDateTime timestamp;
    private String type;
    private boolean read;
    private String audioUrl;
}