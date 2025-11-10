package de.aha.backend.service;

import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.repository.AdvisorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static de.aha.backend.mapper.UserMapper.mapToUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvisorService {
    private final AdvisorRepository advisorRepository;

    /** Creates a new advisor.
     *
     * @param advisor advisor containing user creation details
     */
    public void save(Advisor advisor) {
        log.info("Creating advisor {}", advisor);
        advisorRepository.save(advisor);
    }

    public List<Advisor> getAllAdvisors() {
        log.info("getAllAdvisors");
        return advisorRepository.findAll();
    }

    public Optional<Advisor> getAdvisorById(String id) {
        log.info("getAdvisorById: {}", id);
        return advisorRepository.findById(id);
    }

    public Optional<Advisor> getAdvisorByUserId(String userId) {
        log.info("getAdvisorByUserId: {}", userId);
        return advisorRepository.findByUserId(userId);
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

    public Advisor updateWorkingHours(String advisorId, List<WorkingHours> workingHours) {
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new RuntimeException("Advisor not found"));

        advisor.setWorkingHours(workingHours);
        return advisorRepository.save(advisor);
    }
}
