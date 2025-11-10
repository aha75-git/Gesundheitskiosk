import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContext';
import { appointmentService } from '../services/appointmentService';
import type {AppointmentResponse, AppointmentStatus} from '../types/appointment/AppointmentTypes.ts';
import AppointmentCard from '../components/appointmentBooking/AppointmentCard';
import AppointmentFilter from '../components/appointmentBooking/AppointmentFilter';
import './MyAppointmentsPage.css';

const MyAppointmentsPage: React.FC = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [appointments, setAppointments] = useState<AppointmentResponse[]>([]);
    const [filteredAppointments, setFilteredAppointments] = useState<AppointmentResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [filter, setFilter] = useState<{
        status: AppointmentStatus | 'ALL';
        dateRange: 'upcoming' | 'past' | 'all';
        type: string;
    }>({
        status: 'ALL',
        dateRange: 'upcoming',
        type: 'ALL'
    });

    useEffect(() => {
        if (!user) {
            navigate('/login');
            return;
        }
        loadAppointments();
    }, [user, navigate]);

    const loadAppointments = async () => {
        try {
            setIsLoading(true);
            setError('');
            const appointmentsData = await appointmentService.getMyAppointments();
            setAppointments(appointmentsData);
            applyFilters(appointmentsData, filter);
        } catch (err) {
            setError('Fehler beim Laden der Termine. Bitte versuchen Sie es erneut.');
            console.error('Error loading appointments:', err);
        } finally {
            setIsLoading(false);
        }
    };

    const applyFilters = (appointmentsList: AppointmentResponse[], currentFilter: typeof filter) => {
        let filtered = appointmentsList;

        // Status-Filter
        if (currentFilter.status !== 'ALL') {
            filtered = filtered.filter(appointment => appointment.status === currentFilter.status);
        }

        // Datums-Filter
        const now = new Date();
        if (currentFilter.dateRange === 'upcoming') {
            filtered = filtered.filter(appointment => new Date(appointment.scheduledAt) >= now);
        } else if (currentFilter.dateRange === 'past') {
            filtered = filtered.filter(appointment => new Date(appointment.scheduledAt) < now);
        }

        // Typ-Filter
        if (currentFilter.type !== 'ALL') {
            filtered = filtered.filter(appointment => appointment.type === currentFilter.type);
        }

        // Sortiere Termine: Bevorstehende zuerst, dann nach Datum
        filtered.sort((a, b) => {
            const dateA = new Date(a.scheduledAt);
            const dateB = new Date(b.scheduledAt);
            return dateA.getTime() - dateB.getTime();
        });

        setFilteredAppointments(filtered);
    };

    useEffect(() => {
        applyFilters(appointments, filter);
    }, [appointments, filter]);

    const handleFilterChange = (newFilter: Partial<typeof filter>) => {
        setFilter(prev => ({ ...prev, ...newFilter }));
    };

    const handleStatusUpdate = async (appointmentId: string, newStatus: AppointmentStatus) => {
        try {
            setError('');
            await appointmentService.updateAppointmentStatus(appointmentId, { status: newStatus });

            // Aktualisiere lokalen State
            setAppointments(prev =>
                prev.map(appointment =>
                    appointment.id === appointmentId
                        ? { ...appointment, status: newStatus }
                        : appointment
                )
            );
        } catch (err) {
            setError('Fehler beim Aktualisieren des Termins. Bitte versuchen Sie es erneut.');
            console.error('Error updating appointment status:', err);
        }
    };

    const handleCancelAppointment = async (appointmentId: string) => {
        if (window.confirm('Möchten Sie diesen Termin wirklich stornieren?')) {
            try {
                setError('');
                await appointmentService.cancelAppointment(appointmentId);

                // Entferne Termin aus der Liste
                setAppointments(prev => prev.filter(appointment => appointment.id !== appointmentId));
            } catch (err) {
                setError('Fehler beim Stornieren des Termins. Bitte versuchen Sie es erneut.');
                console.error('Error canceling appointment:', err);
            }
        }
    };

    if (!user) {
        return null;
    }

    const upcomingCount = appointments.filter(app => new Date(app.scheduledAt) >= new Date()).length;
    const pastCount = appointments.filter(app => new Date(app.scheduledAt) < new Date()).length;

    return (
        <div className="my-appointments-page">
            <div className="container">
                <div className="appointments-header">
                    <h1>Meine Termine</h1>
                    <p>Verwalten Sie Ihre geplanten und vergangenen Termine</p>
                </div>

                {error && (
                    <div className="alert alert-error">
                        <i className="fas fa-exclamation-circle"></i>
                        {error}
                    </div>
                )}

                <div className="appointments-stats">
                    <div className="stat-card">
                        <div className="stat-icon upcoming">
                            <i className="fas fa-calendar-check"></i>
                        </div>
                        <div className="stat-info">
                            <h3>{upcomingCount}</h3>
                            <p>Bevorstehende Termine</p>
                        </div>
                    </div>
                    <div className="stat-card">
                        <div className="stat-icon completed">
                            <i className="fas fa-history"></i>
                        </div>
                        <div className="stat-info">
                            <h3>{pastCount}</h3>
                            <p>Vergangene Termine</p>
                        </div>
                    </div>
                    <div className="stat-card">
                        <div className="stat-icon total">
                            <i className="fas fa-list-alt"></i>
                        </div>
                        <div className="stat-info">
                            <h3>{appointments.length}</h3>
                            <p>Gesamte Termine</p>
                        </div>
                    </div>
                </div>

                <AppointmentFilter
                    filter={filter}
                    onFilterChange={handleFilterChange}
                    appointmentCount={filteredAppointments.length}
                />

                {isLoading ? (
                    <div className="loading-container">
                        <div className="loading-spinner">
                            <i className="fas fa-spinner fa-spin"></i>
                            <p>Lade Termine...</p>
                        </div>
                    </div>
                ) : filteredAppointments.length === 0 ? (
                    <div className="empty-state">
                        <i className="fas fa-calendar-times"></i>
                        <h3>Keine Termine gefunden</h3>
                        <p>
                            {appointments.length === 0
                                ? "Sie haben noch keine Termine gebucht. Starten Sie jetzt mit der Beratersuche!"
                                : "Keine Termine entsprechen den aktuellen Filtereinstellungen."
                            }
                        </p>
                        {user.role !== "ADVISOR" && (
                            <button
                                onClick={() => navigate('/search')}
                                className="btn btn-primary"
                            >
                                <i className="fa-solid fa-magnifying-glass btn-advisor-search"></i>
                                Berater suchen
                            </button>
                        )}
                    </div>
                ) : (
                    <div className="appointments-grid">
                        {filteredAppointments.map(appointment => (
                            <AppointmentCard
                                key={appointment.id}
                                appointment={appointment}
                                onStatusUpdate={handleStatusUpdate}
                                onCancel={handleCancelAppointment}
                            />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default MyAppointmentsPage;