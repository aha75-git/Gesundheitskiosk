// components/ChatSessionList.tsx
import React from 'react';
import type {ChatSession} from '../../types/chat/chat.ts';
import './ChatSessionList.css';

interface ChatSessionListProps {
    sessions: ChatSession[];
    onSelectSession: (session: ChatSession) => void;
}

const ChatSessionList: React.FC<ChatSessionListProps> = ({
                                                             sessions,
                                                             onSelectSession
                                                         }) => {
    const formatTime = (timestamp: Date) => {
        const now = new Date();
        const diffInHours = (now.getTime() - timestamp.getTime()) / (1000 * 60 * 60);

        if (diffInHours < 24) {
            return timestamp.toLocaleTimeString('de-DE', {
                hour: '2-digit',
                minute: '2-digit'
            });
        } else {
            return timestamp.toLocaleDateString('de-DE', {
                day: '2-digit',
                month: '2-digit'
            });
        }
    };

    if (sessions.length === 0) {
        return (
            <div className="chat-session-list empty">
                <div className="empty-sessions">
                    <i className="fas fa-comments"></i>
                    <p>Keine aktiven Chats</p>
                    <span>Beginnen Sie eine neue Konversation mit einem Berater</span>
                </div>
            </div>
        );
    }

    return (
        <div className="chat-session-list">
            {sessions.map((session) => (
                <div
                    key={session.id}
                    className={`session-item ${session.unreadCount > 0 ? 'unread' : ''}`}
                    onClick={() => onSelectSession(session)}
                >
                    <div className="session-avatar">
                        <i className="fas fa-user-md"></i>
                        {session.isOnline && <div className="online-indicator"></div>}
                    </div>

                    <div className="session-content">
                        <div className="session-header">
                            <h4>{session.advisorName}</h4>
                            <span className="session-time">
                {formatTime(session.lastMessageTime)}
              </span>
                        </div>

                        <p className="session-preview">
                            {session.lastMessage}
                        </p>
                    </div>

                    {session.unreadCount > 0 && (
                        <div className="unread-badge">
                            {session.unreadCount}
                        </div>
                    )}
                </div>
            ))}
        </div>
    );
};

export default ChatSessionList;