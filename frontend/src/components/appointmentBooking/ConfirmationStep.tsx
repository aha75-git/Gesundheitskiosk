import React from 'react';
import type {AppointmentBookingData} from '../../types/appointment/AppointmentTypes.ts';
import type {Advisor} from "../../types/advisor/AdvisorTypes.ts";

import './ConfirmationStep.css';

interface ConfirmationStepProps {
    advisor: Advisor;
    bookingData: AppointmentBookingData;
    isSubmitting: boolean;
}

const ConfirmationStep: React.FC<ConfirmationStepProps> = ({
                                                               advisor,
                                                               bookingData,
                                                               isSubmitting,
                                                           }) => {
    const formatDate = (dateString: string) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('de-DE', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    console.log(isSubmitting);

    const getAppointmentTypeName = (type: string) => {
        const types: { [key: string]: string } = {
            'VIDEO_CALL': 'Video-Call',
            'PHONE_CALL': 'Telefonat',
            'IN_PERSON': 'Persönlicher Termin',
            'CHAT': 'Chat-Beratung'
        };
        return types[type] || type;
    };

    const getPriorityLabel = (priority: string) => {
        const priorities: { [key: string]: string } = {
            'ROUTINE': 'Routine',
            'URGENT': 'Dringend',
            'EMERGENCY': 'Notfall'
        };
        return priorities[priority] || priority;
    };

    return (
        <div className="confirmation-step">
            <div className="step-header">
                <h2>Termin bestätigen</h2>
                <p>Überprüfen Sie Ihre Angaben bevor Sie den Termin buchen</p>
            </div>

            <div className="confirmation-content">
                <div className="confirmation-section">
                    <h3>Berater</h3>
                    <div className="advisor-summary-confirm">
                        {advisor.image ? (
                            <img src={advisor.image} alt={advisor.name} className="advisor-avatar" />
                        ) : (
                            <div className="advisor-avatar-placeholder">
                                <i className="fas fa-user-md"></i>
                            </div>
                        )}
                        <div className="advisor-details">
                            <h4>{advisor.name}</h4>
                            <p>{advisor.specialization}</p>

                            <div className="advisor-rating">
                                <i className="fas fa-star"></i>
                                <span>{advisor.rating.toFixed(1)}</span>
                            </div>

                        </div>
                    </div>
                </div>

                <div className="confirmation-section">
                    <h3>Termindetails</h3>
                    <div className="details-grid">
                        <div className="detail-item">
                            <span className="detail-label">Art der Beratung:</span>
                            <span className="detail-value">{getAppointmentTypeName(bookingData.type)}</span>
                        </div>
                        <div className="detail-item">
                            <span className="detail-label">Datum & Uhrzeit:</span>
                            <span className="detail-value">{formatDate(bookingData.scheduledAt)}</span>
                        </div>
                        <div className="detail-item">
                            <span className="detail-label">Dauer:</span>
                            <span className="detail-value">{bookingData.duration} Minuten</span>
                        </div>
                        <div className="detail-item">
                            <span className="detail-label">Dringlichkeit:</span>
                            <span className="detail-value">{getPriorityLabel(bookingData.priority)}</span>
                        </div>
                    </div>
                </div>

                {bookingData.symptoms.length > 0 && (
                    <div className="confirmation-section">
                        <h3>Ausgewählte Themen</h3>
                        <div className="symptoms-list">
                            {bookingData.symptoms.map((symptom, index) => (
                                <div key={index} className="symptom-tag">
                                    <i className="fas fa-check"></i>
                                    {symptom}
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                {bookingData.notes && (
                    <div className="confirmation-section">
                        <h3>Ihre Notizen</h3>
                        <div className="notes-preview">
                            <p>{bookingData.notes}</p>
                        </div>
                    </div>
                )}

                <div className="confirmation-notice">
                    <div className="notice-header">
                        <i className="fas fa-info-circle"></i>
                        <h4>Wichtige Hinweise</h4>
                    </div>
                    <ul>
                        <li>Sie erhalten eine Bestätigungs-E-Mail mit allen Details</li>
                        <li>Stornierungen sind bis zu 24 Stunden vor Termin kostenfrei möglich</li>
                        <li>Bitte seien Sie pünktlich zum Termin bereit</li>
                        <li>Bei Video-Calls: Stellen Sie eine stabile Internetverbindung sicher</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default ConfirmationStep;