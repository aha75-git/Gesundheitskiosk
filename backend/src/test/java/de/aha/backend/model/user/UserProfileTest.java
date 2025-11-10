package de.aha.backend.model.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {
    @Test
    void testUserProfileBuilder() {
        // Arrange
        PersonalData personalData = PersonalData.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender("Male")
                .build();

        MedicalInfo medicalInfo = MedicalInfo.builder()
                .bloodType("O+")
                .allergies(List.of("Peanuts"))
                .chronicConditions(List.of("Asthma"))
                .currentMedications(List.of(new Medication("Ventolin", "200 mcg", "As needed")))
                .emergencyContact(new EmergencyContact("Jane Doe", "1234567890", "Spouse"))
                .build();

        ContactInfo contactInfo = ContactInfo.builder()
                .phone("9876543210")
                .address(new Address("Main St", "Springfield", "12345", "USA", "15"))
                .allowHouseVisits(true)
                .build();

        // Act
        UserProfile userProfile = UserProfile.builder()
                .personalData(personalData)
                .medicalInfo(medicalInfo)
                .contactInfo(contactInfo)
                .specialization("Cardiologist")
                .languages(List.of("English", "Spanish"))
                .qualification("MBBS")
                .bio("Experienced Cardiologist.")
                .rating(4.5)
                .reviewCount(100)
                .build();

        // Assert
        assertNotNull(userProfile);
        assertEquals("John", userProfile.getPersonalData().getFirstName());
        assertEquals("O+", userProfile.getMedicalInfo().getBloodType());
        assertTrue(userProfile.getContactInfo().isAllowHouseVisits());
        assertEquals(4.5, userProfile.getRating());
    }
}