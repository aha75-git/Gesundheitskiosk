package de.aha.backend.model.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User domain entity.
 */
class UserTest {

    @Test
    void constructor_ShouldCreateEmptyUser() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getConsents());
        assertNull(user.getProfile());
        assertNull(user.getPreferences());
        assertNull(user.getProvider());
        assertNull(user.getProviderId());
        assertNull(user.getUsername());
        assertNull(user.getCreationDate());
        assertNull(user.getModifyDate());
    }

    @Test
    void setEmail_ShouldSetEmailCorrectly() {
        User user = new User();
        String email = "test@example.com";

        user.setEmail(email);

        assertEquals(email, user.getEmail());
    }

    @Test
    void setPasswordHash_ShouldSetPasswordHashCorrectly() {
        User user = new User();
        String passwordHash = "hashedPassword123";

        user.setPassword(passwordHash);

        assertEquals(passwordHash, user.getPassword());
    }

    @Test
    void setConsents_ShouldSetConsentsCorrectly() {
        User user = new User();
        List<Consent> consents = Arrays.asList(
                Consent.builder().granted(true).type("Hausbesuch").version("1").build(),
                Consent.builder().granted(false).type("Vollmacht").version("2").build(),
                Consent.builder().granted(true).type("Anruf").version("1").build()
        );

        user.setConsents(consents);

        assertEquals(consents, user.getConsents());
        assertEquals(3, user.getConsents().size());
    }

    @Test
    void setId_ShouldSetIdCorrectly() {
        User user = new User();
        String id = "user123";

        user.setId(id);

        assertEquals(id, user.getId());
    }

    @Test
    void setTimestamps_ShouldSetTimestampsCorrectly() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setCreationDate(now);
        user.setModifyDate(now);

        assertEquals(now, user.getCreationDate());
        assertEquals(now, user.getModifyDate());
    }

    @Test
    void fullUserObject_ShouldWorkCorrectly() {
        User user = new User();
        String id = "user123";
        String email = "test@example.com";
        String passwordHash = "hashedPassword123";
        LocalDateTime now = LocalDateTime.now();
        List<Consent> consents = Arrays.asList(
                Consent.builder().granted(true).type("Hausbesuch").version("1").build(),
                Consent.builder().granted(false).type("Vollmacht").version("2").build(),
                Consent.builder().granted(true).type("Anruf").version("1").build()
        );
        UserRole userRole = UserRole.ADVISOR;
        boolean enabled = false;
        String username = "username";
        String providerId = "providerId";
        String provider = "provider";

        Address address = Address.builder()
                .city("Dortmund")
                .country("Deutschland")
                .postalCode("33444")
                .street("Musterstrasse")
                .houseNumber("4a")
                .build();

        ContactInfo contactInfo = ContactInfo.builder()
                .address(address)
                .phone("+49177466336")
                .allowHouseVisits(true)
                .build();

        Medication medication1 = Medication.builder()
                .dosage("20 ml.")
                .name("Paracetamol")
                .frequency("2 x am Tag")
                .build();

        Medication medication2 = Medication.builder()
                .dosage("20 ml.")
                .name("Natural")
                .frequency("3 x am Tag")
                .build();

        EmergencyContact emergencyContact = EmergencyContact.builder()
                .name("Herr. Max Mustermann")
                .phone("+49177466336")
                .relationship("Husband")
                .build();

        List<String> chronicConditions = List.of("Herz");

        MedicalInfo medicalInfo = MedicalInfo.builder()
                .allergies(Arrays.asList("Polenallergie", "Katzenallergie"))
                .bloodType("A")
                .chronicConditions(chronicConditions)
                .emergencyContact(emergencyContact)
                .currentMedications(List.of(medication1, medication2))
                .build();

        PersonalData personalData = PersonalData.builder()
                .firstName("Petra")
                .lastName("Mustermann")
                .dateOfBirth(LocalDate.of(1957, Month.MARCH, 28))
                .gender("Frau")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .bio("bio")
                .languages(List.of("en", "de"))
                .rating(5.0)
                .reviewCount(3)
                .qualification("qualification")
                .specialization("specialization")
                .contactInfo(contactInfo)
                .medicalInfo(medicalInfo)
                .personalData(personalData)
                .build();

        NotificationSettings notificationSettings = NotificationSettings.builder()
                .smsNotifications(true)
                .emailNotifications(true)
                .build();

        UserPreferences userPreferences = UserPreferences.builder()
                .language("DE")
                .notificationSettings(notificationSettings)
                .build();

        user.setId(id);
        user.setEmail(email);
        user.setPassword(passwordHash);
        user.setConsents(consents);
        user.setCreationDate(now);
        user.setModifyDate(now);
        user.setRole(userRole);
        user.setEnabled(enabled);
        user.setUsername(username);
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setProfile(userProfile);
        user.setPreferences(userPreferences);

        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPassword());
        assertEquals(consents, user.getConsents());
        assertEquals(now, user.getCreationDate());
        assertEquals(now, user.getModifyDate());
        assertEquals(userRole, user.getRole());
        assertFalse(user.isEnabled());
        assertEquals(username, user.getUsername());
        assertEquals(provider, user.getProvider());
        assertEquals(providerId, user.getProviderId());
        assertEquals(userProfile, user.getProfile());
        assertEquals(userPreferences, user.getPreferences());
    }

    @Test
    void setEmptyConsents_ShouldWorkCorrectly() {
        User user = new User();
        List<Consent> emptyList = List.of();

        user.setConsents(emptyList);

        assertNotNull(user.getConsents());
        assertTrue(user.getConsents().isEmpty());
    }

    @Test
    void setNullValues_ShouldWorkCorrectly() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hash");

        user.setEmail(null);
        user.setPassword(null);
        user.setConsents(null);

        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getConsents());
    }
}
