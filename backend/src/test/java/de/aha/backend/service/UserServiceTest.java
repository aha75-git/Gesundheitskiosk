package de.aha.backend.service;

import de.aha.backend.dto.user.*;
import de.aha.backend.model.user.Address;
import de.aha.backend.model.user.ContactInfo;
import de.aha.backend.model.user.User;
import de.aha.backend.model.user.UserProfile;
import de.aha.backend.repository.UserRepository;
import de.aha.backend.security.TokenInteract;
import de.aha.backend.security.UserDetailsImpl;
import de.aha.backend.util.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private TokenInteract tokenInteract;

    @Mock
    private UserRepository repository;

    //@Mock
    //private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserLoginRequest loginRequest;
    private UserLoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setUsername("test@example.com");
        user.setPassword("hashedPassword");
        user.setCreationDate(LocalDateTime.now());

        loginRequest = new UserLoginRequest("test@example.com", "password");

        UserResponse userResponse = UserResponse.builder().email("test@example.com").build(); //new UserResponse("test@example.com");

        loginResponse = new UserLoginResponse("token", userResponse);
    }

    @Test
    void getToken_withId_shouldReturnToken() {
        when(repository.getOrThrow("1")).thenReturn(user);
        when(repository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.matches("password", "hashedPassword")).thenReturn(true);
            when(tokenInteract.generateToken(any(UserDetailsImpl.class))).thenReturn("token");

            UserLoginResponse result = userService.getToken(loginRequest);

            assertEquals(loginResponse.token(), result.token());
            verify(repository).getOrThrow("1");
            verify(repository, atLeastOnce()).findByEmailIgnoreCase("test@example.com");
            passwordUtil.verify(() -> PasswordUtil.matches("password", "hashedPassword"));
            verify(tokenInteract).generateToken(any(UserDetailsImpl.class));
        }
    }

    @Test
    void getToken_withoutId_shouldFindByEmailAndReturnToken() {
        when(repository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        when(repository.getOrThrow("1")).thenReturn(user);
        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.matches("password", "hashedPassword")).thenReturn(true);
            when(tokenInteract.generateToken(any(UserDetailsImpl.class))).thenReturn("token");

            UserLoginResponse result = userService.getToken(loginRequest);

            assertEquals(loginResponse.token(), result.token());
            verify(repository).findByEmailIgnoreCase("test@example.com");
            verify(repository).getOrThrow("1");
            passwordUtil.verify(() -> PasswordUtil.matches("password", "hashedPassword"));
            verify(tokenInteract).generateToken(any(UserDetailsImpl.class));
        }
    }

    @Test
    void validateToken_shouldReturnValidationResult() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(tokenInteract.getToken(request)).thenReturn("token");
        when(tokenInteract.validateToken("token")).thenReturn(true);

        Boolean result = userService.validateToken(request);

        assertTrue(result);
        verify(tokenInteract).getToken(request);
        verify(tokenInteract).validateToken("token");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        when(repository.getOrThrow("1")).thenReturn(user);

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .email(user.getId())
                .password(user.getPassword())
                .build();

        UserDetails loaded = userService.loadUserByUsername("1");

        assertEquals("1", loaded.getUsername());
        assertEquals("hashedPassword", loaded.getPassword());
        verify(repository).getOrThrow("1");
    }

    @Test
    void loadUserByUsername_userNotFound_shouldThrowUsernameNotFoundException() {
        when(repository.getOrThrow("unknown@example.com")).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("unknown@example.com"));
        verify(repository).getOrThrow("unknown@example.com");
    }

    @Test
    void findByEmail_shouldReturnUser() {
        when(repository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("test@example.com");

        assertEquals(user, result);
        verify(repository).findByEmailIgnoreCase("test@example.com");
    }

    @Test
    void findByEmail_userNotFound_shouldThrowBadCredentialsException() {
        when(repository.findByEmailIgnoreCase("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> userService.findByEmail("unknown@example.com"));
        verify(repository).findByEmailIgnoreCase("unknown@example.com");
    }

    @Test
    void getToken_invalidPassword_shouldThrowBadCredentialsException() {
        when(repository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.matches("wrongpassword", "hashedPassword")).thenReturn(false);

            UserLoginRequest invalidRequest = new UserLoginRequest("test@example.com", "wrongpassword");

            assertThrows(BadCredentialsException.class, () -> userService.getToken(invalidRequest));
            verify(repository).findByEmailIgnoreCase("test@example.com");
            passwordUtil.verify(() -> PasswordUtil.matches("wrongpassword", "hashedPassword"));
        }
    }

    @Test
    void validateToken_invalidToken_shouldReturnFalse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(tokenInteract.getToken(request)).thenReturn("invalidtoken");
        when(tokenInteract.validateToken("invalidtoken")).thenReturn(false);

        Boolean result = userService.validateToken(request);

        assertFalse(result);
        verify(tokenInteract).getToken(request);
        verify(tokenInteract).validateToken("invalidtoken");
    }

    @Test
    public void testFindProfile() {
        UserProfile expectedProfile = UserProfile.builder()
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
        user.setProfile(expectedProfile);

        when(repository.getOrThrow("1")).thenReturn(user);

        UserProfileResponse response = userService.findProfile("1");
        assertEquals("123456789", response.userProfile().getContactInfo().getPhone());
    }

    @Test
    public void testSaveProfile() {
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

        UserProfile expectedProfile = UserProfile.builder()
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
        user.setProfile(expectedProfile);

        when(repository.getOrThrow("1")).thenReturn(user);
        when(repository.save(any())).thenReturn(user);

        UserProfileResponse response = userService.saveProfile("1", request);
        assertEquals("123456789", response.userProfile().getContactInfo().getPhone());
    }
}