package de.aha.backend.mapper;

import de.aha.backend.dto.user.*;
import de.aha.backend.model.user.User;
import de.aha.backend.model.user.UserProfile;
import de.aha.backend.model.user.UserRole;
import de.aha.backend.util.PasswordUtil;

public class UserMapper {
    public static User mapToUser(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtil.hash(request.getPassword()));
        user.setRole(request.getRole());
        user.setProvider("local");
        user.setProviderId(PasswordUtil.hash(request.getPassword()));
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
                .createdAt(user.getCreationDate().toString())
                .build();
    }

    public static UserLoginResponse mapToLoginResponse(UserResponse user, String token) {
        return new UserLoginResponse(token, user);
    }

    public static UserProfile mapToUserProfile(UserProfileRequest request) {
        return UserProfile.builder()
                .personalData(request.getPersonalData())
                .medicalInfo(request.getMedicalInfo())
                .contactInfo(request.getContactInfo())
                .languages(request.getLanguages())
                .specialization(request.getSpecialization())
                .bio(request.getBio())
                .qualification(request.getQualification())
                .rating(request.getRating())
                .reviewCount(0)
                .build();
    }

    public static UserProfileResponse mapToUserProfileResponse(UserProfile userProfile) {
        return new UserProfileResponse(userProfile);
    }
}
