package de.aha.backend.repository;

import de.aha.backend.model.user.User;
import de.aha.backend.model.user.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entities.
 * Provides methods for user-specific queries.
 */
@Repository
public interface UserRepository extends AbstractRepository<User, String>{

    /**
     * Finds a user by email, ignoring case.
     * @param email user's email address
     * @return Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    List<User> findByRoleAndProfileLanguagesContainingAndProfileSpecializationIgnoreCase(UserRole role, String language, String specialization);

    //List<User> findByRoleAndLanguagesContainingAndSpecialization(UserRole role, String language, String specialization);
    //List<User> findByRoleAndLanguagesContaining(UserRole role, String language);
    //List<User> findByRoleAndSpecialization(UserRole role, String specialization);
    List<User> findByRole(UserRole role);
}