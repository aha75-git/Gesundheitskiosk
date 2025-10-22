package de.aha.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Model for error response returned by REST API.
 * Contains timestamp, status, message, and error code.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String code;
}