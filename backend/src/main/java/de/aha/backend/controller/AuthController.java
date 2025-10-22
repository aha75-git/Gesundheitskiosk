package de.aha.backend.controller;

import de.aha.backend.dto.user.UserResponse;
import de.aha.backend.model.UserRole;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal OAuth2User user) {
        // In GitHub versteckt sich unter dem "login"-Punkt User-Name.
        //return user.getAttribute("login").toString();

        if (user == null) {
            throw new RuntimeException("Invalid user");
        }
        return UserResponse.builder()
                .email(user.getAttribute("email"))
                .username(user.getAttribute("login").toString())
                .role(UserRole.USER)
                .build();
    }
}
