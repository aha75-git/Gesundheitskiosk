package de.aha.backend.service;

import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.repository.AdvisorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvisorService {
    private final AdvisorRepository advisorRepository;

    public List<Advisor> getAllAdvisors() {
        log.info("getAllAdvisors");
        return advisorRepository.findAll();
    }

    public Optional<Advisor> getAdvisorById(String id) {
        log.info("getAdvisorById: {}", id);
        return advisorRepository.findById(id);
    }

    public List<Advisor> searchAdvisors(String specialization, String language) {
        log.info("searchAdvisors: specialization: {} ; language: {}", specialization, language);

        if (specialization != null && language != null) {
            return advisorRepository.findBySpecializationContainingIgnoreCaseAndLanguagesContaining(
                    specialization, language);
        } else if (specialization != null) {
            return advisorRepository.findBySpecializationContainingIgnoreCase(specialization);
        } else if (language != null) {
            return advisorRepository.findByLanguagesContaining(language);
        } else {
            return getAllAdvisors();
        }
    }

    public List<Advisor> getAvailableAdvisors() {
        return advisorRepository.findByAvailableTrue();
    }

    // TODO
    public List<Advisor> findAdvisorsByName(String name) {
        log.info("findAdvisorsByName: {}", name);
        return List.of(); // Implementation for name search
    }
}
