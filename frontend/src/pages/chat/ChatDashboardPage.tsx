import React, { useState, useEffect } from 'react';
import { useAuth } from '../../api/AuthContext.tsx';
import AdvisorChatInterface from '../../components/chat/AdvisorChatInterface.tsx';
import { chatService } from '../../services/chatService.ts';
import type {ChatSession, Advisor} from '../../types/chat/chat.ts';
import ChatSessionList from '../../components/chat/ChatSessionList.tsx';
import AdvisorList from '../../components/chat/AdvisorList.tsx';
import './ChatDashboardPage.css';
import {useNavigate} from "react-router-dom";

const ChatDashboardPage: React.FC = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [activeView, setActiveView] = useState<'sessions' | 'advisors'>('sessions');
    const [sessions, setSessions] = useState<ChatSession[]>([]);
    const [advisors, setAdvisors] = useState<Advisor[]>([]);
    const [selectedAdvisor, setSelectedAdvisor] = useState<Advisor | null>(null);
    const [selectedSession, setSelectedSession] = useState<ChatSession | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if (!user) {
            navigate('/login');
            return;
        }
        loadInitialData();
    }, [user, navigate]);

    // useEffect(() => {
    //     loadInitialData();
    // }, []);

    const loadInitialData = async () => {
        try {
            setIsLoading(true);
            const [sessionsData, advisorsData] = await Promise.all([
                chatService.getChatSessions(),
                chatService.getAvailableAdvisors()
            ]);

            setSessions(sessionsData);
            setAdvisors(advisorsData);
        } catch (error) {
            console.error('Error loading chat data:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSelectAdvisor = (advisor: Advisor) => {
        setSelectedAdvisor(advisor);

        // Prüfen ob bereits eine Session existiert
        const existingSession = sessions.find(s => s.advisorId === advisor.id);
        if (existingSession) {
            setSelectedSession(existingSession);
        } else {
            // Neue Session erstellen
            const newSession: ChatSession = {
                id: `session-${advisor.id}`,
                advisorId: advisor.id,
                advisorName: advisor.name,
                lastMessage: 'Neue Konversation gestartet',
                lastMessageTime: new Date(),
                unreadCount: 0,
                isOnline: advisor.isOnline
            };
            setSelectedSession(newSession);
        }
    };

    const handleBackToSessions = () => {
        setSelectedAdvisor(null);
        setSelectedSession(null);
        loadInitialData(); // Daten aktualisieren
    };

    if (isLoading) {
        return (
            <div className="chat-dashboard loading">
                <div className="loading-container">
                    <i className="fas fa-comments fa-spin"></i>
                    <p>Lade Nachrichten...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="chat-dashboard">
            <div className="chat-container">
                {/* Sidebar */}
                <div className="chat-sidebar">
                    <div className="sidebar-header">
                        <h1>Nachrichten</h1>
                        <div className="view-tabs">
                            <button
                                className={`tab ${activeView === 'sessions' ? 'active' : ''}`}
                                onClick={() => setActiveView('sessions')}
                            >
                                Chats
                            </button>
                            <button
                                className={`tab ${activeView === 'advisors' ? 'active' : ''}`}
                                onClick={() => setActiveView('advisors')}
                            >
                                Berater
                            </button>
                        </div>
                    </div>

                    <div className="sidebar-content">
                        {activeView === 'sessions' ? (
                            <ChatSessionList
                                sessions={sessions}
                                onSelectSession={(session) => {
                                    const advisor = advisors.find(a => a.id === session.advisorId);
                                    if (advisor) {
                                        setSelectedAdvisor(advisor);
                                        setSelectedSession(session);
                                    }
                                }}
                            />
                        ) : (
                            <AdvisorList
                                advisors={advisors}
                                onSelectAdvisor={handleSelectAdvisor}
                            />
                        )}
                    </div>
                </div>

                {/* Main Chat Area */}
                <div className="chat-main">
                    {selectedAdvisor && selectedSession ? (
                        <AdvisorChatInterface
                            advisor={selectedAdvisor}
                            session={selectedSession}
                            onBack={handleBackToSessions}
                        />
                    ) : (
                        <div className="chat-welcome">
                            <div className="welcome-content">
                                <i className="fas fa-comments"></i>
                                <h2>Willkommen im Chat</h2>
                                <p>
                                    {activeView === 'sessions'
                                        ? 'Wählen Sie eine Konversation aus oder starten Sie einen neuen Chat mit einem Berater.'
                                        : 'Wählen Sie einen Berater für die Kommunikation aus.'
                                    }
                                </p>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ChatDashboardPage;