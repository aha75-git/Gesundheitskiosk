package de.aha.backend.model.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PersonalDataTest {
    @Test
    void testPersonalDataBuilder() {
        // Arrange
        PersonalData personalData = PersonalData.builder()
                .firstName("Alice")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1985, 5, 20))
                .gender("Female")
                .build();

        // Assert
        assertNotNull(personalData);
        assertEquals("Alice", personalData.getFirstName());
        assertEquals("Female", personalData.getGender());
        assertEquals("Smith", personalData.getLastName());
    }
}