export interface User {
    id: string;
    username: string;
    email: string;
    role: UserRole;
    createdAt: string;
}

export interface AuthResponse {
    token: string;
    type: string;
    user: User;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    email: string;
    password: string;
    role?: UserRole;
}

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
export enum UserRole {
    USER = 'USER',
    ADMIN = 'ADMIN',
    ADVISOR = 'ADVISOR'
}