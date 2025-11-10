import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import type {AppointmentType} from '../types/appointment/AppointmentTypes.ts';
import type {Advisor} from '../types/advisor/AdvisorTypes.ts';
import { advisorService } from '../services/advisorService';
import { appointmentService } from '../services/appointmentService';
import { useAuth } from '../api/AuthContext';
import AppointmentTypeCard from '../components/appointmentBooking/AppointmentTypeCard';
import WorkingHoursDisplay from '../components/advisorSearch/WorkingHoursDisplay';
import ReviewCard from '../components/advisorSearch/ReviewCard';
import './AdvisorProfilePage.css';

const AdvisorProfilePage: React.FC = () => {
    const { advisorId } = useParams<{ advisorId: string }>();
    const { user } = useAuth();
    const navigate = useNavigate();

    const [advisor, setAdvisor] = useState<Advisor | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [activeTab, setActiveTab] = useState<'overview' | 'reviews' | 'availability'>('overview');
    const [selectedAppointmentType, setSelectedAppointmentType] = useState<AppointmentType | null>(null);

    useEffect(() => {
        if (advisorId) {
            loadAdvisorData();
        }
    }, [advisorId]);

    const loadAdvisorData = async () => {
        try {
            setIsLoading(true);
            setError('');
            const advisorData = await advisorService.getAdvisorById(advisorId!);
            setAdvisor(advisorData);
        } catch (err) {
            setError('Berater konnte nicht geladen werden. Bitte versuchen Sie es erneut.');
            console.error('Error loading advisor:', err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleBookAppointment = (type: AppointmentType) => {
        if (!user) {
            navigate('/login', { state: { returnUrl: `/advisor/${advisorId}` } });
            return;
        }
        navigate(`/appointment/${advisorId}`, { state: { selectedType: type } });
    };

    const handleContactAdvisor = () => {
        if (!user) {
            navigate('/login', { state: { returnUrl: `/advisor/${advisorId}` } });
            return;
        }
        // Hier könnte eine Kontaktfunktion implementiert werden
        alert('Kontaktfunktion wird geöffnet...');
    };

    const formatExperience = (years: number) => {
        if (years === 1) return '1 Jahr Erfahrung';
        return `${years} Jahre Erfahrung`;
    };

    const getAvailabilityStatus = (available: boolean) => {
        return available ? 'Jetzt verfügbar' : 'Zurzeit nicht verfügbar';
    };

    const getAvailabilityColor = (available: boolean) => {
        return available ? 'available' : 'unavailable';
    };

    if (isLoading) {
        return (
            <div className="advisor-profile-page">
                <div className="container">
                    <div className="loading-container">
                        <div className="loading-spinner">
                            <i className="fas fa-spinner fa-spin"></i>
                            <p>Lade Beraterprofil...</p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    if (error || !advisor) {
        return (
            <div className="advisor-profile-page">
                <div className="container">
                    <div className="error-state">
                        <i className="fas fa-exclamation-triangle"></i>
                        <h3>Berater nicht gefunden</h3>
                        <p>{error || 'Der angeforderte Berater konnte nicht gefunden werden.'}</p>
                        <div className="action-buttons">
                            <button
                                onClick={() => navigate('/search')}
                                className="btn btn-primary"
                            >
                                Zurück zur Beratersuche
                            </button>
                            <button
                                onClick={() => navigate('/dashboard')}
                                className="btn btn-secondary"
                            >
                                Zum Dashboard
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="advisor-profile-page">
            <div className="container">
                {/* Header mit Back Button */}
                <div className="profile-header">
                    <button
                        onClick={() => navigate(-1)}
                        className="btn-back"
                    >
                        <i className="fas fa-arrow-left"></i>
                        Zurück
                    </button>
                </div>

                <div className="profile-content">
                    {/* Hauptinhalt */}
                    <div className="profile-main">
                        {/* Berater-Kopfbereich */}
                        <div className="advisor-header profile-advisor-header">
                            <div className="advisor-identity">
                                <div className="avatar-section">
                                    {advisor.image ? (
                                        <img src={advisor.image} alt={advisor.name} className="advisor-avatar-large" />
                                    ) : (
                                        <div className="advisor-avatar-placeholder-large">
                                            <i className="fas fa-user-md"></i>
                                        </div>
                                    )}
                                    <div className="availability-badge profile-availability-badge">
                                        <i className={`fas fa-circle ${getAvailabilityColor(advisor.available)}`}></i>
                                        {getAvailabilityStatus(advisor.available)}
                                    </div>
                                </div>

                                <div className="identity-details">
                                    <h1>{advisor.name}</h1>
                                    <p className="specialization">{advisor.specialization}</p>

                                    <div className="rating-section profile-rating-section">
                                        <div className="rating-display">
                                            <div className="stars profile-stars">
                                                {[1, 2, 3, 4, 5].map((star) => (
                                                    <i
                                                        key={star}
                                                        className={`fas fa-star ${star <= Math.floor(advisor.rating || 0) ? 'filled' : 'empty'}`}
                                                    ></i>
                                                ))}
                                            </div>
                                            <span className="rating-value profile-rating-value">{advisor.rating?.toFixed(1)}</span>
                                            <span className="review-count profile-review-count">({/*advisor.reviewCount*/ 0} Bewertungen)</span>
                                        </div>
                                    </div>

                                    <div className="quick-stats">
                                        <div className="stat">
                                            <i className="fas fa-briefcase"></i>
                                            <span>{formatExperience(advisor.experience || 0)}</span>
                                        </div>
                                        <div className="stat">
                                            <i className="fas fa-language"></i>
                                            <span>{advisor.languages?.join(', ') || 'Deutsch'}</span>
                                        </div>
                                        {/*<div className="stat">*/}
                                        {/*    <i className="fas fa-euro-sign"></i>*/}
                                        {/*    <span>{advisor.consultationFee} €/Stunde</span>*/}
                                        {/*</div>*/}
                                    </div>
                                </div>
                            </div>

                            <div className="action-buttons-header">
                                <button
                                    onClick={() => handleBookAppointment('VIDEO_CALL')}
                                    className="btn btn-primary btn-large"
                                >
                                    <i className="fas fa-calendar-plus"></i>
                                    Termin buchen
                                </button>
                                <button
                                    onClick={handleContactAdvisor}
                                    className="btn btn-secondary"
                                >
                                    <i className="fas fa-envelope"></i>
                                    Nachricht senden
                                </button>
                            </div>
                        </div>

                        {/* Tab Navigation */}
                        <div className="tab-navigation">
                            <button
                                className={`tab-button ${activeTab === 'overview' ? 'active' : ''}`}
                                onClick={() => setActiveTab('overview')}
                            >
                                <i className="fas fa-info-circle"></i>
                                Übersicht
                            </button>
                            <button
                                className={`tab-button ${activeTab === 'reviews' ? 'active' : ''}`}
                                onClick={() => setActiveTab('reviews')}
                            >
                                <i className="fas fa-star"></i>
                                Bewertungen
                                <span className="tab-badge">{/*advisor.reviewCount*/ 0}</span>
                            </button>
                            <button
                                className={`tab-button ${activeTab === 'availability' ? 'active' : ''}`}
                                onClick={() => setActiveTab('availability')}
                            >
                                <i className="fas fa-clock"></i>
                                Verfügbarkeit
                            </button>
                        </div>

                        {/* Tab Content */}
                        <div className="tab-content">
                            {activeTab === 'overview' && (
                                <div className="overview-tab">
                                    {/* Über mich Section */}
                                    {advisor.bio && (
                                        <section className="bio-section">
                                            <h2>Über mich</h2>
                                            <div className="bio-content">
                                                <p>{advisor.bio}</p>
                                            </div>
                                        </section>
                                    )}

                                    {/* Qualifikationen */}
                                    {advisor.qualifications && (
                                        <section className="qualification-section">
                                            <h2>Qualifikationen</h2>
                                            <div className="qualification-content">
                                                <p>{advisor.qualifications}</p>
                                            </div>
                                        </section>
                                    )}

                                    {/* Beratungsarten */}
                                    <section className="appointment-types-section">
                                        <h2>Beratungsarten</h2>
                                        <div className="appointment-types-grid">
                                            {appointmentService.getAppointmentTypes().map((type) => (
                                                <AppointmentTypeCard
                                                    key={type.type}
                                                    type={type}
                                                    selected={selectedAppointmentType === type.type}
                                                    onSelect={() => setSelectedAppointmentType(type.type)}
                                                    onBook={() => handleBookAppointment(type.type)}
                                                    showBookButton={true}
                                                />
                                            ))}
                                        </div>
                                    </section>

                                    {/* Sprachen */}
                                    {advisor.languages && advisor.languages.length > 0 && (
                                        <section className="languages-section">
                                            <h2>Sprachen</h2>
                                            <div className="languages-list">
                                                {advisor.languages.map((language, index) => (
                                                    <span key={index} className="language-tag">
                            <i className="fas fa-check"></i>
                                                        {language}
                          </span>
                                                ))}
                                            </div>
                                        </section>
                                    )}
                                </div>
                            )}

                            {activeTab === 'reviews' && (
                                <div className="reviews-tab">
                                    <div className="reviews-header">
                                        <div className="rating-summary">
                                            <div className="overall-rating">
                                                <span className="rating-number">{advisor.rating?.toFixed(1)}</span>
                                                <div className="stars-large">
                                                    {[1, 2, 3, 4, 5].map((star) => (
                                                        <i
                                                            key={star}
                                                            className={`fas fa-star ${star <= Math.floor(advisor.rating || 0) ? 'filled' : 'empty'}`}
                                                        ></i>
                                                    ))}
                                                </div>
                                                <span className="total-reviews">{advisor.recentReviews.length} Bewertungen</span>
                                            </div>
                                        </div>

                                        {user && (
                                            <button className="btn btn-primary">
                                                <i className="fas fa-pen"></i>
                                                Bewertung schreiben
                                            </button>
                                        )}
                                    </div>

                                    <div className="reviews-list">
                                        {/* Beispiel-Bewertungen - später durch echte Daten ersetzen */}
                                        <ReviewCard
                                            author="Max Mustermann"
                                            rating={5}
                                            date="15. März 2024"
                                            comment="Sehr professionelle Beratung. Dr. Schmidt hat mir in einer schwierigen Lebensphase sehr geholfen. Kann ich nur empfehlen!"
                                            helpful={12}
                                        />
                                        <ReviewCard
                                            author="Anna Schmidt"
                                            rating={4}
                                            date="10. März 2024"
                                            comment="Gute Gesprächsatmosphäre und kompetente Ratschläge. Terminplanung war unkompliziert."
                                            helpful={8}
                                        />
                                        <ReviewCard
                                            author="Thomas Weber"
                                            rating={5}
                                            date="5. März 2024"
                                            comment="Ausgezeichnete Beratung! Habe viele wertvolle Impulse für meine berufliche Entwicklung erhalten."
                                            helpful={15}
                                        />
                                    </div>

                                    {advisor.recentReviews.length === 0 && (
                                        <div className="no-reviews">
                                            <i className="fas fa-star"></i>
                                            <h3>Noch keine Bewertungen</h3>
                                            <p>Seien Sie der Erste, der eine Bewertung für {advisor.name} schreibt.</p>
                                            {user && (
                                                <button className="btn btn-primary">
                                                    Erste Bewertung schreiben
                                                </button>
                                            )}
                                        </div>
                                    )}
                                </div>
                            )}

                            {activeTab === 'availability' && (
                                <div className="availability-tab">
                                    <div className="availability-header">
                                        <h2>Verfügbarkeit</h2>
                                        <p>Wählen Sie eine Beratungsart um verfügbare Termine zu sehen</p>
                                    </div>

                                    <WorkingHoursDisplay workingHours={advisor.workingHours || []} />

                                    <div className="quick-booking">
                                        <h3>Schnelltermin buchen</h3>
                                        <div className="appointment-type-buttons">
                                            {appointmentService.getAppointmentTypes().map((type) => (
                                                <button
                                                    key={type.type}
                                                    onClick={() => handleBookAppointment(type.type)}
                                                    className="appointment-type-btn"
                                                >
                                                    <i className={type.icon}></i>
                                                    <span>{type.name}</span>
                                                </button>
                                            ))}
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Sidebar */}
                    <div className="profile-sidebar">
                        <div className="sidebar-card booking-widget">
                            <h3>Termin buchen</h3>
                            <p>Wählen Sie eine Beratungsart aus:</p>

                            <div className="booking-options">
                                {appointmentService.getAppointmentTypes().map((type) => (
                                    <button
                                        key={type.type}
                                        onClick={() => handleBookAppointment(type.type)}
                                        className="booking-option"
                                    >
                                        <div className="option-icon">
                                            <i className={type.icon}></i>
                                        </div>
                                        <div className="option-details">
                                            <span className="option-name">{type.name}</span>
                                            <span className="option-duration">{type.duration} Min.</span>
                                        </div>
                                        {/*<div className="option-price">*/}
                                        {/*    {Math.round((advisor.consultationFee * type.duration) / 60)} €*/}
                                        {/*</div>*/}
                                    </button>
                                ))}
                            </div>

                            <div className="booking-footer">
                                {/*<div className="price-summary">*/}
                                {/*    <span>Preis pro Stunde:</span>*/}
                                {/*    <span className="price">{advisor.consultationFee} €</span>*/}
                                {/*</div>*/}
                                <button
                                    onClick={() => handleBookAppointment('VIDEO_CALL')}
                                    className="btn btn-primary btn-full"
                                >
                                    <i className="fas fa-calendar-check"></i>
                                    Termin auswählen
                                </button>
                            </div>
                        </div>

                        <div className="sidebar-card contact-widget">
                            <h3>Kontakt</h3>
                            <div className="contact-options">
                                <button
                                    onClick={handleContactAdvisor}
                                    className="btn btn-secondary btn-full"
                                >
                                    <i className="fas fa-envelope"></i>
                                    Nachricht senden
                                </button>
                                <button className="btn btn-outline btn-full">
                                    <i className="fas fa-phone"></i>
                                    Telefon anrufen
                                </button>
                            </div>

                            <div className="contact-info">
                                <div className="info-item">
                                    <i className="fas fa-clock"></i>
                                    <span>Antwortzeit: Innerhalb von 24h</span>
                                </div>
                                <div className="info-item">
                                    <i className="fas fa-calendar"></i>
                                    <span>Termine: Flexibel verfügbar</span>
                                </div>
                            </div>
                        </div>

                        <div className="sidebar-card stats-widget">
                            <h3>Statistiken</h3>
                            <div className="stats-grid">
                                <div className="stat-item">
                                    <span className="stat-value">{advisor.experience}+</span>
                                    <span className="stat-label">Jahre Erfahrung</span>
                                </div>
                                <div className="stat-item">
                                    <span className="stat-value">{advisor.recentReviews.length}+</span>
                                    <span className="stat-label">Bewertungen</span>
                                </div>
                                <div className="stat-item">
                                    <span className="stat-value">98%</span>
                                    <span className="stat-label">Zufriedenheit</span>
                                </div>
                                <div className="stat-item">
                                    <span className="stat-value">24/7</span>
                                    <span className="stat-label">Verfügbarkeit</span>
                                </div>
                            </div>
                        </div>

                        <div className="sidebar-card verification-widget">
                            <h3>Verifizierungen</h3>
                            <div className="verification-list">
                                <div className="verification-item">
                                    <i className="fas fa-shield-alt verified"></i>
                                    <span>Identität verifiziert</span>
                                </div>
                                <div className="verification-item">
                                    <i className="fas fa-graduation-cap verified"></i>
                                    <span>Qualifikation geprüft</span>
                                </div>
                                <div className="verification-item">
                                    <i className="fas fa-briefcase verified"></i>
                                    <span>Berufserfahrung bestätigt</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdvisorProfilePage;