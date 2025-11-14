package de.aha.backend.controller;

import de.aha.backend.dto.chat.ApiResponse;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.security.AuthRequired;
import de.aha.backend.service.AdvisorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/advisors")
@RequiredArgsConstructor
@AuthRequired(AuthInterceptor.class)
@Tag(name = "advisor", description = "advisor endpoints")
public class AdvisorController {
    private final AdvisorService advisorService;
    private final AuthInterceptor authInterceptor;

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Advisor>> getAllAdvisors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String language) {
        List<Advisor> advisors = advisorService.searchAdvisors(specialization, language);
        return ResponseEntity.ok(advisors);
    }

    @GetMapping("/{advisorId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Advisor> getAdvisorById(@PathVariable String advisorId) {
        Optional<Advisor> advisor = advisorService.getAdvisorById(advisorId);
        System.out.println("Get Advisor by ID: " + advisor.get());
        return advisor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // WorkingHours Update Endpoint
    @PutMapping("/{id}/working-hours")
    public ResponseEntity<Advisor> updateWorkingHours(
            @PathVariable String id,
            @RequestBody List<WorkingHours> workingHours) {
        try {
            Advisor updatedAdvisor = advisorService.updateWorkingHours(id, workingHours);
            return ResponseEntity.ok(updatedAdvisor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/advisor")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Advisor> getAdvisorByUser() {
        Advisor advisor = advisorService.getAdvisorByUser(authInterceptor.getUserId());
        System.out.println("Get Advisor by ID: " + advisor);
        return ResponseEntity.ok(advisor);
    }



    /// ////////////  erweiterte mit Chat


    @GetMapping("/apiresponse")
    public ResponseEntity<ApiResponse<List<Advisor>>> getAllAdvisorsAsApiResponse() {
        try {
            List<Advisor> advisors = advisorService.getAllAdvisors();
            return ResponseEntity.ok(ApiResponse.success(advisors));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch advisors: " + e.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Advisor>>> getAvailableAdvisors() {
        try {
            List<Advisor> advisors = advisorService.getAvailableAdvisors();
            return ResponseEntity.ok(ApiResponse.success(advisors));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch available advisors: " + e.getMessage()));
        }
    }

    @GetMapping("/online")
    public ResponseEntity<ApiResponse<List<Advisor>>> getOnlineAdvisors() {
        try {
            List<Advisor> advisors = advisorService.getOnlineAdvisors();
            return ResponseEntity.ok(ApiResponse.success(advisors));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch online advisors: " + e.getMessage()));
        }
    }

    @GetMapping("/apiresponse/{id}")
    public ResponseEntity<ApiResponse<Advisor>> getAdvisorByIdAsApiResponse(@PathVariable String id) {
        try {
            Advisor advisor = advisorService.getAdvisorById(id)
                    .orElseThrow(() -> new RuntimeException("Advisor not found"));
            return ResponseEntity.ok(ApiResponse.success(advisor));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch advisor: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/online")
    public ResponseEntity<ApiResponse<Advisor>> updateOnlineStatus(
            @PathVariable String id,
            @RequestParam boolean online) {

        var advisorId = authInterceptor.getUserId();

        try {
            if (!id.equals(advisorId)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Not authorized to update this advisor"));
            }

            Advisor advisor = advisorService.updateAdvisorOnlineStatus(id, online);
            return ResponseEntity.ok(ApiResponse.success(advisor));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update online status: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<Advisor>> updateAvailability(
            @PathVariable String id,
            @RequestParam boolean available) {

        var advisorId = authInterceptor.getUserId();

        try {
            if (!id.equals(advisorId)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Not authorized to update this advisor"));
            }

            Advisor advisor = advisorService.updateAdvisorAvailability(id, available);
            return ResponseEntity.ok(ApiResponse.success(advisor));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update availability: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Advisor>>> searchAdvisors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) List<String> languages) {

        try {
            List<Advisor> advisors = advisorService.searchAdvisors(specialization, languages);
            return ResponseEntity.ok(ApiResponse.success(advisors));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to search advisors: " + e.getMessage()));
        }
    }
}
