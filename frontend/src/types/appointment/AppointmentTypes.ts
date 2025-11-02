export type Appointment = {
    id: string;
    patientId: string;
    advisorId: string;
    type: AppointmentType;
    status: AppointmentStatus;
    scheduledAt: Date;
    durationMinutes: number;
    notes: string;
    symptoms: string[];
    priority: Priority;
    createdAt: Date;
    updatedAt: Date;
};

export type CreateAppointmentRequest = {
    advisorId: string;
    type: AppointmentType;
    scheduledAt: Date;
    notes: string;
    symptoms: string[];
    priority: Priority;
};

export type UpdateAppointmentStatusRequest = {
    status: AppointmentStatus;
};

export type AppointmentResponse = {
    id: string;
    patientId: string;
    advisorId: string;
    type: AppointmentType;
    status: AppointmentStatus;
    scheduledAt: Date;
    durationMinutes: number;
    notes: string;
    symptoms: string[];
    priority: Priority;
};

export type AvailabilityResponse = {
    advisorId: string;
    date: Date;
    availableSlots: TimeSlot[];
};

export type TimeSlot = {
    start: Date;
    end: Date;
}

export type AppointmentType =
    | 'VIDEO_CALL'
    | 'PHONE_CALL'
    | 'IN_PERSON'
    | 'CHAT';

export type AppointmentStatus =
    | 'REQUESTED'
    | 'SCHEDULED'
    | 'CONFIRMED'
    | 'IN_PROGRESS'
    | 'COMPLETED'
    | 'CANCELLED'
    | 'NO_SHOW';

export type Priority =
    | 'ROUTINE'
    | 'URGENT'
    | 'EMERGENCY';
