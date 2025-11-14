package de.aha.backend.dto.chat;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class SendMessageRequest {
    @NotBlank
    private String content;

    private String messageType;
    private String audioUrl;
}