package de.aha.backend.service;

import de.aha.backend.dto.user.*;
import de.aha.backend.exception.AppAuthenticationException;
import de.aha.backend.exception.ExecutionConflictException;
import de.aha.backend.model.User;
import de.aha.backend.model.UserRole;
import de.aha.backend.repository.UserRepository;
import de.aha.backend.security.TokenInteract;
import de.aha.backend.util.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static de.aha.backend.mapper.UserMapper.*;

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
        userRepository.save(mapToUser(request, PasswordUtil.hash(request.password())));
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
                .orElseThrow(() -> new AppAuthenticationException("Invalid credentials"));

        if (!PasswordUtil.matches(request.password(), user.getPassword())) {
            throw new AppAuthenticationException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new AppAuthenticationException("User account is not active");
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
     * Updates the password of an existing user after verifying the old password.
     *
     * @param userId  ID of the user to update
     * @param request request containing old and new passwords
     * @return response containing updated user details
     */
    public UserResponse updatePassword(String userId, UserUpdatePasswordRequest request) {
        User user = userRepository.getOrThrow(userId);
        verifyPassword(request.oldPassword(), user.getPassword());
        user.setPassword(PasswordUtil.hash(request.password()));
        return mapToResponse(userRepository.save(user));
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId ID of the user to delete
     */
    public void remove(String userId) {
        User user = userRepository.getOrThrow(userId);
        userRepository.delete(user);
    }

    /**
     * Finds a user by email or throws BadCredentialsException if not found.
     *
     * @param email user's email address
     * @return User entity
     */
    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException(
                        "User not found with email: " + email
                ));
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

    /**
     * Verifies the raw password against the stored password hash.
     * Throws BadCredentialsException if the password is invalid.
     *
     * @param rawPassword  plain text password
     * @param passwordHash hashed password
     */
    private void verifyPassword(String rawPassword, String passwordHash) {
        if (!PasswordUtil.matches(rawPassword, passwordHash)) {
            throw new BadCredentialsException("Invalid password");
        }
    }

    /**
     * Authenticates a user by ID or email and returns a login response with JWT token.
     *
     * @param request user login request
     * @return login response containing JWT token and user info
     */
    public UserLoginResponse getToken(UserLoginRequest request) {
        User user = findByEmail(request.email());
        verifyPassword(request.password(), user.getPassword());
        UserResponse response = mapToResponse(user);
        return mapToLoginResponse(response, tokenInteract.generateToken(loadUserByUsername(user.getId())));
    }

    /**
     * Validates the JWT token from the HTTP request.
     *
     * @param request HTTP servlet request
     * @return true if token is valid, false otherwise
     */
    public Boolean validateToken(HttpServletRequest request) {
        String token = tokenInteract.getToken(request);
        return tokenInteract.validateToken(token);
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