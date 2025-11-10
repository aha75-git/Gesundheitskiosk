import axios from 'axios';
import type {
    AppointmentResponse,
    UpdateAppointmentStatusRequest,
    AdvisorAvailabilityResponse,
    AppointmentBookingData, AppointmentType
} from '../types/appointment/AppointmentTypes.ts';
import type {Advisor} from "../types/advisor/AdvisorTypes.ts";

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
    createAppointment: async (request: AppointmentBookingData): Promise<AppointmentResponse> => {
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
    getAdvisorAvailability: async (advisorId: string, date: string): Promise<AdvisorAvailabilityResponse> => {
        const response = await api.get(`/appointments/availability/${advisorId}`, {
            params: { date }
        });
        return response.data;
    },


    // ###########################

    getAppointmentById: async (appointmentId: string): Promise<AppointmentResponse> => {
        const response = await api.get(`/appointments/${appointmentId}`);
        return response.data;
    },

    getAdvisorById: async (advisorId: string): Promise<Advisor> => {
        const response = await api.get(`/advisors/${advisorId}`);
        return response.data;
    },

    // getMyAppointments -> macht es bereits
    //
    // getUserAppointments: async (): Promise<AppointmentResponse[]> => {
    //     const response = await api.get('/appointments/my-appointments');
    //     return response.data;
    // },

    // ###########################


    // Termin löschen
    cancelAppointment: async (appointmentId: string): Promise<void> => {
        await api.delete(`/appointments/${appointmentId}`);
    },

    // Terminvorlagen für verschiedene Typen
    getAppointmentTypes: () => [
        {
            type: 'VIDEO_CALL' as const,
            name: 'Video-Call',
            description: 'Online-Beratung per Video',
            duration: 60,
            icon: 'fas fa-video'
        },
        {
            type: 'PHONE_CALL' as const,
            name: 'Telefonat',
            description: 'Beratung per Telefon',
            duration: 30,
            icon: 'fas fa-phone'
        },
        {
            type: 'IN_PERSON' as const,
            name: 'Persönlicher Termin',
            description: 'Treffen vor Ort',
            duration: 60,
            icon: 'fas fa-user'
        },
        {
            type: 'CHAT' as const,
            name: 'Chat-Beratung',
            description: 'Schriftliche Beratung',
            duration: 45,
            icon: 'fas fa-comments'
        }
    ],

    // TODO
    // getAppointmentType: (type: AppointmentType): AppointmentType | undefined => {
    //     return appointmentService.getAppointmentTypes().find(t => t.type === type);
    // },

    // Symptome-Vorschläge
    getCommonSymptoms: () => [
        'Stress und Burnout',
        'Angstzustände',
        'Depressive Verstimmungen',
        'Beziehungsprobleme',
        'Berufliche Orientierung',
        'Familienkonflikte',
        'Selbstwertprobleme',
        'Schlafstörungen',
        'Konzentrationsschwierigkeiten',
        'Lebenskrisen'
    ]
};