import React from 'react';
import type {ChatMessage} from '../../types/chat/chat.ts';
import './MessageList.css';

interface MessageListProps {
    messages: ChatMessage[];
    isLoading: boolean;
    currentUserId: string;
}

const MessageList: React.FC<MessageListProps> = ({
                                                     messages,
                                                     isLoading,
                                                     currentUserId
                                                 }) => {
    const isCurrentUser = (message: ChatMessage) => message.senderId === currentUserId;

    const formatTime = (timestamp: Date) => {
        return timestamp.toLocaleTimeString('de-DE', {
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    if (isLoading) {
        return (
            <div className="message-list loading">
                <div className="loading-messages">
                    <i className="fas fa-spinner fa-spin"></i>
                    <p>Lade Nachrichten...</p>
                </div>
            </div>
        );
    }

    if (messages.length === 0) {
        return (
            <div className="message-list empty">
                <div className="empty-state">
                    <i className="fas fa-comments"></i>
                    <h3>Noch keine Nachrichten</h3>
                    <p>Beginnen Sie die Konversation mit einer Nachricht</p>
                </div>
            </div>
        );
    }

    return (
        <div className="message-list">
            {messages.map((message) => (
                <div
                    key={message.id}
                    className={`message ${isCurrentUser(message) ? 'outgoing' : 'incoming'} ${message.type}`}
                >
                    <div className="message-content">
                        {message.type === 'system' ? (
                            <div className="system-message">
                                <i className="fas fa-info-circle"></i>
                                {message.content}
                            </div>
                        ) : (
                            <>
                                <div className="message-bubble">
                                    <p>{message.content}</p>
                                </div>
                                <div className="message-meta">
                                    <span className="message-time">{formatTime(message.timestamp)}</span>
                                    {isCurrentUser(message) && (
                                        <span className={`read-status ${message.read ? 'read' : 'unread'}`}>
                      <i className="fas fa-check"></i>
                    </span>
                                    )}
                                </div>
                            </>
                        )}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default MessageList;