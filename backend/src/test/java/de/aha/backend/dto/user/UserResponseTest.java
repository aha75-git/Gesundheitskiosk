package de.aha.backend.dto.user;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {
    private String email;

    @BeforeEach
    void setUp() {
        email = "test@example.com";
    }

    @Test
    void constructor_ShouldCreateUserResponseWithEmail() {

        UserResponse response = UserResponse.builder().email(email).build();

        assertEquals(email, response.getEmail());
    }

    @Test
    void constructor_ShouldHandleNullEmail() {
        UserResponse response = UserResponse.builder().email(null).build();

        assertNull(response.getEmail());
    }

    @Test
    void constructor_ShouldHandleEmptyEmail() {
        UserResponse response = UserResponse.builder().email("").build();

        assertEquals("", response.getEmail());
    }

    @Test
    void equals_ShouldReturnTrueForSameContent() {
        UserResponse response1 = UserResponse.builder().email(email).build();
        UserResponse response2 = UserResponse.builder().email(email).build();

        assertEquals(response1, response2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentContent() {
        UserResponse response1 = UserResponse.builder().email("test1@example.com").build();
        UserResponse response2 = UserResponse.builder().email("test2@example.com").build();

        assertNotEquals(response1, response2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        UserResponse response1 = UserResponse.builder().email(email).build();
        UserResponse response2 = UserResponse.builder().email(email).build();

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void toString_ShouldContainEmail() {
        UserResponse response = UserResponse.builder().email(email).build();

        String toString = response.getEmail();

        assertTrue(toString.contains(email));
    }
}