package de.aha.backend.mapper;

import de.aha.backend.dto.user.RegisterRequest;
import de.aha.backend.dto.user.UserCreateRequest;
import de.aha.backend.dto.user.UserLoginResponse;
import de.aha.backend.dto.user.UserResponse;
import de.aha.backend.model.User;
import de.aha.backend.model.UserRole;

public class UserMapper {
    public static User mapToUser(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setProvider("local");
        user.setProviderId(request.getPassword());
        user.setUsername(request.getUsername());
        return user;
    }

    public static User mapToUser(UserCreateRequest request, String passwordHash) {
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordHash);
        user.setRole(UserRole.USER);
        user.setProvider("local");
        user.setProviderId(passwordHash);
        return user;
    }

    public static UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(user.getProviderId())
                .username(user.getUsername())
                .build();
    }

    public static UserLoginResponse mapToLoginResponse(UserResponse user, String token) {
        return new UserLoginResponse(token, user);
    }
}
