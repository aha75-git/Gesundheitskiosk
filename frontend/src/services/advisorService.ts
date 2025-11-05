import axios from 'axios';
import type {Advisor, SearchFilters, AdvisorSearchResponse, Review} from '../types/advisor/AdvisorTypes.ts';

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

export const advisorService = {
    // Alle Berater abrufen
    getAllAdvisors: async (page: number = 0, size: number = 12): Promise<AdvisorSearchResponse> => {
        const response = await api.get('/advisors', {
            params: { page, size }
        });
        return response.data;
    },

    // Berater nach ID abrufen
    getAdvisorById: async (advisorId: string): Promise<Advisor> => {
        const response = await api.get(`/advisors/${advisorId}`);
        return response.data;
    },

    // Berater suchen mit Filtern
    searchAdvisors: async (
        filters: Partial<SearchFilters>,
        page: number = 0,
        size: number = 12
    ): Promise<AdvisorSearchResponse> => {
        const params = {
            ...filters,
            page,
            size
        };

        const response = await api.get('/searchadvisors/search', { params });
        return response.data;
    },

    // Verfügbare Spezialisierungen abrufen
    getSpecializations: async (): Promise<string[]> => {
        const response = await api.get('/searchadvisors/specializations');
        return response.data;
    },

    // Verfügbare Sprachen abrufen
    getLanguages: async (): Promise<string[]> => {
        const response = await api.get('/searchadvisors/languages');
        return response.data;
    },

    // Bewertung für Berater abgeben
    addReview: async (advisorId: string, review: { rating: number; comment: string }): Promise<Review> => {
        const response = await api.post(`/reviews/${advisorId}`, review);
        return response.data;
    }
};