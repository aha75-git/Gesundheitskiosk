import axios from 'axios';
import type {LoginRequest, RegisterRequest, AuthResponse} from '../types/user/UserTypes.ts';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
    baseURL: API_BASE_URL,
});

// Add token to requests
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const authApi = {
    login: (data: LoginRequest): Promise<AuthResponse> =>
        api.post('/users/login', data).then(response => response.data),

    register: (data: RegisterRequest): Promise<AuthResponse> =>
        api.post('/users/register', data).then(response => response.data),
};

export default api;