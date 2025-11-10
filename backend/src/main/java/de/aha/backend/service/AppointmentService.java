package de.aha.backend.service;

import de.aha.backend.dto.appointment.*;
import de.aha.backend.exception.NotFoundObjectException;
import de.aha.backend.mapper.AppointmentMapper;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.appointment.Appointment;
import de.aha.backend.model.appointment.AppointmentStatus;
import de.aha.backend.model.appointment.TimeSlot;
import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.model.user.User;
import de.aha.backend.model.user.UserRole;
import de.aha.backend.repository.AdvisorRepository;
import de.aha.backend.repository.AppointmentRepository;
import de.aha.backend.repository.UserRepository;
import de.aha.backend.security.TokenInteract;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.aha.backend.mapper.AppointmentMapper.mapToAppointmentResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final TokenInteract tokenInteract;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final AdvisorRepository advisorRepository;
    private static final int DEFAULT_APPOINTMENT_DURATION = 60; // minutes

    @Transactional
    public AppointmentResponse createAppointment(@Valid CreateAppointmentRequest request, String patientId) {

        log.info("Creating appointment for patient: {} with advisor: {}", patientId, request.advisorId());

        // Validate advisor exists
        Advisor advisor = advisorRepository.findById(request.advisorId())
                .orElseThrow(() -> new NotFoundObjectException("Advisor not found with id: " + request.advisorId()));

        // Check if the requested time slot is available
        if (!isTimeSlotAvailable(request.advisorId(), request.scheduledAt(), DEFAULT_APPOINTMENT_DURATION)) {
            throw new NotFoundObjectException("Requested time slot is not available");
        }

        // Create appointment
        Appointment appointment = Appointment.builder()
                .patientId(patientId)
                .advisorId(request.advisorId())
                .type(request.type())
                .status(AppointmentStatus.REQUESTED)
                .scheduledAt(request.scheduledAt())
                .durationMinutes(DEFAULT_APPOINTMENT_DURATION)
                .notes(request.notes())
                .symptoms(request.symptoms())
                .priority(request.priority())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment created successfully with id: {}", savedAppointment.getId());

        return mapToAppointmentResponse(savedAppointment);
    }

    public AppointmentResponse getAppointment(String appointmentId, String userId) {
        log.info("Fetching appointment: {} for user: {}", appointmentId, userId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundObjectException("Appointment not found with id: " + appointmentId));

        // Check if user has access to this appointment
        if (!appointment.getPatientId().equals(userId) && !appointment.getAdvisorId().equals(userId)) {
            throw new RuntimeException("Access denied to appointment");
        }

        return mapToAppointmentResponse(appointment);
    }

    public List<AppointmentResponse> getAppointments(LocalDate date, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundObjectException("User not found with id: " + userId));
        if (user.getRole() == UserRole.ADVISOR) {
            return this.getAdvisorAppointments(date, userId);
        }
        return this.getUserAppointments(date, userId);
    }

    public List<AppointmentResponse> getUserAppointments(LocalDate date, String userId) {
        log.info("Fetching appointments for user: {} on date: {}", userId, date);

        List<Appointment> appointments;

        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            // Get both patient and advisor appointments for the user
            List<Appointment> patientAppointments = appointmentRepository
                    .findByPatientIdAndScheduledAtBetweenOrderByScheduledAt(userId, startOfDay, endOfDay);
            List<Appointment> advisorAppointments = appointmentRepository
                    .findByAdvisorIdAndScheduledAtBetweenOrderByScheduledAt(userId, startOfDay, endOfDay);

            appointments = new ArrayList<>();
            appointments.addAll(patientAppointments);
            appointments.addAll(advisorAppointments);
        } else {
            // Get all appointments for the user
            List<Appointment> patientAppointments = appointmentRepository
                    .findByPatientIdOrderByScheduledAtDesc(userId);
            List<Appointment> advisorAppointments = appointmentRepository
                    .findByAdvisorIdOrderByScheduledAtDesc(userId);

            appointments = new ArrayList<>();
            appointments.addAll(patientAppointments);
            appointments.addAll(advisorAppointments);
        }

        return appointments.stream()
                .map(AppointmentMapper::mapToAppointmentResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAdvisorAppointments(LocalDate date, String userId) {
        log.info("Fetching appointments for advisor: {} on date: {}", userId, date);

        Advisor advisor = advisorRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundObjectException("Advisor not found with user id: " + userId));

        List<Appointment> appointments;

        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            // Get advisor appointments for the user
            appointments = appointmentRepository
                    .findByAdvisorIdAndScheduledAtBetweenOrderByScheduledAt(advisor.getId(), startOfDay, endOfDay);
        } else {
            // Get all appointments for the advisor
            appointments = appointmentRepository
                    .findByAdvisorIdOrderByScheduledAtDesc(advisor.getId());
        }

        return appointments.stream()
                .map(AppointmentMapper::mapToAppointmentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(String appointmentId, @Valid UpdateAppointmentStatusRequest request, String userId) {

        log.info("Updating appointment status: {} to {} by user: {}",
                appointmentId, request.status(), userId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundObjectException("Appointment not found with id: " + appointmentId));

        // Check if user has permission to update status
        if (!appointment.getAdvisorId().equals(userId) && !appointment.getPatientId().equals(userId)) {
            throw new RuntimeException("Access denied to update appointment status");
        }

        // Validate status transition
        validateStatusTransition(appointment.getStatus(), request.status(), userId, appointment);

        appointment.setStatus(request.status());
        appointment.setModifyDate(LocalDateTime.now());

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment status updated successfully");

        return mapToAppointmentResponse(updatedAppointment);
    }


    public AvailabilityResponse checkAvailability(String advisorId, LocalDate date) {
        log.info("Checking availability for advisor: {} on date: {}", advisorId, date);

        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new NotFoundObjectException("Advisor not found with id: " + advisorId));

        var advisorWorkingHoursOptional = this.getAdvisorWorkingHoursOptional(advisor, date);
        List<TimeSlot> availableSlots = calculateAvailableSlots(advisor, date, advisorWorkingHoursOptional);
        var workingHours = advisorWorkingHoursOptional.orElse(
                WorkingHours.builder()
                .start("")
                .end("")
                .build()
        );

        return AvailabilityResponse.builder()
                .advisorId(advisorId)
                .date(date)
                .availableSlots(availableSlots)
                .workingHours(WorkingHoursDTO.builder()
                        .start(workingHours.getStart())
                        .end(workingHours.getEnd())
                        .build())
                .build();
    }

    @Transactional
    public void cancelAppointment(String appointmentId, String userId) {
        log.info("Cancelling appointment: {} by user: {}", appointmentId, userId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundObjectException("Appointment not found with id: " + appointmentId));

        // Check if user has permission to cancel
        if (!appointment.getPatientId().equals(userId) && !appointment.getAdvisorId().equals(userId)) {
            throw new RuntimeException("Access denied to cancel appointment");
        }

        // Only allow cancellation for certain statuses
        if (appointment.getStatus() == AppointmentStatus.COMPLETED ||
                appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel appointment with status: " + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setModifyDate(LocalDateTime.now());

        appointmentRepository.save(appointment);
        log.info("Appointment cancelled successfully");
    }

    private boolean isTimeSlotAvailable(String advisorId, LocalDateTime startTime, int durationMinutes) {
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);

        // Check if there are any overlapping appointments
        return !appointmentRepository.existsByAdvisorIdAndScheduledAtBetweenAndStatusIn(
                advisorId,
                startTime,
                endTime,
                List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED, AppointmentStatus.IN_PROGRESS)
        );
    }

    private Optional<WorkingHours> getAdvisorWorkingHoursOptional(Advisor advisor, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return advisor.getWorkingHours().stream()
                .filter(wh -> wh.getDayOfWeek().name().equals(dayOfWeek.name()))
                .findFirst();
    }

    private List<TimeSlot> calculateAvailableSlots(Advisor advisor, LocalDate date, Optional<WorkingHours> advisorWorkingHoursOptional) {
        List<TimeSlot> availableSlots = new ArrayList<>();

        // Get advisor's working hours for the specific day
//        var advisorWorkingHoursOptional = this.getAdvisorWorkingHoursOptional(advisor, date);

        if (advisorWorkingHoursOptional.isEmpty() || !advisorWorkingHoursOptional.get().isAvailable()) {
            return availableSlots; // No working hours for this day
        }

        WorkingHours workingHours = advisorWorkingHoursOptional.get();
        LocalTime startTime = LocalTime.parse(workingHours.getStart());
        LocalTime endTime = LocalTime.parse(workingHours.getEnd());

        // Get booked appointments for the day
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(LocalTime.MAX);
        List<Appointment> bookedAppointments = appointmentRepository
                .findBookedAppointmentsForAdvisorInTimeRange(advisor.getId(), dayStart, dayEnd);

        // Generate time slots (every 60 minutes)
        LocalDateTime currentSlotStart = date.atTime(startTime);
        LocalDateTime dayEndTime = date.atTime(endTime);

        while (currentSlotStart.plusMinutes(DEFAULT_APPOINTMENT_DURATION).isBefore(dayEndTime.plusSeconds(1))) {
            LocalDateTime currentSlotEnd = currentSlotStart.plusMinutes(DEFAULT_APPOINTMENT_DURATION);

            // Check if this slot is available (no overlapping appointments)
            LocalDateTime finalCurrentSlotStart = currentSlotStart;
            boolean isAvailable = bookedAppointments.stream()
                    .noneMatch(appointment -> {
                        LocalDateTime appointmentStart = appointment.getScheduledAt();
                        LocalDateTime appointmentEnd = appointmentStart.plusMinutes(appointment.getDurationMinutes());
                        return finalCurrentSlotStart.isBefore(appointmentEnd) && currentSlotEnd.isAfter(appointmentStart);
                    });

            if (isAvailable) {
                availableSlots.add(TimeSlot.builder()
                                .start(currentSlotStart)
                                .end(currentSlotEnd)
                                .available(true)
                        .build());
            }

            currentSlotStart = currentSlotStart.plusMinutes(DEFAULT_APPOINTMENT_DURATION);
        }

        return availableSlots;
    }

    private void validateStatusTransition(
            AppointmentStatus currentStatus,
            AppointmentStatus newStatus,
            String userId,
            Appointment appointment) {

        // Simple validation - in production you might want more complex rules
        if (newStatus == AppointmentStatus.CANCELLED) {
            if (currentStatus == AppointmentStatus.COMPLETED || currentStatus == AppointmentStatus.CANCELLED) {
                throw new RuntimeException("Cannot cancel appointment with status: " + currentStatus);
            }
        }

        // TODO
        // Add more validation rules as needed
    }
}
