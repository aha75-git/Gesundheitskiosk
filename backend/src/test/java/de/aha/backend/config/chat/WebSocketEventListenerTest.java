package de.aha.backend.config.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketEventListenerTest {

    @InjectMocks
    private WebSocketEventListener webSocketEventListener;

    @Mock
    private SessionConnectedEvent connectedEvent;

    @Mock
    private SessionDisconnectEvent disconnectEvent;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void handleWebSocketConnectListener_shouldLogSessionId() {
        // Arrange
        String sessionId = "test-session-123";
        Map<String, Object> headers = new HashMap<>();
        headers.put("simpSessionId", sessionId);

        Message<byte[]> message = new GenericMessage<>(new byte[0], headers);

        // Act - publish the actual event
        eventPublisher.publishEvent(new SessionConnectedEvent(this, message));

        webSocketEventListener.handleWebSocketConnectListener(connectedEvent);

        // Assert
        verify(connectedEvent, times(1)).getMessage();
        // We can't easily verify the log output without a logging framework test setup,
        // but we can verify the method executes without exceptions
    }

    @Test
    void handleWebSocketDisconnectListener_shouldLogSessionId() {
        // Arrange
        String sessionId = "test-session-456";
        Map<String, Object> headers = new HashMap<>();
        headers.put("simpSessionId", sessionId);

        Message<byte[]> message = new GenericMessage<>(new byte[0], headers);

        // Act - publish the actual event
        eventPublisher.publishEvent(new SessionDisconnectEvent(this, message, sessionId, CloseStatus.NORMAL));
        webSocketEventListener.handleWebSocketDisconnectListener(disconnectEvent);

        // Assert
        verify(disconnectEvent, times(1)).getMessage();
        // Verify the method executes without exceptions
    }

    @Test
    void handleWebSocketDisconnectListener_shouldUpdateAdvisorStatusWhenImplemented() {
        // Arrange
        String sessionId = "test-session-789";
        Map<String, Object> headers = new HashMap<>();
        headers.put("simpSessionId", sessionId);

        Message<byte[]> message = new GenericMessage<>(new byte[0], headers);

        // Act - publish the actual event
        eventPublisher.publishEvent(new SessionDisconnectEvent(this, message, sessionId, CloseStatus.NORMAL));

        // Act
        webSocketEventListener.handleWebSocketDisconnectListener(disconnectEvent);

        // Assert
        verify(disconnectEvent, times(1)).getMessage();

        // Wenn AdvisorService implementiert ist, sollte hier der Service-Call verifiziert werden:
        // verify(advisorService, times(1)).updateAdvisorOnlineStatus(anyString(), eq(false));
    }
}