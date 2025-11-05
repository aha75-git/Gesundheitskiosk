export interface Advisor {
    id: string;
    name: string;
    specialization: string;
    rating: number;
    languages: string[];
    image?: string;
    email: string;
    phone: string;
    bio: string;
    qualifications: string[];
    experience: number; // years of experience
    consultationFee: number;
    available: boolean;
    reviews: Review[];
}

export interface Review {
    id: string;
    patientName: string;
    rating: number;
    comment: string;
    date: Date;
}

export interface SearchFilters {
    searchQuery: string;
    specialization: string;
    language: string;
    minRating: number;
    maxFee: number;
    availableToday: boolean;
}

export interface AdvisorSearchResponse {
    advisors: Advisor[];
    totalCount: number;
    currentPage: number;
    totalPages: number;
}