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

// // eslint-disable-next-line @typescript-eslint/ban-ts-comment
// // @ts-expect-error
// export enum UserRole {
//     USER = 'USER',
//     ADMIN = 'ADMIN',
//     ADVISOR = 'ADVISOR'
// }

export type UserRole = 'USER' | 'ADVISOR' | 'ADMIN';

export interface ProfileRequest {
    username: string;
    email: string;
    personalData: {
        firstName: string;
        lastName: string;
        dateOfBirth: string;
        gender: string;
    };
    medicalInfo: {
        bloodType: string;
        allergies: string[];
        chronicConditions: string[];
        currentMedications: Medication[];
        emergencyContact: {
            name: string;
            phone: string;
            relationship: string;
        };
    };
    contactInfo: {
        phone: string;
        address: {
            street: string;
            city: string;
            postalCode: string;
            country: string;
            houseNumber: string;
        };
        allowHouseVisits: boolean;
    };
    languages: string[];
    specialization: string;
    bio: string;
    qualification: string;
}

export interface ProfileResponse {
    userProfile: UserProfile;
}

export interface UserProfile {
    personalData: PersonalData;
    medicalInfo: MedicalInfo;
    contactInfo: ContactInfo;
    languages: string[];
    specialization?: string;
    bio?: string;
    qualification?: string;
    rating?: number;
}

export interface PersonalData {
    firstName: string;
    lastName: string;
    dateOfBirth: string;
    gender: string;
}

export interface MedicalInfo {
    bloodType: string;
    allergies: string[];
    chronicConditions: string[];
    currentMedications: Medication[];
    emergencyContact: EmergencyContact;
}

export interface ContactInfo {
    phone: string;
    address: Address;
    allowHouseVisits: boolean;
}

export interface Medication {
    name: string;
    dosage: string;
    frequency: string;
}

export interface EmergencyContact {
    name: string;
    phone: string;
    relationship: string;
}

export interface Address {
    street: string;
    city: string;
    postalCode: string;
    country: string;
    houseNumber: string;
}
