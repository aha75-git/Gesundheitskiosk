package de.aha.backend.security;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of UserDetails for Spring Security authentication.
 * Stores user ID, name, and password hash.
 */
@Builder
public class UserDetailsImpl implements UserDetails {

    private String email;
    private String password;

    /**
     * Returns authorities granted to the user (empty for this implementation).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * Returns the user's password hash (empty for this implementation).
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's username (email).
     */
    @Override
    public String getUsername() {
        return email;
    }
}