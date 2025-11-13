import React from 'react';
import './OnlineStatus.css';

interface OnlineStatusProps {
    isOnline: boolean;
    lastSeen?: Date;
}

const OnlineStatus: React.FC<OnlineStatusProps> = ({ isOnline, lastSeen }) => {
    return (
        <div className="online-status">
            <span className={`status-dot ${isOnline ? 'online' : 'offline'}`}></span>
            <span className="status-text">
        {isOnline ? 'Online' : lastSeen ? `Zuletzt online: ${lastSeen.toLocaleTimeString()}` : 'Offline'}
      </span>
        </div>
    );
};

export default OnlineStatus;