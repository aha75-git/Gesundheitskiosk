import axios from 'axios';
import type {
    AppointmentResponse,
    CreateAppointmentRequest,
    UpdateAppointmentStatusRequest,
    AvailabilityResponse
} from '../types/appointment/AppointmentTypes.ts';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
    baseURL: API_BASE_URL,
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const appointmentService = {
    // Termine des aktuellen Benutzers abrufen
    getMyAppointments: async (date?: string): Promise<AppointmentResponse[]> => {
        const params = date ? { date } : {};
        const response = await api.get('/appointments', { params });
        return response.data;
    },

    // Einzelnen Termin abrufen
    getAppointment: async (appointmentId: string): Promise<AppointmentResponse> => {
        const response = await api.get(`/appointments/${appointmentId}`);
        return response.data;
    },

    // Neuen Termin erstellen
    createAppointment: async (request: CreateAppointmentRequest): Promise<AppointmentResponse> => {
        const response = await api.post('/appointments', request);
        return response.data;
    },

    // Terminstatus aktualisieren
    updateAppointmentStatus: async (
        appointmentId: string,
        request: UpdateAppointmentStatusRequest
    ): Promise<AppointmentResponse> => {
        const response = await api.put(`/appointments/${appointmentId}/status`, request);
        return response.data;
    },

    // Verfügbarkeit prüfen
    checkAvailability: async (advisorId: string, date: string): Promise<AvailabilityResponse> => {
        const response = await api.get(`/appointments/availability/${advisorId}`, {
            params: { date }
        });
        return response.data;
    },

    // Termin löschen
    cancelAppointment: async (appointmentId: string): Promise<void> => {
        await api.delete(`/appointments/${appointmentId}`);
    }
};