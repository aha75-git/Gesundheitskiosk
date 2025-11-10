package de.aha.backend.controller;

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
}
