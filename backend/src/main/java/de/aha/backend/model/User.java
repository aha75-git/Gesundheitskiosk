package de.aha.backend.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a user entity stored in MongoDB.
 * Contains user email, password hash, and associated restaurant IDs.
 */
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class User extends AbstractDocument implements UserDetails {

    /**
     * User name.
     */
    @Field("username")
    private String username;

    /**
     * User's email address. Must be unique.
     */
    @Indexed(unique = true)
    @Field("email")
    private String email;

    /**
     * Hashed password of the user.
     */
    @Field("password_hash")
    private String password; // passwordHash;

    /**
     * Enum of user roles associated with the user.
     */
    @Field("role")
    private UserRole role;

    /**
     * Is the user active and therefore enabled?
     */
    @Field("enabled")
    private boolean enabled = true;

    /**
     * OAuth Provider such as "local", "github", "google"
     */
    @Field("provider")
    private String provider;

    /**
     * ID from OAuth provider
     */
    @Field("provider_id")
    private String providerId;

    /**
     * User profile
     */
    private UserProfile profile;

    /**
     * User preferences
     */
    private UserPreferences preferences;

    /**
     * User consents
     */
    private List<Consent> consents;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}


