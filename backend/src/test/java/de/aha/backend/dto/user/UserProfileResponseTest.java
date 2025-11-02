package de.aha.backend.dto.user;

import de.aha.backend.model.user.Address;
import de.aha.backend.model.user.ContactInfo;
import de.aha.backend.model.user.UserProfile;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileResponseTest {
    @Test
    void validUserProfileRequest_ShouldPassValidation() {
        UserProfileResponse response = UserProfileResponse.builder()
                .userProfile(UserProfile.builder()
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
                        .build())
                .build();

        assertEquals("123456789", response.userProfile().getContactInfo().getPhone());
        assertEquals("city", response.userProfile().getContactInfo().getAddress().getCity());
        assertEquals("4", response.userProfile().getContactInfo().getAddress().getHouseNumber());
        assertEquals("street", response.userProfile().getContactInfo().getAddress().getStreet());
        assertEquals("country", response.userProfile().getContactInfo().getAddress().getCountry());
        assertEquals("12345", response.userProfile().getContactInfo().getAddress().getPostalCode());
    }
}