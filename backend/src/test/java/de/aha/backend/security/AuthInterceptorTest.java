package de.aha.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {

    @Mock
    private TokenInteract tokenInteract;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthInterceptor authInterceptor;

    private Object handler = new Object();

    @BeforeEach
    void setUp() {
        authInterceptor.postHandle(request, response, handler, null);
    }

    @Test
    void preHandle_WithVersionEndpoint_ShouldReturnTrue() throws Exception {
        when(request.getRequestURI()).thenReturn("/version");

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verifyNoInteractions(tokenInteract);
    }

    @Test
    void preHandle_WithAuthenticateEndpoint_ShouldReturnTrue() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/authenticate");

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verifyNoInteractions(tokenInteract);
    }

    @Test
    void preHandle_WithUserCreateEndpoint_ShouldReturnTrue() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/users/create");

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verifyNoInteractions(tokenInteract);
    }

    @Test
    void preHandle_WithAuthenticateURI_ShouldReturnTrue() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/authenticate");

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verifyNoInteractions(tokenInteract);
    }

    @Test
    void preHandle_WithUserCreateURI_ShouldReturnTrue() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/users/create");

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verifyNoInteractions(tokenInteract);
    }

    @Test
    void preHandle_WithUserLogin_ShouldReturnTrue() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/users/login");

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verifyNoInteractions(tokenInteract);
    }

    @Test
    void preHandle_WithUserRegister_ShouldReturnTrue() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/v1/users/register");

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verifyNoInteractions(tokenInteract);
    }


    @Test
    void preHandle_WithValidToken_ShouldReturnTrue() throws Exception {
        String token = "validToken";
        String userId = "user123";

        when(request.getRequestURI()).thenReturn("/api/v1/appointments");
        when(tokenInteract.getToken(request)).thenReturn(token);
        when(tokenInteract.validateToken(token)).thenReturn(true);
        when(tokenInteract.getUserId(token)).thenReturn(userId);

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        assertEquals(userId, authInterceptor.getUserId());
        verify(tokenInteract).getToken(request);
        verify(tokenInteract).validateToken(token);
        verify(tokenInteract).getUserId(token);
    }

    @Test
    void preHandle_WithInvalidToken_ShouldReturnFalse() throws Exception {
        String token = "invalidToken";

        when(request.getRequestURI()).thenReturn("/api/v1/appointments");
        when(tokenInteract.getToken(request)).thenReturn(token);
        when(tokenInteract.validateToken(token)).thenReturn(false);

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertFalse(result);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        verify(tokenInteract).getToken(request);
        verify(tokenInteract).validateToken(token);
        verify(tokenInteract, never()).getUserId(token);
    }

    @Test
    void preHandle_WithTokenButEmptyUserId_ShouldReturnFalse() throws Exception {
        String token = "validToken";

        when(request.getRequestURI()).thenReturn("/api/v1/appointments");
        when(tokenInteract.getToken(request)).thenReturn(token);
        when(tokenInteract.validateToken(token)).thenReturn(true);
        when(tokenInteract.getUserId(token)).thenReturn("");

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertFalse(result);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Test
    void preHandle_WithTokenButNullUserId_ShouldReturnFalse() throws Exception {
        String token = "validToken";

        when(request.getRequestURI()).thenReturn("/api/v1/appointments");
        when(tokenInteract.getToken(request)).thenReturn(token);
        when(tokenInteract.validateToken(token)).thenReturn(true);
        when(tokenInteract.getUserId(token)).thenReturn(null);

        boolean result = authInterceptor.preHandle(request, response, handler);

        assertFalse(result);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Test
    void preHandle_WithTokenException_ShouldThrowException() {
        when(request.getRequestURI()).thenReturn("/api/v1/appointments");
        when(tokenInteract.getToken(request)).thenThrow(new RuntimeException("Token validation failed"));

        assertThrows(RuntimeException.class, () -> authInterceptor.preHandle(request, response, handler));
    }

    @Test
    void getUserId_WithoutSettingUser_ShouldReturnNull() {
        String userId = authInterceptor.getUserId();

        assertNull(userId);
    }

    @Test
    void postHandle_ShouldClearThreadLocal() throws Exception {
        String token = "validToken";
        String userId = "user123";

        when(request.getRequestURI()).thenReturn("/api/v1/appointments");
        when(tokenInteract.getToken(request)).thenReturn(token);
        when(tokenInteract.validateToken(token)).thenReturn(true);
        when(tokenInteract.getUserId(token)).thenReturn(userId);

        authInterceptor.preHandle(request, response, handler);
        assertEquals(userId, authInterceptor.getUserId());

        authInterceptor.postHandle(request, response, handler, null);

        assertNull(authInterceptor.getUserId());
    }
}