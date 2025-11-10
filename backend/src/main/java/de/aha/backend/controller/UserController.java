package de.aha.backend.controller;

import de.aha.backend.dto.user.*;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.security.AuthRequired;
import de.aha.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@AuthRequired(AuthInterceptor.class)
@Tag(name = "user", description = "User endpoints")
public class UserController {
    private final UserService service;
    private final AuthInterceptor authInterceptor;

    /**
     * Retrieves the details of the currently authenticated user.
     * Returns a 200 OK response with user details if successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @GetMapping("/find")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Finds the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<UserResponse> find() {
        return ResponseEntity.ok(service.find(authInterceptor.getUserId()));
    }

    /**
     * Create a user.
     * Returns a 201 CREATED if a user created successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @PostMapping("/create")
    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Register a user.
     * Returns a 200 OK response with user details if successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<UserLoginResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        var response = service.registerUser(request);
        return ResponseEntity.ok(response);
    }

    /**
     * User login.
     * Returns a 200 OK response with user details if successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @PostMapping("/login")
    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = service.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> findAll() {
        //service.findAll();
        return ResponseEntity.ok(List.of());
    }

    /**
     * Updates the password of the currently authenticated user.
     * Returns a 200 OK response if the password is updated successfully.
     * Returns a 400 Bad Request response if the request data is invalid.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @PostMapping("/update-password")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Updates the password of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<UserResponse> updatePassword(@Valid @RequestBody UserUpdatePasswordRequest updateRequest) {
        UserResponse response = service.updatePassword(authInterceptor.getUserId(), updateRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes the currently authenticated user.
     * Returns a 204 No Content response if the user is deleted successfully.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @DeleteMapping("/delete")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Deletes the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<Void> delete() {
        service.remove(authInterceptor.getUserId());
        return ResponseEntity.noContent().build();
    }


    /**
     * Retrieves the profile details of the currently authenticated user.
     * Returns a 200 OK response with user profile details if successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @GetMapping("/profile")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get the profile of current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<UserProfileResponse> getProfile() {
        UserProfileResponse response = service.findProfile(authInterceptor.getUserId());
        return ResponseEntity.ok(response);
    }


    /**
     * Updates the profile of the currently authenticated user.
     * Returns a 200 OK response if the profile is updated successfully.
     * Returns a 400 Bad Request response if the request data is invalid.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @PostMapping("/profile")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Updates the profile of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<UserProfileResponse> saveProfile(@Valid @RequestBody UserProfileRequest request) {
        UserProfileResponse response = service.saveProfile(authInterceptor.getUserId(), request);
        return ResponseEntity.ok(response);
    }
}
