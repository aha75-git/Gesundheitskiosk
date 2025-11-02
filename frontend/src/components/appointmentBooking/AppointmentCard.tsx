import React, { useState } from 'react';
import type {
    AppointmentResponse,
    AppointmentStatus,
    AppointmentType
} from '../../types/appointment/AppointmentTypes.ts';
import './AppointmentCard.css';

interface AppointmentCardProps {
    appointment: AppointmentResponse;
    onStatusUpdate: (appointmentId: string, newStatus: AppointmentStatus) => void;
    onCancel: (appointmentId: string) => void;
}

const AppointmentCard: React.FC<AppointmentCardProps> = ({
                                                             appointment,
                                                             onStatusUpdate,
                                                             onCancel
                                                         }) => {
    const [showDetails, setShowDetails] = useState(false);

    const getStatusColor = (status: AppointmentStatus) => {
        const statusColors = {
            REQUESTED: 'var(--warning)',
            SCHEDULED: 'var(--primary)',
            CONFIRMED: 'var(--success)',
            IN_PROGRESS: 'var(--info)',
            COMPLETED: 'var(--gray)',
            CANCELLED: 'var(--danger)',
            NO_SHOW: 'var(--danger)'
        };
        return statusColors[status] || 'var(--gray)';
    };

    const getTypeIcon = (type: AppointmentType) => {
        const typeIcons = {
            VIDEO_CALL: 'fas fa-video',
            PHONE_CALL: 'fas fa-phone',
            IN_PERSON: 'fas fa-user',
            CHAT: 'fas fa-comments'
        };
        return typeIcons[type] || 'fas fa-calendar';
    };

    const getPriorityColor = (priority: string) => {
        const priorityColors = {
            ROUTINE: 'var(--success)',
            URGENT: 'var(--warning)',
            EMERGENCY: 'var(--danger)'
        };
        return priorityColors[priority as keyof typeof priorityColors] || 'var(--gray)';
    };

    const formatDateTime = (date: Date) => {
        return new Date(date).toLocaleString('de-DE', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const formatDuration = (minutes: number) => {
        const hours = Math.floor(minutes / 60);
        const mins = minutes % 60;
        if (hours > 0) {
            return `${hours}h ${mins > 0 ? `${mins}m` : ''}`;
        }
        return `${minutes}m`;
    };

    const canCancel = ['REQUESTED', 'SCHEDULED', 'CONFIRMED'].includes(appointment.status);
    const isUpcoming = new Date(appointment.scheduledAt) > new Date();

    return (
        <div className="appointment-card">
            <div className="appointment-header">
                <div className="appointment-type">
                    <i className={getTypeIcon(appointment.type)}></i>
                    <span>
            {appointment.type === 'VIDEO_CALL' && 'Video-Call'}
                        {appointment.type === 'PHONE_CALL' && 'Telefonat'}
                        {appointment.type === 'IN_PERSON' && 'Persönlicher Termin'}
                        {appointment.type === 'CHAT' && 'Chat-Beratung'}
          </span>
                </div>

                <div
                    className="appointment-status"
                    style={{ backgroundColor: getStatusColor(appointment.status) }}
                >
                    {appointment.status === 'REQUESTED' && 'Angefragt'}
                    {appointment.status === 'SCHEDULED' && 'Geplant'}
                    {appointment.status === 'CONFIRMED' && 'Bestätigt'}
                    {appointment.status === 'IN_PROGRESS' && 'In Bearbeitung'}
                    {appointment.status === 'COMPLETED' && 'Abgeschlossen'}
                    {appointment.status === 'CANCELLED' && 'Storniert'}
                    {appointment.status === 'NO_SHOW' && 'Nicht Erschienen'}
                </div>
            </div>

            <div className="appointment-body">
                <div className="appointment-datetime">
                    <i className="fas fa-clock"></i>
                    <div>
                        <strong>{formatDateTime(appointment.scheduledAt)}</strong>
                        <span>Dauer: {formatDuration(appointment.durationMinutes)}</span>
                    </div>
                </div>

                <div className="appointment-priority">
                    <i className="fas fa-exclamation-circle"></i>
                    <span style={{ color: getPriorityColor(appointment.priority) }}>
            {appointment.priority === 'ROUTINE' && 'Routine'}
                        {appointment.priority === 'URGENT' && 'Dringend'}
                        {appointment.priority === 'EMERGENCY' && 'Notfall'}
          </span>
                </div>

                {appointment.symptoms.length > 0 && (
                    <div className="appointment-symptoms">
                        <i className="fas fa-stethoscope"></i>
                        <span>{appointment.symptoms.join(', ')}</span>
                    </div>
                )}

                {showDetails && appointment.notes && (
                    <div className="appointment-notes">
                        <i className="fas fa-sticky-note"></i>
                        <p>{appointment.notes}</p>
                    </div>
                )}
            </div>

            <div className="appointment-footer">
                <button
                    onClick={() => setShowDetails(!showDetails)}
                    className="btn-details"
                >
                    <i className={`fas fa-chevron-${showDetails ? 'up' : 'down'}`}></i>
                    {showDetails ? 'Weniger Details' : 'Mehr Details'}
                </button>

                <div className="appointment-actions">
                    {canCancel && isUpcoming && (
                        <button
                            onClick={() => onCancel(appointment.id)}
                            className="btn btn-danger"
                        >
                            <i className="fas fa-times"></i>
                            Stornieren
                        </button>
                    )}

                    {isUpcoming && appointment.status === 'REQUESTED' && (
                        <button
                            onClick={() => onStatusUpdate(appointment.id, 'CONFIRMED')}
                            className="btn btn-success"
                        >
                            <i className="fas fa-check"></i>
                            Bestätigen
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default AppointmentCard;