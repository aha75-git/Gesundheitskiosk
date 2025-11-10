package de.aha.backend.repository;

import de.aha.backend.model.appointment.Appointment;
import de.aha.backend.model.appointment.AppointmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByPatientIdOrderByScheduledAtDesc(String patientId);

    List<Appointment> findByAdvisorIdOrderByScheduledAtDesc(String advisorId);

    List<Appointment> findByPatientIdAndScheduledAtBetweenOrderByScheduledAt(
            String patientId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByAdvisorIdAndScheduledAtBetweenOrderByScheduledAt(
            String advisorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByPatientIdAndStatusOrderByScheduledAt(
            String patientId, AppointmentStatus status);

    List<Appointment> findByAdvisorIdAndStatusOrderByScheduledAt(
            String advisorId, AppointmentStatus status);

    @Query("{ 'advisorId': ?0, 'scheduledAt': { $gte: ?1, $lt: ?2 }, 'status': { $in: ['SCHEDULED', 'CONFIRMED', 'IN_PROGRESS'] } }")
    List<Appointment> findBookedAppointmentsForAdvisorInTimeRange(
            String advisorId, LocalDateTime start, LocalDateTime end);

    Optional<Appointment> findByIdAndPatientId(String id, String patientId);

    Optional<Appointment> findByIdAndAdvisorId(String id, String advisorId);

    boolean existsByAdvisorIdAndScheduledAtBetweenAndStatusIn(
            String advisorId,
            LocalDateTime start,
            LocalDateTime end,
            List<AppointmentStatus> statuses);
}
