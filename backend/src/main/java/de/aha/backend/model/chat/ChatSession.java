package de.aha.backend.model.chat;

import de.aha.backend.model.AbstractDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_sessions")
@CompoundIndex(name = "user_advisor_idx", def = "{'userId': 1, 'advisorId': 1}", unique = true)
public class ChatSession extends AbstractDocument {

    @Indexed
    private String userId;

    @Indexed
    private String advisorId;
    private String advisorName;
    private String advisorImage;

    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer unreadCount;

    @Builder.Default
    private Boolean isActive = true;
}