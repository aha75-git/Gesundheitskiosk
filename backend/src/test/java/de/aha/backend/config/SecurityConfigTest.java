package de.aha.backend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {
    @Test
    void canInstantiate() {
        assertDoesNotThrow(SecurityConfig::new);
    }

    @Test
    void passwordEncoder_returnsBCryptPasswordEncoder() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        String raw = "pass";
        String encoded = encoder.encode(raw);
        assertTrue(encoder.matches(raw, encoded));
    }

    @Test
    void logoutSuccessHandler_setsStatusOk() throws Exception {
        SecurityConfig config = new SecurityConfig();
        LogoutSuccessHandler handler = config.logoutSuccessHandler();
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        handler.onLogoutSuccess(request, response, null);
        verify(response).setStatus(HttpStatus.OK.value());
    }

    @Test
    void filterChain_buildsSecurityFilterChain() throws Exception {
        SecurityConfig config = new SecurityConfig();
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        assertNotNull(config.filterChain(http));
    }
}
