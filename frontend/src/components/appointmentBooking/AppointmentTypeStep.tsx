import React from 'react';
import type {AppointmentType} from '../../types/appointment/AppointmentTypes.ts';
import { appointmentService } from '../../services/appointmentService.ts';
import './AppointmentTypeStep.css';

interface AppointmentTypeStepProps {
    selectedType?: AppointmentType;
    onTypeSelect: (type: AppointmentType, duration: number) => void;
}

const AppointmentTypeStep: React.FC<AppointmentTypeStepProps> = ({
                                                                     selectedType,
                                                                     onTypeSelect
                                                                 }) => {
    const appointmentTypes = appointmentService.getAppointmentTypes();

    return (
        <div className="appointment-type-step">
            <div className="step-header">
                <h2>Art der Beratung wählen</h2>
                <p>Wählen Sie die für Sie passende Beratungsform</p>
            </div>

            <div className="appointment-types-grid">
                {appointmentTypes.map((type) => (
                    <div
                        key={type.type}
                        className={`type-card ${selectedType === type.type ? 'selected' : ''}`}
                        onClick={() => onTypeSelect(type.type, type.duration)}
                    >
                        <div className="type-icon">
                            <i className={type.icon}></i>
                        </div>

                        <div className="type-content">
                            <h3>{type.name}</h3>
                            <p>{type.description}</p>

                            <div className="type-details">
                <span className="duration">
                  <i className="fas fa-clock"></i>
                    {type.duration} Minuten
                </span>
                            </div>
                        </div>

                        <div className="selection-indicator">
                            <div className="radio-indicator">
                                {selectedType === type.type && <div className="radio-dot"></div>}
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            <div className="type-help">
                <div className="help-card">
                    <i className="fas fa-info-circle"></i>
                    <div>
                        <h4>Unsicher bei der Auswahl?</h4>
                        <p>Wählen Sie "Video-Call" für eine erste umfassende Beratung oder "Chat" für kurze schriftliche Fragen.</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AppointmentTypeStep;