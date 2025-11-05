package de.aha.backend.controller;

import de.aha.backend.dto.user.*;
import de.aha.backend.exception.AppAuthenticationException;
import de.aha.backend.exception.ExecutionConflictException;
import de.aha.backend.model.user.*;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.security.TokenInteract;
import de.aha.backend.security.UserDetailsImpl;
import de.aha.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    @Mock
    private AuthInterceptor authInterceptor;
    @Mock
    private TokenInteract tokenInteract;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void find_success() {
        String userId = "user123";
        UserResponse response = UserResponse.builder()
                .email("test@email.com")
                .build();

        when(authInterceptor.getUserId()).thenReturn(userId);
        when(userService.find(userId)).thenReturn(response);
        ResponseEntity<UserResponse> result = userController.find();
        assertEquals(org.springframework.http.HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }


    @Test
    void create_success() {
        UserCreateRequest req = new UserCreateRequest("test@email.com", "password123");
        doNothing().when(userService).create(req);
        ResponseEntity<Void> result = userController.create(req);
        assertEquals(org.springframework.http.HttpStatus.CREATED, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void updatePassword_success() {
        String userId = "user123";
        UserUpdatePasswordRequest req = new UserUpdatePasswordRequest("newPassword123", "oldPassword123");
        UserResponse resp = UserResponse.builder().email("test@email.com").build();
        when(authInterceptor.getUserId()).thenReturn(userId);
        when(userService.updatePassword(userId, req)).thenReturn(resp);
        ResponseEntity<UserResponse> result = userController.updatePassword(req);
        assertEquals(org.springframework.http.HttpStatus.OK, result.getStatusCode());
        assertEquals(resp, result.getBody());
    }

    @Test
    void delete_success() {
        String userId = "user123";
        when(authInterceptor.getUserId()).thenReturn(userId);
        doNothing().when(userService).remove(userId);
        ResponseEntity<Void> result = userController.delete();
        assertEquals(org.springframework.http.HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void login_success() {
        // GIVEN
        User user =  new User();
        user.setId("user123");
        user.setEmail("test@email.com");
        user.setPassword("password123");
        String token = "secret_token_123"; //tokenInteract.generateToken(loadUserByUsername(user.getId()));
        UserLoginRequest request = new UserLoginRequest("test@email.com", "password123");
        UserResponse userResponse = UserResponse.builder().email("test@email.com").token(token).build();
        UserLoginResponse loginResponse = new UserLoginResponse(token, userResponse);
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .email(user.getId())
                .password(user.getPassword())
                .build();

        // WHEN
        when(tokenInteract.generateToken(userDetails)).thenReturn(token);
        when(userService.authenticateUser(request)).thenReturn(loginResponse);
        var result = userController.login(request);

        // THEN
        assertEquals(org.springframework.http.HttpStatus.OK, result.getStatusCode());
        assertEquals(loginResponse, result.getBody());
    }

    @Test
    void login_failure() {
        // GIVEN
        UserLoginRequest request = new UserLoginRequest("test@email.com", "wrong_password");

        // WHEN
        when(userService.authenticateUser(request)).thenThrow(new AppAuthenticationException("Invalid credentials"));

        // THEN
        assertThrows(AppAuthenticationException.class, () -> userController.login(request));
    }

    @Test
    void registerUser_success() {
        // GIVEN
        RegisterRequest request = new RegisterRequest("testmax","test@email.com", "password123", UserRole.USER);
        UserResponse userResponse = UserResponse.builder().email("test@email.com").build();

        // WHEN
        when(userService.registerUser(request)).thenReturn(userResponse);
        var result = userController.registerUser(request);

        // THEN
        assertEquals(org.springframework.http.HttpStatus.OK, result.getStatusCode());
        assertEquals(userResponse, result.getBody());
    }

    @Test
    void registerUser_failure() {
        // GIVEN
        RegisterRequest request = new RegisterRequest("testmax","test@email.com", "password123", UserRole.USER);

        // WHEN
        when(userService.registerUser(request)).thenThrow(new ExecutionConflictException(""));

        // THEN
        assertThrows(ExecutionConflictException.class, () -> userController.registerUser(request));
    }

//    @Test
//    void getProfile_success() {
//        String userId = "user123";
//        UserProfile userProfile = UserProfile.builder()
//                .
//                .build();
//
//        UserProfileResponse response = UserProfileResponse.builder()
//                .email("test@email.com")
//                .build();
//
//        when(authInterceptor.getUserId()).thenReturn(userId);
//        when(userService.find(userId)).thenReturn(response);
//        ResponseEntity<UserResponse> result = userController.find();
//        assertEquals(org.springframework.http.HttpStatus.OK, result.getStatusCode());
//        assertEquals(response, result.getBody());
//    }


    @Test
    public void testGetProfile() {
        // GIVEN
        UserProfileResponse expectedResponse = UserProfileResponse.builder()
                .userProfile(UserProfile.builder()
                        .contactInfo(ContactInfo.builder()
                                .phone("123456789")
                                .allowHouseVisits(true)
                                .address(Address.builder()
                                        .city("city")
                                        .country("country")
                                        .street("street")
                                        .houseNumber("4")
                                        .postalCode("12345")
                                        .build())
                                .build())
                        .build())
                .build();

        // WHEN
        when(authInterceptor.getUserId()).thenReturn("1");
        when(userService.findProfile("1")).thenReturn(expectedResponse);

        // THEN
        ResponseEntity<UserProfileResponse> result = userController.getProfile();
        assertEquals(org.springframework.http.HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    public void testSaveProfile() {
        // GIVEN
        UserProfileRequest request = UserProfileRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .contactInfo(ContactInfo.builder()
                        .phone("123456789")
                        .allowHouseVisits(true)
                        .address(Address.builder()
                                .city("city")
                                .country("country")
                                .street("street")
                                .houseNumber("4")
                                .postalCode("12345")
                                .build())
                        .build())
                .build();

        UserProfileResponse expectedResponse = UserProfileResponse.builder()
                .userProfile(UserProfile.builder()
                        .contactInfo(ContactInfo.builder()
                                .phone("123456789")
                                .allowHouseVisits(true)
                                .address(Address.builder()
                                        .city("city")
                                        .country("country")
                                        .street("street")
                                        .houseNumber("4")
                                        .postalCode("12345")
                                        .build())
                                .build())
                        .build())
                .build();

        // WHEN
        when(authInterceptor.getUserId()).thenReturn("1");
        when(userService.saveProfile("1", request)).thenReturn(expectedResponse);

        // THEN
        ResponseEntity<UserProfileResponse> result = userController.saveProfile(request);
        assertEquals(org.springframework.http.HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }
}