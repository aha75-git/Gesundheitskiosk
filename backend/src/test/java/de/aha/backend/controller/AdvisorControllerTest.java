package de.aha.backend.controller;

import de.aha.backend.dto.chat.ApiResponse;
import de.aha.backend.exception.AppAuthenticationException;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.appointment.DayOfWeek;
import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.service.AdvisorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdvisorControllerTest {

    @Mock
    private AdvisorService advisorService;

    @Mock
    private AuthInterceptor authInterceptor;

    @InjectMocks
    private AdvisorController advisorController;

    private final String advisorId = "advisor123";
    private final String userId = "user123";
    private Advisor testAdvisor;
    private List<Advisor> testAdvisors;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testAdvisor = Advisor.builder()
                .name("Test Advisor")
                .specialization("Psychology")
                .languages(List.of("English", "German"))
                .online(true)
                .available(true)
                .build();
        testAdvisor.setId(advisorId);

        Advisor testAdvisorX = Advisor.builder()
                .name("Another Advisor")
                .specialization("Career")
                .languages(List.of("English"))
                .online(false)
                .available(true)
                .build();
        testAdvisorX.setId("advisor456");

        testAdvisors = List.of(
                testAdvisor,
                testAdvisorX
        );
    }

    @Test
    void getAllAdvisors_success() {
        // Arrange
        when(advisorService.searchAdvisors("", "Deutsch")).thenReturn(testAdvisors);

        // Act
        ResponseEntity<List<Advisor>> result = advisorController.getAllAdvisors("", "Deutsch");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(advisorService).searchAdvisors("", "Deutsch");
    }

    @Test
    void getAllAdvisors_withFilters() {
        // Arrange
        String specialization = "Psychology";
        String language = "English";
        when(advisorService.searchAdvisors(specialization, language)).thenReturn(List.of(testAdvisor));

        // Act
        ResponseEntity<List<Advisor>> result = advisorController.getAllAdvisors(specialization, language);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("Psychology", result.getBody().get(0).getSpecialization());
        verify(advisorService).searchAdvisors(specialization, language);
    }

    @Test
    void getAdvisorById_success() {
        // Arrange
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.of(testAdvisor));

        // Act
        ResponseEntity<Advisor> result = advisorController.getAdvisorById(advisorId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(advisorId, result.getBody().getId());
        verify(advisorService).getAdvisorById(advisorId);
    }

    @Test
    void getAdvisorById_notFound() {
        // Arrange
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.empty());

        // Act
//        ResponseEntity<Advisor> result = advisorController.getAdvisorById(advisorId);

        // Assert
//        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
//        verify(advisorService).getAdvisorById(advisorId);
        assertThrows(NoSuchElementException.class, () -> advisorController.getAdvisorById(advisorId));
    }

    @Test
    void updateWorkingHours_success() {
        // Arrange
        List<WorkingHours> workingHours = List.of(
                new WorkingHours(DayOfWeek.MONDAY, "09:00", "17:00", true)
        );
        when(advisorService.updateWorkingHours(advisorId, workingHours)).thenReturn(testAdvisor);

        // Act
        ResponseEntity<Advisor> result = advisorController.updateWorkingHours(advisorId, workingHours);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(advisorId, result.getBody().getId());
        verify(advisorService).updateWorkingHours(advisorId, workingHours);
    }

    @Test
    void updateWorkingHours_notFound() {
        // Arrange
        List<WorkingHours> workingHours = List.of();
        when(advisorService.updateWorkingHours(advisorId, workingHours))
                .thenThrow(new RuntimeException("Advisor not found"));

        // Act
        ResponseEntity<Advisor> result = advisorController.updateWorkingHours(advisorId, workingHours);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(advisorService).updateWorkingHours(advisorId, workingHours);
    }

    @Test
    void getAdvisorByUser_success() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(userId);
        when(advisorService.getAdvisorByUser(userId)).thenReturn(testAdvisor);

        // Act
        ResponseEntity<Advisor> result = advisorController.getAdvisorByUser();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(advisorId, result.getBody().getId());
        verify(authInterceptor).getUserId();
        verify(advisorService).getAdvisorByUser(userId);
    }

    @Test
    void getAllAdvisorsAsApiResponse_success() {
        // Arrange
        when(advisorService.getAllAdvisors()).thenReturn(testAdvisors);

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.getAllAdvisorsAsApiResponse();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(2, result.getBody().getData().size());
        verify(advisorService).getAllAdvisors();
    }

    @Test
    void getAllAdvisorsAsApiResponse_error() {
        // Arrange
        when(advisorService.getAllAdvisors()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.getAllAdvisorsAsApiResponse();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Failed to fetch advisors: Database error", result.getBody().getError());
        verify(advisorService).getAllAdvisors();
    }

    @Test
    void getAvailableAdvisors_success() {
        // Arrange
        when(advisorService.getAvailableAdvisors()).thenReturn(testAdvisors);

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.getAvailableAdvisors();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(2, result.getBody().getData().size());
        verify(advisorService).getAvailableAdvisors();
    }

    @Test
    void getAvailableAdvisors_error() {
        // Arrange
        when(advisorService.getAvailableAdvisors()).thenThrow(new RuntimeException("Service unavailable"));

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.getAvailableAdvisors();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Failed to fetch available advisors: Service unavailable", result.getBody().getError());
        verify(advisorService).getAvailableAdvisors();
    }

    @Test
    void getOnlineAdvisors_success() {
        // Arrange
        List<Advisor> onlineAdvisors = List.of(testAdvisor);
        when(advisorService.getOnlineAdvisors()).thenReturn(onlineAdvisors);

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.getOnlineAdvisors();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(1, result.getBody().getData().size());
        assertTrue(result.getBody().getData().get(0).getOnline());
        verify(advisorService).getOnlineAdvisors();
    }

    @Test
    void getOnlineAdvisors_error() {
        // Arrange
        when(advisorService.getOnlineAdvisors()).thenThrow(new RuntimeException("Network error"));

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.getOnlineAdvisors();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Failed to fetch online advisors: Network error", result.getBody().getError());
        verify(advisorService).getOnlineAdvisors();
    }

    @Test
    void getAdvisorByIdAsApiResponse_success() {
        // Arrange
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.of(testAdvisor));

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.getAdvisorByIdAsApiResponse(advisorId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(advisorId, result.getBody().getData().getId());
        verify(advisorService).getAdvisorById(advisorId);
    }

    @Test
    void getAdvisorByIdAsApiResponse_notFound() {
        // Arrange
        when(advisorService.getAdvisorById(advisorId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.getAdvisorByIdAsApiResponse(advisorId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Failed to fetch advisor: Advisor not found", result.getBody().getError());
        verify(advisorService).getAdvisorById(advisorId);
    }

    @Test
    void updateOnlineStatus_success() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(advisorService.updateAdvisorOnlineStatus(advisorId, true)).thenReturn(testAdvisor);

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.updateOnlineStatus(advisorId, true);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(advisorId, result.getBody().getData().getId());
        verify(authInterceptor).getUserId();
        verify(advisorService).updateAdvisorOnlineStatus(advisorId, true);
    }

    @Test
    void updateOnlineStatus_unauthorized() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn("differentUser");

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.updateOnlineStatus(advisorId, true);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Not authorized to update this advisor", result.getBody().getError());
        verify(authInterceptor).getUserId();
        verify(advisorService, never()).updateAdvisorOnlineStatus(anyString(), anyBoolean());
    }

    @Test
    void updateOnlineStatus_error() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(advisorService.updateAdvisorOnlineStatus(advisorId, true))
                .thenThrow(new RuntimeException("Update failed"));

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.updateOnlineStatus(advisorId, true);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Failed to update online status: Update failed", result.getBody().getError());
        verify(advisorService).updateAdvisorOnlineStatus(advisorId, true);
    }

    @Test
    void updateAvailability_success() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(advisorService.updateAdvisorAvailability(advisorId, false)).thenReturn(testAdvisor);

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.updateAvailability(advisorId, false);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(advisorId, result.getBody().getData().getId());
        verify(authInterceptor).getUserId();
        verify(advisorService).updateAdvisorAvailability(advisorId, false);
    }

    @Test
    void updateAvailability_unauthorized() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn("differentUser");

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.updateAvailability(advisorId, true);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Not authorized to update this advisor", result.getBody().getError());
        verify(authInterceptor).getUserId();
        verify(advisorService, never()).updateAdvisorAvailability(anyString(), anyBoolean());
    }

    @Test
    void updateAvailability_error() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        when(advisorService.updateAdvisorAvailability(advisorId, true))
                .thenThrow(new RuntimeException("Availability update failed"));

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.updateAvailability(advisorId, true);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Failed to update availability: Availability update failed", result.getBody().getError());
        verify(advisorService).updateAdvisorAvailability(advisorId, true);
    }

    @Test
    void searchAdvisors_success() {
        // Arrange
        String specialization = "Psychology";
        List<String> languages = List.of("English", "German");
        when(advisorService.searchAdvisors(specialization, languages)).thenReturn(List.of(testAdvisor));

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.searchAdvisors(specialization, languages);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(1, result.getBody().getData().size());
        assertEquals("Psychology", result.getBody().getData().get(0).getSpecialization());
        verify(advisorService).searchAdvisors(specialization, languages);
    }

    @Test
    void searchAdvisors_withNullParameters() {
        // Arrange
        when(advisorService.searchAdvisors(null, List.of("Deutsch"))).thenReturn(testAdvisors);

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.searchAdvisors(null, List.of("Deutsch"));

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertEquals(2, result.getBody().getData().size());
        verify(advisorService).searchAdvisors(null, List.of("Deutsch"));
    }

    @Test
    void searchAdvisors_error() {
        // Arrange
        String specialization = "Psychology";
        List<String> languages = List.of("English");
        when(advisorService.searchAdvisors(specialization, languages))
                .thenThrow(new RuntimeException("Search failed"));

        // Act
        ResponseEntity<ApiResponse<List<Advisor>>> result = advisorController.searchAdvisors(specialization, languages);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.getBody().isSuccess());
//        assertEquals("Failed to search advisors: Search failed", result.getBody().getError());
        verify(advisorService).searchAdvisors(specialization, languages);
    }

    @Test
    void updateOnlineStatus_offline() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        testAdvisor.setOnline(false);
        when(advisorService.updateAdvisorOnlineStatus(advisorId, false)).thenReturn(testAdvisor);

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.updateOnlineStatus(advisorId, false);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertFalse(result.getBody().getData().getOnline());
        verify(advisorService).updateAdvisorOnlineStatus(advisorId, false);
    }

    @Test
    void updateAvailability_unavailable() {
        // Arrange
        when(authInterceptor.getUserId()).thenReturn(advisorId);
        testAdvisor.setAvailable(false);
        when(advisorService.updateAdvisorAvailability(advisorId, false)).thenReturn(testAdvisor);

        // Act
        ResponseEntity<ApiResponse<Advisor>> result = advisorController.updateAvailability(advisorId, false);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isSuccess());
        assertFalse(result.getBody().getData().getAvailable());
        verify(advisorService).updateAdvisorAvailability(advisorId, false);
    }
}