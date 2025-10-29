package de.aha.backend.config;

import de.aha.backend.security.TokenInteract;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {
    @Mock
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    void canInstantiate() {
        assertDoesNotThrow(() -> new SecurityConfig(oAuth2AuthenticationSuccessHandler));
    }

    @Test
    void passwordEncoder_returnsBCryptPasswordEncoder() {
        SecurityConfig config = new SecurityConfig(oAuth2AuthenticationSuccessHandler);
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        String raw = "pass";
        String encoded = encoder.encode(raw);
        assertTrue(encoder.matches(raw, encoded));
    }

    @Test
    void logoutSuccessHandler_setsStatusOk() throws Exception {
        SecurityConfig config = new SecurityConfig(oAuth2AuthenticationSuccessHandler);
        LogoutSuccessHandler handler = config.logoutSuccessHandler();
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        handler.onLogoutSuccess(request, response, null);
        verify(response).setStatus(HttpStatus.OK.value());
    }

    @Test
    void filterChain_buildsSecurityFilterChain() throws Exception {
        SecurityConfig config = new SecurityConfig(oAuth2AuthenticationSuccessHandler);
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        assertNotNull(config.filterChain(http));
    }
}
