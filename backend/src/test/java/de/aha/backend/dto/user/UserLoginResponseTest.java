package de.aha.backend.dto.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginResponseTest {

    @Test
    void constructor_ShouldCreateUserLoginResponseWithTokenAndUser() {
        String token = "jwt-token-123";
        UserResponse user = UserResponse.builder().email("test@example.com").build();

        UserLoginResponse response = new UserLoginResponse(token, user);

        assertEquals(token, response.token());
        assertEquals(user, response.user());
        assertEquals("test@example.com", response.user().getEmail());
    }

    @Test
    void constructor_ShouldHandleNullToken() {
        UserResponse user = UserResponse.builder().email("test@example.com").build();

        UserLoginResponse response = new UserLoginResponse(null, user);

        assertNull(response.token());
        assertNotNull(response.user());
    }

    @Test
    void constructor_ShouldHandleNullUser() {
        String token = "jwt-token-123";

        UserLoginResponse response = new UserLoginResponse(token, null);

        assertEquals(token, response.token());
        assertNull(response.user());
    }

    @Test
    void constructor_ShouldHandleEmptyToken() {
        UserResponse user = UserResponse.builder().email("test@example.com").build();

        UserLoginResponse response = new UserLoginResponse("", user);

        assertEquals("", response.token());
        assertNotNull(response.user());
    }

    @Test
    void equals_ShouldReturnTrueForSameContent() {
        UserResponse user = UserResponse.builder().email("test@example.com").build();
        UserLoginResponse response1 = new UserLoginResponse("token", user);
        UserLoginResponse response2 = new UserLoginResponse("token", user);

        assertEquals(response1, response2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentContent() {
        UserResponse user1 = UserResponse.builder().email("test1@example.com").build();
        UserResponse user2 = UserResponse.builder().email("test2@example.com").build();
        UserLoginResponse response1 = new UserLoginResponse("token", user1);
        UserLoginResponse response2 = new UserLoginResponse("token", user2);

        assertNotEquals(response1, response2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        UserResponse user = UserResponse.builder().email("test@example.com").build();
        UserLoginResponse response1 = new UserLoginResponse("token", user);
        UserLoginResponse response2 = new UserLoginResponse("token", user);

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void toString_ShouldContainTokenAndUser() {
        UserResponse user = UserResponse.builder().email("test@example.com").build();
        UserLoginResponse response = new UserLoginResponse("jwt-token-123", user);

        String toStringToken = response.toString();
        String toStringUser = response.user().getEmail();

        System.out.println(toStringToken);
        System.out.println(toStringUser);
        assertTrue(toStringToken.contains("jwt-token-123"));
        assertTrue(toStringUser.contains("test@example.com"));
    }
}