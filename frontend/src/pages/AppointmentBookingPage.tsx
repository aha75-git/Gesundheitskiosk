import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContext';
import { appointmentService } from '../services/appointmentService.ts';
import { advisorService } from '../services/advisorService';
import type {
    AppointmentBookingData,
    AdvisorAvailability,
    BookingWizardStep
} from '../types/appointment/AppointmentTypes.ts';
import type {Advisor} from "../types/advisor/AdvisorTypes.ts";
import AppointmentWizard from '../components/appointmentBooking/AppointmentWizard';
import './AppointmentBookingPage.css';

export default function AppointmentBookingPage(){
    const { advisorId } = useParams<{ advisorId: string }>();
    const { user } = useAuth();
    const navigate = useNavigate();

    const [advisor, setAdvisor] = useState<Advisor | null>(null);
    const [availability, setAvailability] = useState<AdvisorAvailability | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [currentStep, setCurrentStep] = useState(1);

    const steps: BookingWizardStep[] = [
        { step: 1, title: 'Terminart wählen', description: 'Wählen Sie die Art der Beratung' },
        { step: 2, title: 'Termin auswählen', description: 'Wählen Sie einen passenden Termin' },
        { step: 3, title: 'Details angeben', description: 'Beschreiben Sie Ihr Anliegen' },
        { step: 4, title: 'Bestätigung', description: 'Überprüfen Sie Ihre Angaben' }
    ];

    useEffect(() => {
        if (!user) {
            navigate('/login');
            return;
        }

        if (advisorId) {
            loadAdvisorAndAvailability();
        }
    }, [advisorId, user, navigate]);

    const loadAdvisorAndAvailability = async () => {
        try {
            setIsLoading(true);
            setError('');

            // Beraterdaten laden
            const advisorData = await advisorService.getAdvisorById(advisorId!);
            setAdvisor(advisorData);

            // Verfügbarkeit für heute laden
            const today = new Date().toISOString().split('T')[0];
            const availabilityDataResponse = await appointmentService.getAdvisorAvailability(advisorId!, today);

            const dateFromResponse = new Date(availabilityDataResponse.date);
            const dateToString = dateFromResponse.toISOString();

            const availabilityData: AdvisorAvailability = {
                advisorId: availabilityDataResponse.advisorId,
                date: dateToString,
                availableSlots: availabilityDataResponse.availableSlots,
                workingHours: availabilityDataResponse.workingHours,
            }
            setAvailability(availabilityData);

        } catch (err) {
            setError('Fehler beim Laden der Beraterdaten. Bitte versuchen Sie es erneut.');
            console.error('Error loading advisor data:', err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleBookAppointment = async (bookingData: AppointmentBookingData) => {
        try {
            setError('');

            // Termin erstellen
            const appointment = await appointmentService.createAppointment(bookingData);

            // Zur Bestätigungsseite navigieren
            navigate('/appointment-confirmation', {
                state: {
                    appointment,
                    advisor
                }
            });

        } catch (err) {
            setError('Fehler bei der Terminbuchung. Bitte versuchen Sie es erneut.');
            console.error('Error booking appointment:', err);
            throw err; // Wird im Wizard behandelt
        }
    };

    const handleAvailabilityUpdate = async (date: string) => {
        try {

            const availabilityDataResponse = await appointmentService.getAdvisorAvailability(advisorId!, date);
            const dateFromResponse = new Date(availabilityDataResponse.date);
            const dateToString = dateFromResponse.toISOString();

            const availabilityData: AdvisorAvailability = {
                advisorId: availabilityDataResponse.advisorId,
                date: dateToString,
                availableSlots: availabilityDataResponse.availableSlots,
                workingHours: availabilityDataResponse.workingHours,
            }
            setAvailability(availabilityData);
        } catch (err) {
            setError('Fehler beim Laden der Verfügbarkeit. Bitte versuchen Sie es erneut.');
            console.error('Error loading availability:', err);
        }
    };

    if (!user) {
        return null;
    }

    if (isLoading) {
        return (
            <div className="appointment-booking-page">
                <div className="container">
                    <div className="loading-container">
                        <div className="loading-spinner">
                            <i className="fas fa-spinner fa-spin"></i>
                            <p>Lade Beraterinformationen...</p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    if (!advisor) {
        return (
            <div className="appointment-booking-page">
                <div className="container">
                    <div className="error-state">
                        <i className="fas fa-exclamation-triangle"></i>
                        <h3>Berater nicht gefunden</h3>
                        <p>Der angeforderte Berater konnte nicht gefunden werden.</p>
                        <button
                            onClick={() => navigate('/search')}
                            className="btn btn-primary"
                        >
                            Zurück zur Beratersuche
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="appointment-booking-page">
            <div className="container">
                <div className="booking-header">
                    <button
                        onClick={() => navigate(-1)}
                        className="btn-back"
                    >
                        <i className="fas fa-arrow-left"></i>
                        Zurück
                    </button>

                    <div className="header-content">
                        <h1>Termin buchen</h1>
                        <p>Buchen Sie einen Termin mit {advisor.name}</p>
                    </div>

                    <div className="advisor-summary">
                        <div className="advisor-info-compact">
                            {advisor.image ? (
                                <img src={advisor.image} alt={advisor.name} className="advisor-avatar" />
                            ) : (
                                <div className="advisor-avatar-placeholder">
                                    <i className="fas fa-user-md"></i>
                                </div>
                            )}
                            <div className="advisor-details">
                                <h3>{advisor.name}</h3>
                                <p>{advisor.specialization}</p>
                                <div className="advisor-rating">
                                    <i className="fas fa-star"></i>
                                    <span>{advisor.rating.toFixed(1)}</span>
                                    <span>({advisor.recentReviews.length} Bewertungen)</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {error && (
                    <div className="alert alert-error">
                        <i className="fas fa-exclamation-circle"></i>
                        {error}
                    </div>
                )}

                <div className="booking-content">
                    <div className="booking-wizard-container">
                        <AppointmentWizard
                            advisor={advisor}
                            availability={availability}
                            currentStep={currentStep}
                            steps={steps}
                            onStepChange={setCurrentStep}
                            onBookAppointment={handleBookAppointment}
                            onAvailabilityUpdate={handleAvailabilityUpdate}
                        />
                    </div>

                    <div className="booking-sidebar">
                        <div className="sidebar-card">
                            <h3>Ihr Berater</h3>
                            <div className="advisor-details-sidebar">
                                <div className="advisor-header-sidebar">
                                    {advisor.image ? (
                                        <img src={advisor.image} alt={advisor.name} />
                                    ) : (
                                        <div className="avatar-placeholder">
                                            <i className="fas fa-user-md"></i>
                                        </div>
                                    )}
                                    <div>
                                        <h4>{advisor.name}</h4>
                                        <p className="specialization">{advisor.specialization}</p>
                                    </div>
                                </div>

                                <div className="advisor-stats">
                                    <div className="stat">
                                        <i className="fas fa-star"></i>
                                        <span>{advisor.rating.toFixed(1)} Bewertung</span>
                                    </div>
                                    <div className="stat">
                                        <i className="fas fa-briefcase"></i>
                                        <span>{advisor.experience} Jahre Erfahrung</span>
                                    </div>
                                    <div className="stat">
                                        <i className="fas fa-language"></i>
                                        <span>{advisor.languages.join(', ')}</span>
                                    </div>
                                </div>

                                {/*<div className="advisor-fee">*/}
                                {/*    <i className="fas fa-euro-sign"></i>*/}
                                {/*    <span>{advisor.consultationFee} € pro Stunde</span>*/}
                                {/*</div>*/}

                                <div className="availability-status">
                                    <i className={`fas fa-circle ${advisor.available ? 'available' : 'unavailable'}`}></i>
                                    <span>{advisor.available ? 'Jetzt verfügbar' : 'Zurzeit nicht verfügbar'}</span>
                                </div>
                            </div>
                        </div>

                        <div className="sidebar-card">
                            <h3>Was Sie erwartet</h3>
                            <ul className="expectation-list">
                                <li>
                                    <i className="fas fa-check-circle"></i>
                                    <span>Professionelle Beratung</span>
                                </li>
                                <li>
                                    <i className="fas fa-check-circle"></i>
                                    <span>Diskrete Behandlung</span>
                                </li>
                                <li>
                                    <i className="fas fa-check-circle"></i>
                                    <span>Individuelle Lösungen</span>
                                </li>
                                <li>
                                    <i className="fas fa-check-circle"></i>
                                    <span>Nachbereitungsunterlagen</span>
                                </li>
                            </ul>
                        </div>

                        <div className="sidebar-card">
                            <h3>Kontakt</h3>
                            <div className="contact-info">
                                <p>Fragen zur Terminbuchung?</p>
                                <button className="btn btn-secondary btn-small">
                                    <i className="fas fa-envelope"></i>
                                    Nachricht senden
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
