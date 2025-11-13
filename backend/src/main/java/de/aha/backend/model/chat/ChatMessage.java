package de.aha.backend.model.chat;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;

    @Indexed
    private String sessionId;

    private String content;
    private String senderId;
    private String senderName;
    private SenderType senderType;
    private LocalDateTime timestamp;
    private MessageType type;
    private boolean read;
    private String audioUrl;
}