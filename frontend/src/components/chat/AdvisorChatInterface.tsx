import React, { useState, useEffect, useRef } from 'react';
import type {ChatMessage, Advisor, ChatSession} from '../../types/chat/chat.ts';
import { chatService } from '../../services/chatService';
import MessageList from './MessageList';
import MessageInput from './MessageInput';
import './AdvisorChatInterface.css';

interface AdvisorChatInterfaceProps {
    advisor: Advisor;
    session: ChatSession;
    onBack: () => void;
}

const AdvisorChatInterface: React.FC<AdvisorChatInterfaceProps> = ({
                                                                       advisor,
                                                                       session,
                                                                       onBack
                                                                   }) => {
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [isSending, setIsSending] = useState(false);
    // const [isTyping, setIsTyping] = useState(false);
    const messagesEndRef = useRef<HTMLDivElement>(null);

    console.log("Session: " + session?.id);

    useEffect(() => {
        loadChatHistory();
        setupRealtimeUpdates();
    }, [advisor.id]);

    const loadChatHistory = async () => {
        try {
            setIsLoading(true);
            const history = await chatService.getChatHistory(advisor.id);
            setMessages(history);

            // Markiere Nachrichten als gelesen
            const unreadMessageIds = history
                .filter(msg => msg.senderType === 'advisor' && !msg.read)
                .map(msg => msg.id);

            if (unreadMessageIds.length > 0) {
                await chatService.markMessagesAsRead(advisor.id, unreadMessageIds);
            }
        } catch (error) {
            console.error('Error loading chat history:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const setupRealtimeUpdates = () => {
        // Simuliere eingehende Nachrichten
        const cleanup = chatService.simulateRealtimeMessages(advisor.id, (newMessage) => {
            setMessages(prev => [...prev, newMessage]);
        });

        return cleanup;
    };

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const handleSendMessage = async (content: string) => {
        if (!content.trim()) return;

        setIsSending(true);

        try {
            const newMessage = await chatService.sendMessage(advisor.id, content);
            setMessages(prev => [...prev, newMessage]);
        } catch (error) {
            console.error('Error sending message:', error);
            // Fehlerbehandlung - könnte einen Toast oder Alert anzeigen
        } finally {
            setIsSending(false);
        }
    };

    return (
        <div className="advisor-chat-interface">
            {/* Chat Header */}
            <div className="chat-header">
                <button onClick={onBack} className="back-button">
                    <i className="fas fa-arrow-left"></i>
                </button>

                <div className="advisor-info">
                    <div className="advisor-avatar">
                        {advisor.image ? (
                            <img src={advisor.image} alt={advisor.name} />
                        ) : (
                            <i className="fas fa-user-md"></i>
                        )}
                    </div>
                    <div className="advisor-details">
                        <h3>{advisor.name}</h3>
                        <p className="specialization">{advisor.specialization}</p>
                        <div className="advisor-status">
                            <span className={`status-dot ${advisor.isOnline ? 'online' : 'offline'}`}></span>
                            <span>{advisor.isOnline ? 'Online' : 'Offline'}</span>
                            <span className="response-time">• {advisor.responseTime}</span>
                        </div>
                    </div>
                </div>

                <div className="chat-actions">
                    <button className="action-button">
                        <i className="fas fa-phone"></i>
                    </button>
                    <button className="action-button">
                        <i className="fas fa-video"></i>
                    </button>
                    <button className="action-button">
                        <i className="fas fa-ellipsis-v"></i>
                    </button>
                </div>
            </div>

            {/* Message List */}
            <div className="message-list-container">
                <MessageList
                    messages={messages}
                    isLoading={isLoading}
                    currentUserId="user-1" // In echter App aus Auth Context
                />
                <div ref={messagesEndRef} />
            </div>

            {/* Message Input */}
            <div className="message-input-container">
                <MessageInput
                    onSendMessage={handleSendMessage}
                    disabled={isSending || !advisor.isOnline}
                    placeholder={
                        advisor.isOnline
                            ? "Schreiben Sie eine Nachricht..."
                            : "Berater ist derzeit offline"
                    }
                />
            </div>
        </div>
    );
};

export default AdvisorChatInterface;