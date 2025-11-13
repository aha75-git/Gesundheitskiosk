import React from 'react';
import './OnlineStatus.css';

interface OnlineStatusProps {
    isOnline: boolean;
    lastSeen?: Date;
}

const OnlineStatus: React.FC<OnlineStatusProps> = ({ isOnline, lastSeen }) => {
    let statusMessage;

    if (isOnline) {
        statusMessage = 'Online';
    } else if (lastSeen) {
        statusMessage = `Zuletzt online: ${lastSeen.toLocaleTimeString()}`;
    } else {
        statusMessage = 'Offline';
    }

    return (
        <div className="online-status">
            <span className={`status-dot ${isOnline ? 'online' : 'offline'}`}></span>
            <span className="status-text">
            {statusMessage}
        </span>
        </div>
    );
};

export default OnlineStatus;