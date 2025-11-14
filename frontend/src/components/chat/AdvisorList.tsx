import React from 'react';
import type {Advisor} from '../../types/chat/chat.ts';
import './AdvisorList.css';

interface AdvisorListProps {
    advisors: Advisor[];
    onSelectAdvisor: (advisor: Advisor) => void;
}

const AdvisorList: React.FC<AdvisorListProps> = ({ advisors, onSelectAdvisor }) => {
    return (
        <div className="advisor-list">
            {advisors.map((advisor) => (
                <button
                    key={advisor.id}
                    className="advisor-item"
                    onClick={() => onSelectAdvisor(advisor)}
                >
                    <div className="advisor-avatar">
                        {advisor.image ? (
                            <img src={advisor.image} alt={advisor.name} />
                        ) : (
                            <i className="fas fa-user-md"></i>
                        )}
                        {advisor.isOnline && <div className="online-indicator"></div>}
                    </div>

                    <div className="advisor-details">
                        <h4>{advisor.name}</h4>
                        <p className="specialization">{advisor.specialization}</p>
                        <div className="advisor-meta">
              <span className={`status ${advisor.isOnline ? 'online' : 'offline'}`}>
                {advisor.isOnline ? 'Online' : 'Offline'}
              </span>
                            <span className="response-time">{advisor.responseTime}</span>
                        </div>
                        <div className="advisor-rating">
                            <i className="fas fa-star"></i>
                            <span>{advisor.rating.toFixed(1)}</span>
                        </div>
                    </div>

                    <div className="advisor-action">
                        <button className="start-chat-btn">
                            <i className="fas fa-comment"></i>
                        </button>
                    </div>
                </button>
            ))}
        </div>
    );
};

export default AdvisorList;