// contexts/ChatContext.tsx
import React, { createContext, /*useContext,*/ useReducer, useEffect } from 'react';
import type { /*ChatMessage,*/ ChatSession} from '../types/chat/chat.ts';
import { chatService } from '../services/chatService';

interface ChatState {
    sessions: ChatSession[];
    unreadCount: number;
    isLoading: boolean;
}

type ChatAction =
    | { type: 'SET_SESSIONS'; payload: ChatSession[] }
    | { type: 'SET_LOADING'; payload: boolean }
    | { type: 'UPDATE_SESSION'; payload: ChatSession }
    | { type: 'INCREMENT_UNREAD' }
    | { type: 'RESET_UNREAD' };

const ChatContext = createContext<{
    state: ChatState;
    dispatch: React.Dispatch<ChatAction>;
    refreshSessions: () => Promise<void>;
} | undefined>(undefined);

export const ChatProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [state, dispatch] = useReducer(chatReducer, {
        sessions: [],
        unreadCount: 0,
        isLoading: false
    });

    const refreshSessions = async () => {
        dispatch({ type: 'SET_LOADING', payload: true });
        try {
            const sessions = await chatService.getChatSessions();
            dispatch({ type: 'SET_SESSIONS', payload: sessions });

            const totalUnread = sessions.reduce((sum, session) => sum + session.unreadCount, 0);
            if (totalUnread !== state.unreadCount) {
                // Unread Count würde hier aktualisiert
            }
        } catch (error) {
            console.error('Error refreshing sessions:', error);
        } finally {
            dispatch({ type: 'SET_LOADING', payload: false });
        }
    };

    useEffect(() => {
        refreshSessions();
    }, []);

    return (
        <ChatContext.Provider value={{ state, dispatch, refreshSessions }}>
            {children}
        </ChatContext.Provider>
    );
};

const chatReducer = (state: ChatState, action: ChatAction): ChatState => {
    switch (action.type) {
        case 'SET_SESSIONS':
            return { ...state, sessions: action.payload };
        case 'SET_LOADING':
            return { ...state, isLoading: action.payload };
        case 'UPDATE_SESSION':
            { const updatedSessions = state.sessions.map(session =>
                session.id === action.payload.id ? action.payload : session
            );
            return { ...state, sessions: updatedSessions }; }
        default:
            return state;
    }
};

// TODO auskommentieren, wenn verwendet wird. 
//  Es wird als Fehler wegen unbenutzt angezeigt.
// export const useChat = () => {
//     const context = useContext(ChatContext);
//     if (context === undefined) {
//         throw new Error('useChat must be used within a ChatProvider');
//     }
//     return context;
// };