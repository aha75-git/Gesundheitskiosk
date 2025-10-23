package de.aha.backend.controller;

import de.aha.backend.dto.user.*;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
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


    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        var response = service.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
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
}
