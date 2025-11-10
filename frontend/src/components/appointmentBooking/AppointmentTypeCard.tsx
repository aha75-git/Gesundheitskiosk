import React from 'react';
import type {AppointmentType as AppointmentTypeEnum} from '../../types/appointment/AppointmentTypes.ts';
import './AppointmentTypeCard.css';

export interface AppointmentType {
    type: AppointmentTypeEnum;
    name: string;
    description: string;
    duration: number;
    icon: string;
}

interface AppointmentTypeCardProps {
    type: AppointmentType;
    selected: boolean;
    onSelect: (type: AppointmentTypeEnum) => void;
    onBook: (type: AppointmentTypeEnum) => void;
    showBookButton: boolean;
    compact?: boolean;
}

const AppointmentTypeCard: React.FC<AppointmentTypeCardProps> = ({
                                                                     type,
                                                                     selected,
                                                                     onSelect,
                                                                     onBook,
                                                                     showBookButton,
                                                                     compact = false
                                                                 }) => {
    const handleCardClick = () => {
        onSelect(type.type);
    };

    const handleBookClick = (e: React.MouseEvent) => {
        e.stopPropagation();
        onBook(type.type);
    };

    if (compact) {
        return (
            <div
                className={`appointment-type-card compact ${selected ? 'selected' : ''}`}
                onClick={handleCardClick}
            >
                <div className="card-content-compact">
                    <div className="icon-section">
                        <i className={type.icon}></i>
                    </div>
                    <div className="details-section">
                        <h4>{type.name}</h4>
                        <span className="duration">{type.duration} Min.</span>
                    </div>
                    {showBookButton && (
                        <button
                            onClick={handleBookClick}
                            className="btn-book-compact"
                            title={`${type.name} buchen`}
                        >
                            <i className="fas fa-arrow-right"></i>
                        </button>
                    )}
                </div>
            </div>
        );
    }

    return (
        <div
            className={`appointment-type-card ${selected ? 'selected' : ''}`}
            onClick={handleCardClick}
        >
            <div className="card-header">
                <div className="type-icon">
                    <i className={type.icon}></i>
                </div>
                <div className="type-info">
                    <h3>{type.name}</h3>
                    <p className="type-description">{type.description}</p>
                </div>
                <div className="selection-indicator">
                    <div className="radio-indicator">
                        {selected && <div className="radio-dot"></div>}
                    </div>
                </div>
            </div>

            <div className="card-body">
                <div className="type-details">
                    <div className="detail-item">
                        <i className="fas fa-clock"></i>
                        <span>{type.duration} Minuten</span>
                    </div>
                    <div className="detail-item">
                        <i className="fas fa-video"></i>
                        <span>
              {type.type === 'VIDEO_CALL' && 'Video-Konferenz'}
                            {type.type === 'PHONE_CALL' && 'Telefonat'}
                            {type.type === 'IN_PERSON' && 'Persönliches Treffen'}
                            {type.type === 'CHAT' && 'Text-Chat'}
            </span>
                    </div>
                </div>

                {type.type === 'VIDEO_CALL' && (
                    <div className="features-list">
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Echtzeit-Video und Audio</span>
                        </div>
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Bildschirm teilen möglich</span>
                        </div>
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Aufzeichnung auf Wunsch</span>
                        </div>
                    </div>
                )}

                {type.type === 'PHONE_CALL' && (
                    <div className="features-list">
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Diskrete Telefonberatung</span>
                        </div>
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Flexibel von überall</span>
                        </div>
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Keine technische Einrichtung nötig</span>
                        </div>
                    </div>
                )}

                {type.type === 'IN_PERSON' && (
                    <div className="features-list">
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Persönliches Kennenlernen</span>
                        </div>
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Direkte Interaktion</span>
                        </div>
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Private Beratungsumgebung</span>
                        </div>
                    </div>
                )}

                {type.type === 'CHAT' && (
                    <div className="features-list">
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Schriftliche Dokumentation</span>
                        </div>
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Flexible Zeiteinteilung</span>
                        </div>
                        <div className="feature">
                            <i className="fas fa-check"></i>
                            <span>Nachberatung verfügbar</span>
                        </div>
                    </div>
                )}
            </div>

            <div className="card-footer">
                {showBookButton ? (
                    <button
                        onClick={handleBookClick}
                        className="btn btn-primary btn-full"
                    >
                        <i className="fas fa-calendar-check"></i>
                        {type.name} buchen
                    </button>
                ) : (
                    <div className="selection-hint">
                        {selected ? (
                            <div className="selected-indicator">
                                <i className="fas fa-check-circle"></i>
                                <span>Ausgewählt</span>
                            </div>
                        ) : (
                            <span className="select-hint">Zum Auswählen klicken</span>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default AppointmentTypeCard;