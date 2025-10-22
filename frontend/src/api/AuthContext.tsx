import React, { createContext, useContext, useState, useEffect } from 'react';
import type {User, LoginRequest, RegisterRequest, AuthResponse} from '../types/types.ts';
import { authApi } from './api';

interface AuthContextType {
    user: User | null;
    login: (data: LoginRequest) => Promise<void>;
    register: (data: RegisterRequest) => Promise<void>;
    logout: () => void;
    isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const userData = localStorage.getItem('user');

        if (token && userData) {
            setUser(JSON.parse(userData));
        }
        setIsLoading(false);
    }, []);

    const login = async (data: LoginRequest) => {
        const response: AuthResponse = await authApi.login(data);
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify({
            username: response.username,
            email: response.email,
            role: response.role
        }));
        setUser({
            username: response.username,
            email: response.email,
            role: response.role
        });
    };

    const register = async (data: RegisterRequest) => {
        const response: AuthResponse = await authApi.register(data);
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify({
            username: response.username,
            email: response.email,
            role: response.role
        }));
        setUser({
            username: response.username,
            email: response.email,
            role: response.role
        });
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, register, logout, isLoading }}>
            {children}
        </AuthContext.Provider>
    );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};