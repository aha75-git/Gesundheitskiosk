import axios from 'axios';
import type {ProfileRequest, ProfileResponse} from "../types/user/UserTypes.ts";

// const API_BASE_URL = 'http://localhost:8080/api/v1';
const API_BASE_URL = '/api/v1';

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

export const profileService = {
    // Profil laden
    getProfile: async (): Promise<ProfileResponse> => {
        const response = await api.get('/users/profile');
        return response.data;
    },

    // Profil aktualisieren
    updateProfile: async (profileData: ProfileRequest): Promise<ProfileResponse> => {
        const response = await api.post('/users/profile', profileData);
        return response.data;
    },

    // Profil erstellen
    createProfile: async (profileData: ProfileRequest): Promise<ProfileResponse> => {
        const response = await api.post('/users/profile', profileData);
        return response.data;
    }
};