// types/chat.ts
export interface ChatMessage {
    id: string;
    content: string;
    senderId: string;
    senderName: string;
    senderType: 'user' | 'advisor';
    timestamp: Date;
    type: 'text' | 'system' | 'audio';
    read: boolean;
    audioUrl?: string;
}

export interface ChatSession {
    id: string;
    advisorId: string;
    advisorName: string;
    advisorImage?: string;
    lastMessage?: string;
    lastMessageTime: Date;
    unreadCount: number;
    isOnline: boolean;
}

// TODO Advisor gibt es schon
export interface Advisor {
    id: string;
    name: string;
    specialization: string;
    image?: string;
    isOnline: boolean;
    responseTime: string;
    rating: number;
}