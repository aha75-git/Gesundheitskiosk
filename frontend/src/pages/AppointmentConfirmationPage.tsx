import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import {type Appointment} from '../types/appointment/AppointmentTypes.ts';
import type {Advisor} from '../types/advisor/AdvisorTypes.ts';
import { appointmentService } from '../services/appointmentService';
import './AppointmentConfirmationPage.css';
import {mapAppointmentResponseToAppointment} from "../utils/maps/maps.ts";

interface LocationState {
    appointment: Appointment;
    advisor: Advisor;
}

const AppointmentConfirmationPage: React.FC = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [appointment, setAppointment] = useState<Appointment | null>(null);
    const [advisor, setAdvisor] = useState<Advisor | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const state = location.state as LocationState;

        if (state?.appointment && state?.advisor) {
            setAppointment(state.appointment);
            setAdvisor(state.advisor);
            setIsLoading(false);
        } else {
            // Falls keine Daten im state sind, versuchen wir die appointmentId aus der URL zu holen
            const urlParams = new URLSearchParams(location.search);
            const appointmentId = urlParams.get('id');

            if (appointmentId) {
                loadAppointmentData(appointmentId);
            } else {
                setError('Keine Termindaten gefunden.');
                setIsLoading(false);
            }
        }
    }, [location]);

    const loadAppointmentData = async (appointmentId: string) => {
        try {
            setIsLoading(true);
            const appointmentDataResponse = await appointmentService.getAppointmentById(appointmentId);
            const appointmentData = mapAppointmentResponseToAppointment(appointmentDataResponse);
            const advisorData = await appointmentService.getAdvisorById(appointmentData.advisorId);

            setAppointment(appointmentData);
            setAdvisor(advisorData);
        } catch (err) {
            setError('Termin konnte nicht geladen werden.');
            console.error('Error loading appointment:', err);
        } finally {
            setIsLoading(false);
        }
    };



    const formatDate = (dateString: string) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('de-DE', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    };

    const formatTime = (dateString: string) => {
        const date = new Date(dateString);
        return date.toLocaleTimeString('de-DE', {
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const getAppointmentTypeName = (type: string) => {
        const types: { [key: string]: string } = {
            'VIDEO_CALL': 'Video-Call',
            'PHONE_CALL': 'Telefonat',
            'IN_PERSON': 'Persönlicher Termin',
            'CHAT': 'Chat-Beratung'
        };
        return types[type] || type;
    };

    const handleAddToCalendar = () => {
        if (!appointment) return;

        const startTime = new Date(appointment.scheduledAt);
        const endTime = new Date(startTime.getTime() + (appointment.durationMinutes || 60) * 60000);

        // Google Calendar Link
        const googleCalendarUrl = `https://calendar.google.com/calendar/render?action=TEMPLATE&text=Beratungstermin mit ${advisor?.name}&dates=${startTime.toISOString().replace(/[-:]/g, '').split('.')[0]}/${endTime.toISOString().replace(/[-:]/g, '').split('.')[0]}&details=Beratungstermin mit ${advisor?.name}%0A%0ATyp: ${getAppointmentTypeName(appointment.type)}%0AThemen: ${appointment.symptoms?.join(', ')}&location=${appointment.type === 'VIDEO_CALL' ? 'Video-Call' : appointment.type === 'PHONE_CALL' ? 'Telefonat' : 'Vor Ort'}`;

        window.open(googleCalendarUrl, '_blank');
    };

    const handleDownloadICS = () => {
        if (!appointment || !advisor) return;

        const startTime = new Date(appointment.scheduledAt);
        const endTime = new Date(startTime.getTime() + (appointment.durationMinutes || 60) * 60000);

        const icsContent = `BEGIN:VCALENDAR
VERSION:2.0
BEGIN:VEVENT
SUMMARY:Beratungstermin mit ${advisor.name}
DTSTART:${startTime.toISOString().replace(/[-:]/g, '').split('.')[0]}Z
DTEND:${endTime.toISOString().replace(/[-:]/g, '').split('.')[0]}Z
DESCRIPTION:Beratungstermin mit ${advisor.name}\\nTyp: ${getAppointmentTypeName(appointment.type)}\\nThemen: ${appointment.symptoms?.join(', ')}
LOCATION:${appointment.type === 'VIDEO_CALL' ? 'Video-Call' : appointment.type === 'PHONE_CALL' ? 'Telefonat' : 'Vor Ort'}
END:VEVENT
END:VCALENDAR`;

        const blob = new Blob([icsContent], { type: 'text/calendar;charset=utf-8' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = `Termin-${advisor.name}-${startTime.toISOString().split('T')[0]}.ics`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };

    if (isLoading) {
        return (
            <div className="confirmation-page">
                <div className="container">
                    <div className="loading-container">
                        <div className="loading-spinner">
                            <i className="fas fa-spinner fa-spin"></i>
                            <p>Lade Termininformationen...</p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    if (error || !appointment || !advisor) {
        return (
            <div className="confirmation-page">
                <div className="container">
                    <div className="error-state">
                        <i className="fas fa-exclamation-triangle"></i>
                        <h3>Termin nicht gefunden</h3>
                        <p>{error || 'Der angeforderte Termin konnte nicht geladen werden.'}</p>
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
        <div className="confirmation-page">
            <div className="container">
                <div className="confirmation-header">
                    <div className="success-icon">
                        <i className="fas fa-check-circle"></i>
                    </div>
                    <h1>Termin erfolgreich gebucht!</h1>
                    <p>Ihr Beratungstermin wurde reserviert. Sie erhalten in Kürze eine Bestätigungs-E-Mail.</p>
                </div>

                <div className="confirmation-content">
                    <div className="appointment-details">
                        <div className="details-card">
                            <h2>Terminübersicht</h2>

                            <div className="detail-section">
                                <h3>Berater</h3>
                                <div className="advisor-info confirmation-advisor-info">
                                    {advisor.image ? (
                                        <img src={advisor.image} alt={advisor.name} className="advisor-avatar confirmation-advisor-avatar" />
                                    ) : (
                                        <div className="advisor-avatar-placeholder">
                                            <i className="fas fa-user-md"></i>
                                        </div>
                                    )}
                                    <div className="advisor-details confirmation-advisor-details">
                                        <h4>{advisor.name}</h4>
                                        <p className="specialization">{advisor.specialization}</p>
                                        <div className="advisor-meta">
                      <span className="rating">
                        <i className="fas fa-star"></i>
                          {advisor.rating?.toFixed(1)}
                      </span>
                                            <span className="experience">
                        <i className="fas fa-briefcase"></i>
                                                {advisor.experience} Jahre Erfahrung
                      </span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="detail-section">
                                <h3>Termininformationen</h3>
                                <div className="info-grid">
                                    <div className="info-item">
                                        <span className="label">Datum:</span>
                                        <span className="value">{formatDate(new Date(appointment.scheduledAt).toISOString())}</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="label">Uhrzeit:</span>
                                        <span className="value">{formatTime(new Date(appointment.scheduledAt).toISOString())} Uhr</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="label">Dauer:</span>
                                        <span className="value">{appointment.durationMinutes} Minuten</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="label">Art der Beratung:</span>
                                        <span className="value">{getAppointmentTypeName(appointment.type)}</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="label">Notizen:</span>
                                        <span className="value">{appointment.notes}</span>
                                    </div>
                                    {/*<div className="info-item">*/}
                                    {/*    <span className="label">Termin-ID:</span>*/}
                                    {/*    <span className="value">{appointment.id}</span>*/}
                                    {/*</div>*/}
                                </div>
                            </div>

                            {appointment.symptoms && appointment.symptoms.length > 0 && (
                                <div className="detail-section">
                                    <h3>Besprochene Themen</h3>
                                    <div className="symptoms-list">
                                        {appointment.symptoms.map((symptom, index) => (
                                            <div key={index} className="symptom-tag">
                                                <i className="fas fa-check"></i>
                                                {symptom}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}

                            {appointment.notes && (
                                <div className="detail-section">
                                    <h3>Ihre Notizen</h3>
                                    <div className="notes-preview">
                                        <p>{appointment.notes}</p>
                                    </div>
                                </div>
                            )}

                            <div className="detail-section">
                                <h3>Vorbereitungshinweise</h3>
                                <div className="preparation-tips">
                                    {appointment.type === 'VIDEO_CALL' && (
                                        <div className="tip">
                                            <i className="fas fa-video"></i>
                                            <div>
                                                <strong>Video-Call Vorbereitung:</strong>
                                                <ul>
                                                    <li>Stellen Sie eine stabile Internetverbindung sicher</li>
                                                    <li>Testen Sie Kamera und Mikrofon vorher</li>
                                                    <li>Wählen Sie einen ruhigen, privaten Ort</li>
                                                    <li>Der Meeting-Link wird 15 Minuten vor Termin per E-Mail versendet</li>
                                                </ul>
                                            </div>
                                        </div>
                                    )}

                                    {appointment.type === 'PHONE_CALL' && (
                                        <div className="tip">
                                            <i className="fas fa-phone"></i>
                                            <div>
                                                <strong>Telefonat Vorbereitung:</strong>
                                                <ul>
                                                    <li>Ihr Berater ruft Sie zur vereinbarten Zeit an</li>
                                                    <li>Bitte halten Sie Ihre Telefonnummer bereit</li>
                                                    <li>Wählen Sie einen ungestörten Gesprächsort</li>
                                                </ul>
                                            </div>
                                        </div>
                                    )}

                                    {appointment.type === 'IN_PERSON' && (
                                        <div className="tip">
                                            <i className="fas fa-map-marker-alt"></i>
                                            <div>
                                                <strong>Persönlicher Termin:</strong>
                                                <ul>
                                                    <li>Adresse: [Berater-Adresse]</li>
                                                    <li>Bitte seien Sie 5-10 Minuten vor Termin vor Ort</li>
                                                    <li>Parkmöglichkeiten: [Hinweise]</li>
                                                    <li>ÖPNV: [Hinweise]</li>
                                                </ul>
                                            </div>
                                        </div>
                                    )}

                                    {appointment.type === 'CHAT' && (
                                        <div className="tip">
                                            <i className="fas fa-comments"></i>
                                            <div>
                                                <strong>Chat-Beratung:</strong>
                                                <ul>
                                                    <li>Loggen Sie sich zur vereinbarten Zeit im Portal ein</li>
                                                    <li>Der Chat-Raum wird automatisch geöffnet</li>
                                                    <li>Sie können den Chat nach dem Termin exportieren</li>
                                                </ul>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="action-sidebar">
                        <div className="sidebar-card">
                            <h3>Termin verwalten</h3>

                            <div className="calendar-actions">
                                <button
                                    onClick={handleAddToCalendar}
                                    className="btn btn-secondary btn-full"
                                >
                                    <i className="fas fa-calendar-plus"></i>
                                    Zu Google Calendar hinzufügen
                                </button>

                                <button
                                    onClick={handleDownloadICS}
                                    className="btn btn-outline btn-full"
                                >
                                    <i className="fas fa-download"></i>
                                    ICS-Datei herunterladen
                                </button>
                            </div>

                            <div className="appointment-actions">
                                <Link
                                    to={`/advisor/${advisor.id}`}
                                    className="btn btn-outline btn-full"
                                >
                                    <i className="fas fa-user-md"></i>
                                    Beraterprofil ansehen
                                </Link>

                                <button
                                    onClick={() => navigate('/appointments')}
                                    className="btn btn-outline btn-full"
                                >
                                    <i className="fas fa-list"></i>
                                    Zu meinen Terminen
                                </button>

                                <button
                                    onClick={() => navigate('/search')}
                                    className="btn btn-primary btn-full"
                                >
                                    <i className="fas fa-plus"></i>
                                    Neuen Termin buchen
                                </button>
                            </div>
                        </div>

                        <div className="sidebar-card">
                            <h3>Support</h3>
                            <div className="support-info">
                                <p>Fragen zu Ihrem Termin?</p>
                                <div className="support-actions">
                                    <button className="btn btn-outline btn-small">
                                        <i className="fas fa-envelope"></i>
                                        Nachricht senden
                                    </button>
                                    <button className="btn btn-outline btn-small">
                                        <i className="fas fa-phone"></i>
                                        Support anrufen
                                    </button>
                                </div>
                                <div className="support-contact">
                                    <p><strong>Support-Hotline:</strong> 0800 123 456 789</p>
                                    <p><strong>E-Mail:</strong> support@beratung.de</p>
                                </div>
                            </div>
                        </div>

                        <div className="sidebar-card cancellation-info">
                            <h3>Stornierungsbedingungen</h3>
                            <ul>
                                <li>Kostenfreie Stornierung bis 24 Stunden vor Termin</li>
                                <li>Bei Verspätung: Bitte informieren Sie Ihren Berater</li>
                                <li>Notfall-Stornierung: Support kontaktieren</li>
                                <li>Wiederholtes Nichterscheinen kann zu Account-Einschränkungen führen</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AppointmentConfirmationPage;