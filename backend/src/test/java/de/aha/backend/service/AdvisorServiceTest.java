package de.aha.backend.service;

import de.aha.backend.exception.NotFoundObjectException;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.appointment.DayOfWeek;
import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.repository.AdvisorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdvisorServiceTest {

    @Mock
    private AdvisorRepository advisorRepository;

    @InjectMocks
    private AdvisorService advisorService;

    private Advisor testAdvisor;
    private List<Advisor> testAdvisors;
    private List<WorkingHours> testWorkingHours;

    @BeforeEach
    void setUp() {
        testWorkingHours = List.of(
                new WorkingHours(DayOfWeek.MONDAY, "09:00", "17:00", true),
                new WorkingHours(DayOfWeek.TUESDAY, "09:00", "17:00", true)
        );

        testAdvisor = Advisor.builder()
                .userId("user123")
                .name("Test Advisor")
                .specialization("Psychology")
                .languages(List.of("English", "German"))
                .online(true)
                .available(true)
                .workingHours(testWorkingHours)
                .lastSeen(LocalDateTime.now())
                .build();
        testAdvisor.setId("advisor123");


        Advisor adv1 = Advisor.builder()
                .userId("user456")
                .name("Another Advisor")
                .specialization("Career Counseling")
                .languages(List.of("English", "French"))
                .online(false)
                .available(true)
                .build();

        adv1.setId("advisor456");

        Advisor adv2 = Advisor.builder()
                        .userId("user789")
                        .name("Third Advisor")
                        .specialization("Psychology")
                        .languages(List.of("German"))
                        .online(true)
                        .available(false)
                        .build();
        adv2.setId("advisor789");


        testAdvisors = List.of(
                testAdvisor,
                adv1,
                adv2
        );
    }

    @Test
    void save_success() {
        // Arrange
        when(advisorRepository.save(any(Advisor.class))).thenReturn(testAdvisor);

        // Act
        advisorService.save(testAdvisor);

        // Assert
        verify(advisorRepository).save(testAdvisor);
    }

    @Test
    void getAllAdvisors_success() {
        // Arrange
        when(advisorRepository.findAll()).thenReturn(testAdvisors);

        // Act
        List<Advisor> result = advisorService.getAllAdvisors();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(advisorRepository).findAll();
    }

    @Test
    void getAdvisorById_success() {
        // Arrange
        String advisorId = "advisor123";
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(testAdvisor));

        // Act
        Optional<Advisor> result = advisorService.getAdvisorById(advisorId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(advisorId, result.get().getId());
        verify(advisorRepository).findById(advisorId);
    }

    @Test
    void getAdvisorById_notFound() {
        // Arrange
        String advisorId = "nonexistent";
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.empty());

        // Act
        Optional<Advisor> result = advisorService.getAdvisorById(advisorId);

        // Assert
        assertFalse(result.isPresent());
        verify(advisorRepository).findById(advisorId);
    }

    @Test
    void getAdvisorByUser_success() {
        // Arrange
        String userId = "user123";
        when(advisorRepository.findByUserId(userId)).thenReturn(Optional.of(testAdvisor));

        // Act
        Advisor result = advisorService.getAdvisorByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(advisorRepository).findByUserId(userId);
    }

    @Test
    void getAdvisorByUser_notFound() {
        // Arrange
        String userId = "nonexistent";
        when(advisorRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundObjectException.class, () -> advisorService.getAdvisorByUser(userId));
        verify(advisorRepository).findByUserId(userId);
    }

    @Test
    void getAdvisorByUserId_success() {
        // Arrange
        String userId = "user123";
        when(advisorRepository.findByUserId(userId)).thenReturn(Optional.of(testAdvisor));

        // Act
        Optional<Advisor> result = advisorService.getAdvisorByUserId(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
        verify(advisorRepository).findByUserId(userId);
    }

    @Test
    void getAdvisorByUserId_notFound() {
        // Arrange
        String userId = "nonexistent";
        when(advisorRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        Optional<Advisor> result = advisorService.getAdvisorByUserId(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(advisorRepository).findByUserId(userId);
    }

    @Test
    void searchAdvisors_withSpecializationAndLanguage() {
        // Arrange
        String specialization = "Psychology";
        String language = "English";
        List<Advisor> expectedAdvisors = List.of(testAdvisor);
        when(advisorRepository.findBySpecializationContainingIgnoreCaseAndLanguagesContaining(specialization, language))
                .thenReturn(expectedAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(specialization, language);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(advisorRepository).findBySpecializationContainingIgnoreCaseAndLanguagesContaining(specialization, language);
    }

    @Test
    void searchAdvisors_withSpecializationOnly() {
        // Arrange
        String language = null;
        String specialization = "Psychology";
        List<Advisor> expectedAdvisors = List.of(testAdvisor, testAdvisors.get(2));
        when(advisorRepository.findBySpecializationContainingIgnoreCase(specialization))
                .thenReturn(expectedAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(specialization, language);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(advisorRepository).findBySpecializationContainingIgnoreCase(specialization);
    }

    @Test
    void searchAdvisors_withLanguageOnly() {
        // Arrange
        String language = "German";
        List<Advisor> expectedAdvisors = List.of(testAdvisor, testAdvisors.get(2));
        when(advisorRepository.findByLanguagesContaining(language)).thenReturn(expectedAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(null, language);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(advisorRepository).findByLanguagesContaining(language);
    }

    @Test
    void searchAdvisors_withNoFilters() {
        // Arrange
        String language = null;
        when(advisorRepository.findAll()).thenReturn(testAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(null, language);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(advisorRepository).findAll();
    }

    @Test
    void getAvailableAdvisors_success() {
        // Arrange
        List<Advisor> availableAdvisors = List.of(testAdvisors.get(0), testAdvisors.get(1));
        when(advisorRepository.findByAvailableTrue()).thenReturn(availableAdvisors);

        // Act
        List<Advisor> result = advisorService.getAvailableAdvisors();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        result.forEach(advisor -> assertTrue(advisor.getAvailable()));
        verify(advisorRepository).findByAvailableTrue();
    }

    @Test
    void findAdvisorsByName_success() {
        // Act
        List<Advisor> result = advisorService.findAdvisorsByName("Test");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Currently returns empty list as per TODO
    }

    @Test
    void updateWorkingHours_success() {
        // Arrange
        String advisorId = "advisor123";
        List<WorkingHours> newWorkingHours = List.of(
                new WorkingHours(DayOfWeek.MONDAY, "10:00", "18:00", true),
                new WorkingHours(DayOfWeek.WEDNESDAY, "09:00", "17:00", true)
        );

        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(testAdvisor));
        when(advisorRepository.save(any(Advisor.class))).thenReturn(testAdvisor);

        // Act
        Advisor result = advisorService.updateWorkingHours(advisorId, newWorkingHours);

        // Assert
        assertNotNull(result);
        assertEquals(newWorkingHours, result.getWorkingHours());
        verify(advisorRepository).findById(advisorId);
        verify(advisorRepository).save(testAdvisor);
    }

    @Test
    void updateWorkingHours_advisorNotFound() {
        // Arrange
        String advisorId = "nonexistent";
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                advisorService.updateWorkingHours(advisorId, testWorkingHours));
        verify(advisorRepository).findById(advisorId);
        verify(advisorRepository, never()).save(any(Advisor.class));
    }

    @Test
    void getOnlineAdvisors_success() {
        // Arrange
        List<Advisor> onlineAdvisors = List.of(testAdvisors.get(0), testAdvisors.get(2));
        when(advisorRepository.findOnlineAdvisors()).thenReturn(onlineAdvisors);

        // Act
        List<Advisor> result = advisorService.getOnlineAdvisors();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        result.forEach(advisor -> assertTrue(advisor.getOnline()));
        verify(advisorRepository).findOnlineAdvisors();
    }

    @Test
    void updateAdvisorOnlineStatus_success() {
        // Arrange
        String advisorId = "advisor123";
        boolean newOnlineStatus = false;

        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(testAdvisor));
        when(advisorRepository.save(any(Advisor.class))).thenReturn(testAdvisor);

        // Act
        Advisor result = advisorService.updateAdvisorOnlineStatus(advisorId, newOnlineStatus);

        // Assert
        assertNotNull(result);
        assertEquals(newOnlineStatus, result.getOnline());
        assertNotNull(result.getLastSeen());
        verify(advisorRepository).findById(advisorId);
        verify(advisorRepository).save(testAdvisor);
    }

    @Test
    void updateAdvisorOnlineStatus_advisorNotFound() {
        // Arrange
        String advisorId = "nonexistent";
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                advisorService.updateAdvisorOnlineStatus(advisorId, true));
        verify(advisorRepository).findById(advisorId);
        verify(advisorRepository, never()).save(any(Advisor.class));
    }

    @Test
    void updateAdvisorAvailability_success() {
        // Arrange
        String advisorId = "advisor123";
        boolean newAvailability = false;

        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(testAdvisor));
        when(advisorRepository.save(any(Advisor.class))).thenReturn(testAdvisor);

        // Act
        Advisor result = advisorService.updateAdvisorAvailability(advisorId, newAvailability);

        // Assert
        assertNotNull(result);
        assertEquals(newAvailability, result.getAvailable());
        verify(advisorRepository).findById(advisorId);
        verify(advisorRepository).save(testAdvisor);
    }

    @Test
    void updateAdvisorAvailability_advisorNotFound() {
        // Arrange
        String advisorId = "nonexistent";
        when(advisorRepository.findById(advisorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                advisorService.updateAdvisorAvailability(advisorId, true));
        verify(advisorRepository).findById(advisorId);
        verify(advisorRepository, never()).save(any(Advisor.class));
    }

    @Test
    void searchAdvisors_withSpecializationAndLanguagesList() {
        // Arrange
        String specialization = "Psychology";
        List<String> languages = List.of("English", "German");
        List<Advisor> expectedAdvisors = List.of(testAdvisor);

        when(advisorRepository.findBySpecializationContainingIgnoreCase(specialization))
                .thenReturn(expectedAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(specialization, languages);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(advisorRepository).findBySpecializationContainingIgnoreCase(specialization);
    }

    @Test
    void searchAdvisors_withSpecializationOnlyInListVersion() {
        // Arrange
        String language = null;
        String specialization = "Career Counseling";
        List<Advisor> expectedAdvisors = List.of(testAdvisors.get(1));

        when(advisorRepository.findBySpecializationContainingIgnoreCase(specialization))
                .thenReturn(expectedAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(specialization, language);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Career Counseling", result.get(0).getSpecialization());
        verify(advisorRepository).findBySpecializationContainingIgnoreCase(specialization);
    }

    @Test
    void searchAdvisors_withLanguagesListOnly() {
        // Arrange
        List<String> languages = List.of("French");
        List<Advisor> expectedAdvisors = List.of(testAdvisors.get(1));

        when(advisorRepository.findByLanguages(languages)).thenReturn(expectedAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(null, languages);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getLanguages().contains("French"));
        verify(advisorRepository).findByLanguages(languages);
    }

    @Test
    void searchAdvisors_withEmptyLanguagesList() {
        // Arrange
        List<String> languages = List.of();
        when(advisorRepository.findAll()).thenReturn(testAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(null, languages);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(advisorRepository).findAll();
    }

    @Test
    void searchAdvisors_withNoFiltersInListVersion() {
        // Arrange
        List<String> languages = null;
        when(advisorRepository.findAll()).thenReturn(testAdvisors);

        // Act
        List<Advisor> result = advisorService.searchAdvisors(null, languages);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(advisorRepository).findAll();
    }

    @Test
    void updateAdvisorOnlineStatus_updatesLastSeen() {
        // Arrange
        String advisorId = "advisor123";
        LocalDateTime beforeUpdate = LocalDateTime.now().minusSeconds(1);

        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(testAdvisor));
        when(advisorRepository.save(any(Advisor.class))).thenAnswer(invocation -> {
            Advisor savedAdvisor = invocation.getArgument(0);
            return savedAdvisor;
        });

        // Act
        Advisor result = advisorService.updateAdvisorOnlineStatus(advisorId, true);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getLastSeen());
        assertTrue(result.getLastSeen().isAfter(beforeUpdate));
        verify(advisorRepository).save(testAdvisor);
    }

    @Test
    void updateWorkingHours_preservesOtherFields() {
        // Arrange
        String advisorId = "advisor123";
        List<WorkingHours> newWorkingHours = List.of(
                new WorkingHours(DayOfWeek.FRIDAY, "08:00", "16:00", true)
        );

        Advisor originalAdvisor = Advisor.builder()
                .userId("user123")
                .name("Original Name")
                .specialization("Original Specialization")
                .online(true)
                .available(false)
                .build();
        originalAdvisor.setId(advisorId);

        when(advisorRepository.findById(advisorId)).thenReturn(Optional.of(originalAdvisor));
        when(advisorRepository.save(any(Advisor.class))).thenReturn(originalAdvisor);

        // Act
        Advisor result = advisorService.updateWorkingHours(advisorId, newWorkingHours);

        // Assert
        assertNotNull(result);
        assertEquals(newWorkingHours, result.getWorkingHours());
        assertEquals("Original Name", result.getName()); // Other fields should be preserved
        assertEquals("Original Specialization", result.getSpecialization());
        assertTrue(result.getOnline());
        assertFalse(result.getAvailable());
    }
}