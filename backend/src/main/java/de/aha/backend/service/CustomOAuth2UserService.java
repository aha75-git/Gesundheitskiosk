package de.aha.backend.service;

import de.aha.backend.model.User;
import de.aha.backend.model.UserRole;
import de.aha.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        //User user = userRepository.findByProviderAndProviderId(provider, providerId)
        User user = userRepository.findById(oAuth2User.getName())
                .orElseGet(() -> createUser(oAuth2User, provider));

//        return new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(user.getRole().name())),
//                oAuth2User.getAttributes(), "id"); // Benutzer anhand seiner Role zurückgeben
        return oAuth2User;
    }

    private User createUser(OAuth2User oAuth2User, String provider) {
        String providerId = oAuth2User.getAttribute("id") != null ?
                oAuth2User.getAttribute("id").toString() :
                oAuth2User.getAttribute("sub").toString();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String username = oAuth2User.getAttribute("login") != null ?
                oAuth2User.getAttribute("login").toString() :
                this.generateUsername(provider, providerId);

        User user = new User();
        user.setId(oAuth2User.getName()); // UserId von GitHub
        user.setEmail(email);
        //user.setPassword(passwordHash);
        user.setRole(UserRole.USER);
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setUsername(username);

        userRepository.save(user);
        return user;
    }

    private String generateUsername(String provider, String providerId) {
        return provider + "_" + providerId;
    }
}
