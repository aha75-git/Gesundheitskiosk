package de.aha.backend.model.appointment;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TimeSlot(LocalDateTime start, LocalDateTime end) {}
