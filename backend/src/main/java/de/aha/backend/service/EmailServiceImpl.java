package de.aha.backend.service;

import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.appointment.Appointment;
import de.aha.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

// Service für E-Mail-Benachrichtigungen
@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender mailSender = new JavaMailSenderImpl();

    public void sendAppointmentConfirmation(Appointment appointment, Advisor advisor, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Terminbestätigung - Ihre Beratung mit " + advisor.getName());
        message.setText(createConfirmationEmail(appointment, advisor, user));

        mailSender.send(message);
    }

    private String createConfirmationEmail(Appointment appointment, Advisor advisor, User user) {
        return String.format(
                "Sehr geehrte/r %s %s,\n\n" +
                        "vielen Dank für Ihre Terminbuchung!\n\n" +
                        "Terminübersicht:\n" +
                        "Berater: %s\n" +
                        "Datum: %s\n" +
                        "Uhrzeit: %s\n" +
                        "Dauer: %d Minuten\n" +
                        "Art: %s\n\n" +
                        "Mit freundlichen Grüßen\nIhr Beratungsteam",
                user.getProfile().getPersonalData().getFirstName(),
                user.getProfile().getPersonalData().getLastName(),
                advisor.getName(),
                // formatierte Datumsangaben...
                appointment.getScheduledAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                appointment.getScheduledAt().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                appointment.getDurationMinutes(),
                appointment.getType().name()
        );
    }
}
