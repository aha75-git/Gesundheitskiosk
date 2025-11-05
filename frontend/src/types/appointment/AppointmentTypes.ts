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

// export type CreateAppointmentRequest = {
//     advisorId: string;
//     type: AppointmentType;
//     scheduledAt: Date;
//     duration: number; // in minutes
//     notes: string;
//     symptoms: string[];
//     priority: Priority;
// };

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

export type AdvisorAvailabilityResponse = {
    advisorId: string;
    date: Date;
    availableSlots: TimeSlot[];
    workingHours: {
        start: string;
        end: string;
    };
};

export type TimeSlot = {
    start: Date;
    end: Date;
    available: boolean;
}

export type AppointmentBookingData = {
    advisorId: string;
    type: AppointmentType;
    scheduledAt: string; // ISO string
    duration: number; // in minutes
    notes: string;
    symptoms: string[];
    priority: Priority;
}

export type AdvisorAvailability = {
    advisorId: string;
    date: string; // ISO date string
    availableSlots: TimeSlot[];
    workingHours: {
        start: string;
        end: string;
    };
}

export type BookingWizardStep = {
    step: 1 | 2 | 3 | 4;
    title: string;
    description: string;
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
