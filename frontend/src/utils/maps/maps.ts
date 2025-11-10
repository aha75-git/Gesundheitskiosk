import type {Appointment, AppointmentResponse} from "../../types/appointment/AppointmentTypes.ts";

export const mapAppointmentResponseToAppointment = (appointmentResponse: AppointmentResponse) => {
    const appointment: Appointment = {
        id: appointmentResponse.id,
        patientId: appointmentResponse.patientId,
        advisorId: appointmentResponse.advisorId,
        type: appointmentResponse.type,
        status: appointmentResponse.status,
        scheduledAt: appointmentResponse.scheduledAt,
        durationMinutes: appointmentResponse.durationMinutes,
        notes: appointmentResponse.notes,
        symptoms: appointmentResponse.symptoms,
        priority: appointmentResponse.priority,
        createdAt: new Date(),
        updatedAt: new Date(),
    }
    return appointment;
};