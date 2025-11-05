import React from 'react';
import type {Priority} from '../../types/appointment/AppointmentTypes.ts';
import { appointmentService } from '../../services/appointmentService.ts';
import './DetailsStep.css';

interface DetailsStepProps {
    selectedSymptoms: string[];
    notes: string;
    priority: Priority;
    onSymptomsChange: (symptoms: string[]) => void;
    onNotesChange: (notes: string) => void;
    onPriorityChange: (priority: Priority) => void;
}

const DetailsStep: React.FC<DetailsStepProps> = ({
                                                     selectedSymptoms,
                                                     notes,
                                                     priority,
                                                     onSymptomsChange,
                                                     onNotesChange,
                                                     onPriorityChange,
                                                 }) => {
    const commonSymptoms = appointmentService.getCommonSymptoms();

    const handleSymptomToggle = (symptom: string) => {
        const newSymptoms = selectedSymptoms.includes(symptom)
            ? selectedSymptoms.filter(s => s !== symptom)
            : [...selectedSymptoms, symptom];
        onSymptomsChange(newSymptoms);
    };

    const priorityOptions = [
        { value: 'ROUTINE' as Priority, label: 'Routine', description: 'Normale Dringlichkeit' },
        { value: 'URGENT' as Priority, label: 'Dringend', description: 'Benötigt baldige Aufmerksamkeit' },
        { value: 'EMERGENCY' as Priority, label: 'Notfall', description: 'Sofortige Aufmerksamkeit erforderlich' },
    ];

    return (
        <div className="details-step">
            <div className="step-header">
                <h2>Ihr Anliegen beschreiben</h2>
                <p>Helfen Sie uns, Sie bestmöglich zu unterstützen</p>
            </div>

            <div className="symptoms-section">
                <h3>Was beschäftigt Sie? (Mehrfachauswahl möglich)</h3>
                <div className="symptoms-grid">
                    {commonSymptoms.map((symptom) => (
                        <div
                            key={symptom}
                            className={`symptom-card ${selectedSymptoms.includes(symptom) ? 'selected' : ''}`}
                            onClick={() => handleSymptomToggle(symptom)}
                        >
                            <div className="symptom-checkbox">
                                {selectedSymptoms.includes(symptom) && (
                                    <i className="fas fa-check"></i>
                                )}
                            </div>
                            <span className="symptom-text">{symptom}</span>
                        </div>
                    ))}
                </div>
            </div>

            <div className="notes-section">
                <h3>Weitere Details zu Ihrem Anliegen</h3>
                <textarea
                    value={notes}
                    onChange={(e) => onNotesChange(e.target.value)}
                    placeholder="Beschreiben Sie hier näher, was Sie bewegt oder welche spezifischen Fragen Sie haben..."
                    rows={6}
                    className="notes-textarea"
                />
                <div className="notes-hint">
                    <i className="fas fa-info-circle"></i>
                    Diese Informationen helfen Ihrem Berater, sich auf das Gespräch vorzubereiten.
                </div>
            </div>

            <div className="priority-section">
                <h3>Wie dringend ist Ihr Anliegen?</h3>
                <div className="priority-options">
                    {priorityOptions.map((option) => (
                        <div
                            key={option.value}
                            className={`priority-option ${priority === option.value ? 'selected' : ''}`}
                            onClick={() => onPriorityChange(option.value)}
                        >
                            <div className="priority-radio">
                                {priority === option.value && <div className="radio-dot"></div>}
                            </div>
                            <div className="priority-content">
                                <h4>{option.label}</h4>
                                <p>{option.description}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default DetailsStep;