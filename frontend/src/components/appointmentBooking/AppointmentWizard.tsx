import React, { useState } from 'react';
import type { AppointmentBookingData, AdvisorAvailability, BookingWizardStep } from '../../types/appointment/AppointmentTypes.ts';
// import { appointmentBookingService } from '../services/appointmentBookingService';
import AppointmentTypeStep from './AppointmentTypeStep';
import DateTimeStep from './DateTimeStep';
import DetailsStep from './DetailsStep';
import ConfirmationStep from './ConfirmationStep';
import './AppointmentWizard.css';
import type {Advisor} from "../../types/advisor/AdvisorTypes.ts";

interface AppointmentWizardProps {
    advisor: Advisor;
    availability: AdvisorAvailability | null;
    currentStep: number;
    steps: BookingWizardStep[];
    onStepChange: (step: number) => void;
    onBookAppointment: (bookingData: AppointmentBookingData) => Promise<void>;
    onAvailabilityUpdate: (date: string) => void;
}

const AppointmentWizard: React.FC<AppointmentWizardProps> = ({
                                                                 advisor,
                                                                 availability,
                                                                 currentStep,
                                                                 steps,
                                                                 onStepChange,
                                                                 onBookAppointment,
                                                                 onAvailabilityUpdate
                                                             }) => {
    const [bookingData, setBookingData] = useState<Partial<AppointmentBookingData>>({
        advisorId: advisor.id,
        type: undefined,
        scheduledAt: '',
        duration: 60,
        notes: '',
        symptoms: [],
        priority: 'ROUTINE'
    });

    const [isSubmitting, setIsSubmitting] = useState(false);

    const updateBookingData = (updates: Partial<AppointmentBookingData>) => {
        setBookingData(prev => ({ ...prev, ...updates }));
    };

    const handleNextStep = () => {
        if (currentStep < steps.length) {
            onStepChange(currentStep + 1);
        }
    };

    const handlePreviousStep = () => {
        if (currentStep > 1) {
            onStepChange(currentStep - 1);
        }
    };

    const handleSubmit = async () => {
        if (!bookingData.type || !bookingData.scheduledAt) {
            return;
        }

        try {
            setIsSubmitting(true);
            await onBookAppointment(bookingData as AppointmentBookingData);
        } catch (err) {
            console.log(err);
            // Error wird in der Parent-Komponente behandelt
        } finally {
            setIsSubmitting(false);
        }
    };

    const canProceedToNextStep = () => {
        switch (currentStep) {
            case 1:
                return !!bookingData.type;
            case 2:
                return !!bookingData.scheduledAt;
            case 3:
                return bookingData.symptoms && bookingData.symptoms.length > 0;
            case 4:
                return true;
            default:
                return false;
        }
    };

    const renderStep = () => {
        switch (currentStep) {
            case 1:
                return (
                    <AppointmentTypeStep
                        selectedType={bookingData.type}
                        onTypeSelect={(type, duration) => updateBookingData({ type, duration })}
                    />
                );

            case 2:
                return (
                    <DateTimeStep
                        advisor={advisor}
                        availability={availability}
                        selectedDateTime={bookingData.scheduledAt || ''}
                        duration={bookingData.duration || 60}
                        onDateTimeSelect={(datetime) => updateBookingData({ scheduledAt: datetime })}
                        onAvailabilityUpdate={onAvailabilityUpdate}
                    />
                );

            case 3:
                return (
                    <DetailsStep
                        selectedSymptoms={bookingData.symptoms || []}
                        notes={bookingData.notes || ''}
                        priority={bookingData.priority || 'ROUTINE'}
                        onSymptomsChange={(symptoms) => updateBookingData({ symptoms })}
                        onNotesChange={(notes) => updateBookingData({ notes })}
                        onPriorityChange={(priority) => updateBookingData({ priority })}
                    />
                );

            case 4:
                return (
                    <ConfirmationStep
                        advisor={advisor}
                        bookingData={bookingData as AppointmentBookingData}
                        isSubmitting={isSubmitting}
                    />
                );

            default:
                return null;
        }
    };

    return (
        <div className="appointment-wizard">
            {/* Progress Steps */}
            <div className="wizard-progress">
                {steps.map((step, index) => (
                    <div key={step.step} className="progress-step">
                        <div
                            className={`step-indicator ${currentStep >= step.step ? 'active' : ''} ${currentStep === step.step ? 'current' : ''}`}
                            onClick={() => onStepChange(step.step)}
                        >
                            <span className="step-number">{step.step}</span>
                            <div className="step-info">
                                <span className="step-title">{step.title}</span>
                                <span className="step-description">{step.description}</span>
                            </div>
                        </div>

                        {index < steps.length - 1 && (
                            <div className={`step-connector ${currentStep > step.step ? 'active' : ''}`}></div>
                        )}
                    </div>
                ))}
            </div>

            {/* Step Content */}
            <div className="wizard-content">
                {renderStep()}
            </div>

            {/* Navigation */}
            <div className="wizard-navigation">
                <button
                    onClick={handlePreviousStep}
                    disabled={currentStep === 1}
                    className="btn btn-secondary"
                >
                    <i className="fas fa-arrow-left"></i>
                    Zurück
                </button>

                {currentStep < steps.length ? (
                    <button
                        onClick={handleNextStep}
                        disabled={!canProceedToNextStep()}
                        className="btn btn-primary"
                    >
                        Weiter
                        <i className="fas fa-arrow-right"></i>
                    </button>
                ) : (
                    <button
                        onClick={handleSubmit}
                        disabled={isSubmitting || !canProceedToNextStep()}
                        className="btn btn-primary"
                    >
                        {isSubmitting ? (
                            <>
                                <i className="fas fa-spinner fa-spin"></i>
                                Wird gebucht...
                            </>
                        ) : (
                            <>
                                <i className="fas fa-calendar-check"></i>
                                Termin buchen
                            </>
                        )}
                    </button>
                )}
            </div>
        </div>
    );
};

export default AppointmentWizard;