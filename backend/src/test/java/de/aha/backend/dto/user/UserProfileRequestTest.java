package de.aha.backend.dto.user;

import de.aha.backend.model.user.Address;
import de.aha.backend.model.user.ContactInfo;
import de.aha.backend.model.user.UserRole;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validUserProfileRequest_ShouldPassValidation() {
        UserProfileRequest request = UserProfileRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .contactInfo(ContactInfo.builder()
                        .phone("123456789")
                        .allowHouseVisits(true)
                        .address(Address.builder()
                                .city("city")
                                .country("country")
                                .street("street")
                                .houseNumber("4")
                                .postalCode("12345")
                                .build())
                        .build())
                .build();

        Set<ConstraintViolation<UserProfileRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("testuser", request.getUsername());
        assertEquals("123456789", request.getContactInfo().getPhone());
        assertEquals("city", request.getContactInfo().getAddress().getCity());
        assertEquals("4", request.getContactInfo().getAddress().getHouseNumber());
        assertEquals("street", request.getContactInfo().getAddress().getStreet());
        assertEquals("country", request.getContactInfo().getAddress().getCountry());
        assertEquals("12345", request.getContactInfo().getAddress().getPostalCode());
    }
}