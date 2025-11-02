package de.aha.backend.controller;

import de.aha.backend.dto.appointment.AppointmentResponse;
import de.aha.backend.dto.appointment.AvailabilityResponse;
import de.aha.backend.dto.appointment.CreateAppointmentRequest;
import de.aha.backend.dto.appointment.UpdateAppointmentStatusRequest;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.security.AuthRequired;
import de.aha.backend.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@AuthRequired(AuthInterceptor.class)
@Tag(name = "appointment", description = "Appointment endpoints")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AuthInterceptor authInterceptor;

    /**
     * Create an appointment of the user.
     * Returns a 201 CREATED if a user created successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        var response = appointmentService.createAppointment(request, authInterceptor.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the appointment of the currently authenticated user.
     * Returns a 200 OK response with user appointment if successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @GetMapping("/{appointmentId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get the appointment of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User appointment"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable String appointmentId) {
        var response = appointmentService.getAppointment(appointmentId, authInterceptor.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves appointments of the currently authenticated user.
     * Returns a 200 OK response with user appointments if successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get appointments of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User appointments"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<List<AppointmentResponse>> getUserAppointments(@RequestParam(required = false) LocalDate date) {
        var appointments = appointmentService.getUserAppointments(date, authInterceptor.getUserId());
        return ResponseEntity.ok(appointments);
    }

    /**
     * Update appointment status of the currently authenticated user.
     * Returns a 200 OK response if the appointment updated successful.
     * Returns a 401 Unauthorized response if the user is not authenticated.
     * Returns a 500 Internal Server Error response if an unexpected error occurs.
     * Returns a 503 Service Unavailable response if the service is temporarily unavailable.
     */
    @PutMapping("/{appointmentId}/status")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update the appointment of the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User appointments"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service unavailable"),
    })
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable String appointmentId,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        var response = appointmentService.updateAppointmentStatus(appointmentId, request, authInterceptor.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/availability/{advisorId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AvailabilityResponse> checkAvailability(
            @PathVariable String advisorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AvailabilityResponse response = appointmentService.checkAvailability(advisorId, date);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{appointmentId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> cancelAppointment(@PathVariable String appointmentId) {
        appointmentService.cancelAppointment(appointmentId, authInterceptor.getUserId());
        return ResponseEntity.ok().build();
    }
}
