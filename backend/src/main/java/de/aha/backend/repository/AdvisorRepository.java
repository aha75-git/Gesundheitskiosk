package de.aha.backend.repository;

import de.aha.backend.model.appointment.Advisor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvisorRepository extends MongoRepository<Advisor, String> {

    List<Advisor> findBySpecializationContainingIgnoreCase(String specialization);

    List<Advisor> findByLanguagesContaining(String language);

    List<Advisor> findBySpecializationContainingIgnoreCaseAndLanguagesContaining(
            String specialization, String language);

    Optional<Advisor> findByNameContainingIgnoreCase(String name);
}