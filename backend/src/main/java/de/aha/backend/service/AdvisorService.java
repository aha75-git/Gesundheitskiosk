package de.aha.backend.service;

import de.aha.backend.exception.NotFoundObjectException;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.repository.AdvisorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static de.aha.backend.mapper.UserMapper.mapToUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvisorService {
    private final AdvisorRepository advisorRepository;

    /**
     * Creates a new advisor.
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

    public Advisor getAdvisorByUser(String userId) {
        log.info("getAdvisorByRole with userId: {}", userId);
        return advisorRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundObjectException("Advisor not found with user id: " + userId));
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

    public List<Advisor> getOnlineAdvisors() {
        log.info("getOnlineAdvisors");
        return advisorRepository.findOnlineAdvisors();
    }

    public Advisor updateAdvisorOnlineStatus(String advisorId, boolean online) {
        log.info("updateAdvisorOnlineStatus: advisorId: {} ; online: {}", advisorId, online);

        Optional<Advisor> advisorOpt = advisorRepository.findById(advisorId);
        if (advisorOpt.isPresent()) {
            Advisor advisor = advisorOpt.get();
            advisor.setOnline(online);
            advisor.setLastSeen(LocalDateTime.now());
            return advisorRepository.save(advisor);
        }
        throw new RuntimeException("Advisor not found with id: " + advisorId);
    }

    public Advisor updateAdvisorAvailability(String advisorId, boolean available) {
        log.info("updateAdvisorAvailability: advisorId: {} ; available: {}", advisorId, available);

        Optional<Advisor> advisorOpt = advisorRepository.findById(advisorId);
        if (advisorOpt.isPresent()) {
            Advisor advisor = advisorOpt.get();
            advisor.setAvailable(available);
            return advisorRepository.save(advisor);
        }
        throw new RuntimeException("Advisor not found with id: " + advisorId);
    }

    public List<Advisor> searchAdvisors(String specialization, List<String> languages) {
        log.info("searchAdvisors: specialization: {} ; languages: {}", specialization, languages);

        if (specialization != null && languages != null && !languages.isEmpty()) {
            // Kombinierte Suche implementieren
            return advisorRepository.findBySpecializationContainingIgnoreCase(specialization);
        } else if (specialization != null) {
            return advisorRepository.findBySpecializationContainingIgnoreCase(specialization);
        } else if (languages != null && !languages.isEmpty()) {
            return advisorRepository.findByLanguages(languages);
        } else {
            return getAllAdvisors();
        }
    }
}
