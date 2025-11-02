package de.aha.backend.service;

import de.aha.backend.model.appointment.Advisor;
import de.aha.backend.repository.AdvisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvisorService {
    private final AdvisorRepository advisorRepository;

    public List<Advisor> getAllAdvisors() {
        return advisorRepository.findAll();
    }

    public Optional<Advisor> getAdvisorById(String id) {
        return advisorRepository.findById(id);
    }

    public List<Advisor> searchAdvisors(String specialization, String language) {
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

    // TODO
    public List<Advisor> findAdvisorsByName(String name) {
        return List.of(); // Implementation for name search
    }
}
