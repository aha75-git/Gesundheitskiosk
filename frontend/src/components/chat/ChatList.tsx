import React from 'react';
import type {Advisor} from '../../types/advisor/AdvisorTypes.ts';
import './ChatList.css';

interface ChatListProps {
    advisors: Advisor[];
    onAdvisorSelect: (advisor: Advisor) => void;
}

const ChatList: React.FC<ChatListProps> = ({ advisors, onAdvisorSelect }) => {
    return (
        <div className="chat-list">
            {advisors.map(advisor => (
                <div
                    key={advisor.id}
                    className="chat-list-item"
                    onClick={() => onAdvisorSelect(advisor)}
                >
                    <div className="advisor-avatar">
                        {advisor.image ? (
                            <img src={advisor.image} alt={advisor.name} />
                        ) : (
                            <i className="fas fa-user-md"></i>
                        )}
                    </div>

                    <div className="advisor-info">
                        <h4>{advisor.name}</h4>
                        <p className="specialization">{advisor.specialization}</p>
                        <div className="advisor-meta">
                            <span className="status online">Online</span>
                            <span className="response-time">Antwortet meist innerhalb von 2 Stunden</span>
                        </div>
                    </div>

                    <div className="chat-indicators">
                        <span className="unread-count">3</span>
                        <i className="fas fa-chevron-right"></i>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default ChatList;