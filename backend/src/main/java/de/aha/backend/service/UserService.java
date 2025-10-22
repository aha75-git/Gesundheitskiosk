package de.aha.backend.service;

import de.aha.backend.dto.user.*;
import de.aha.backend.exception.AppRuntimeException;
import de.aha.backend.exception.ExecutionConflictException;
import de.aha.backend.model.User;
import de.aha.backend.model.UserRole;
import de.aha.backend.repository.UserRepository;
import de.aha.backend.security.TokenInteract;
import de.aha.backend.util.PasswordUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final TokenInteract tokenInteract;
    private final UserRepository userRepository;

    /** Finds a user by ID and returns a UserResponse DTO.
     *
     * @param id ID of the user to find
     * @return UserResponse containing user details
     */
    public UserResponse find(String id) {
        User user = userRepository.getOrThrow(id);
        //return mapper.toResponse(repository.getOrThrow(id));

        return mapToResponse(user);
    }

    /*
    public List<AdvisorResponse> findAdvisors(String specialization, String language) {
        List<User> advisors;

        if (language != null && specialization != null) {
            advisors = userRepository.findByRoleAndLanguagesContainingAndSpecialization(
                    User.UserRole.ADVISOR, language, specialization);
        } else if (language != null) {
            advisors = userRepository.findByRoleAndLanguagesContaining(User.UserRole.ADVISOR, language);
        } else if (specialization != null) {
            advisors = userRepository.findByRoleAndSpecialization(User.UserRole.ADVISOR, specialization);
        } else {
            advisors = userRepository.findByRole(User.UserRole.ADVISOR);
        }

        return advisors.stream()
                .map(this::mapToAdvisorResponse)
                .toList();
    }

     */


    /** Creates a new user after checking for unique email and hashing the password.
     *
     * @param request request containing user creation details
     */
    public void create(UserCreateRequest request) {
        checkUniqueEmail(request.email());
        userRepository.save(this.mapToUser(request, PasswordUtil.hash(request.password())));
    }

    public UserResponse registerUser(@Valid RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        checkUniqueEmail(request.getEmail());
        User user = userRepository.save(mapToUser(request));
        //cacheUser(user);
        log.info("User registered successfully: {}", user.getId());
        return mapToResponse(user);
    }

    public UserLoginResponse authenticateUser(@Valid UserLoginRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!PasswordUtil.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("User account is not active");
        }

        //UserResponse response = mapper.toResponse(user);
        //return mapper.toLoginResponse(response, tokenInteract.generateToken(loadUserByUsername(user.getId())));


        var token = tokenInteract.generateToken(loadUserByUsername(user.getId()));
        var userResponse = mapToResponse(user);

        // Cache user session
        //cacheUserSession(token, user);

        return new UserLoginResponse(token, userResponse);
    }

    /**
     * Loads user details by user ID for authentication purposes.
     *
     * @param userId user's ID
     * @return UserDetails implementation containing user info
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.getOrThrow(userId);
    }

    /**
     * Checks if the email is unique in the database.
     * Throws ExecutionConflictException if a user with the email already exists.
     *
     * @param email user's email address
     */
    private void checkUniqueEmail(String email) {
        userRepository.findByEmailIgnoreCase(email)
                .ifPresent(u -> {
                    throw new ExecutionConflictException(
                            "User with email '" + email + "' already exists");
                });
    }

    private User mapToUser(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setProvider("local");
        user.setProviderId(request.getPassword());
        user.setUsername(request.getUsername());
        return user;
    }

    private User mapToUser(UserCreateRequest request, String passwordHash) {
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordHash);
        user.setRole(UserRole.USER);
        user.setProvider("local");
        user.setProviderId(passwordHash);
        return user;
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(user.getProviderId())
                .username(user.getUsername())
                .build();
    }

    /*
    public List<UserResponse> findAll() {
        User user = userRepository.findAll();
        //return mapper.toResponse(repository.getOrThrow(id));

        return mapToResponse(user);
    }

     */

    /*
    private AdvisorResponse mapToAdvisorResponse(User advisor) {
        return new AdvisorResponse(
                advisor.id(),
                advisor.profile().personalData().firstName() + " " + advisor.profile().personalData().lastName(),
                advisor.profile().specialization(),
                List.copyOf(advisor.profile().languages()),
                advisor.profile().rating() != null ? advisor.profile().rating() : 0.0,
                true,
                advisor.profile().bio(),
                advisor.profile().qualification()
        );
    }

     */
}