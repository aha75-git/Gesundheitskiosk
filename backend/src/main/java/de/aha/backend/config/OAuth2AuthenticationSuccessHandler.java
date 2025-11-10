package de.aha.backend.config;

import de.aha.backend.exception.NotFoundObjectException;
import de.aha.backend.model.user.User;
import de.aha.backend.repository.UserRepository;
import de.aha.backend.security.TokenInteract;
import de.aha.backend.security.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final TokenInteract tokenInteract;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String provider = ((OAuth2User) authentication.getPrincipal()).getAttribute("provider");
//        String providerId = oAuth2User.getAttribute("id") != null ?
//                oAuth2User.getAttribute("id").toString() :
//                oAuth2User.getAttribute("sub").toString();

//        var user = userRepository.findByProviderAndProviderId(provider, providerId)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = userRepository.findById(oAuth2User.getName())
                .orElseThrow(() -> new NotFoundObjectException("User not found"));

        var token = tokenInteract.generateToken(UserDetailsImpl.builder()
                .email(user.getId())
                .password(user.getPassword())
                .build());
        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth2/redirect")
                .queryParam("token", token)
                .queryParam("username", user.getUsername())
                .queryParam("role", user.getRole())
                .queryParam("id", user.getId())
                .queryParam("createdAt", user.getModifyDate())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
