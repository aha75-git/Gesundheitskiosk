import {useNavigate} from "react-router-dom";
import {useAuth} from "../api/AuthContext.tsx";
import {useEffect, useState} from "react";
import type {Advisor} from "../types/advisor/AdvisorTypes.ts";
import type {AdvisorAvailability, AppointmentBookingData} from "../types/appointment/AppointmentTypes.ts";
import {advisorService} from "../services/advisorService.ts";
import {appointmentService} from "../services/appointmentService.ts";
import DateTimeStep from "../components/appointmentBooking/DateTimeStep.tsx";

// type StatusUpdateProps = {
//     isStatusUpdated: boolean;
// }

export default function AppointmentPlanningPage() {
    // const {advisorId} = useParams<{advisorId: string}>();
    // const advisorId = props.advisorId;
    // const statusUpdate = props.isStatusUpdated;
    const { user } = useAuth();
    const navigate = useNavigate();

    const [advisor, setAdvisor] = useState<Advisor | null>(null);
    const [availability, setAvailability] = useState<AdvisorAvailability | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [advisorId, setAdvisorId] = useState<string | null>(null);

    // const [statusUpdate, setStatusUpdate] = useState<StatusUpdateProps>({
    //     isStatusUpdated: false,
    // });

    const [bookingData, setBookingData] = useState<Partial<AppointmentBookingData>>({
        advisorId: advisor?.id,
        type: undefined,
        scheduledAt: '',
        duration: 60,
        notes: '',
        symptoms: [],
        priority: 'ROUTINE'
    });

    useEffect(() => {
        if (!user) {
            navigate('/login');
            return;
        }

        if (user.role === "ADVISOR") {
            loadAdvisor();
        }

        if (advisorId) {
            loadAvailability();
        }
    }, [advisorId, user, navigate]);

    const loadAdvisor = async () => {
        try {
            setIsLoading(true);
            setError('');

            // Beraterdaten laden
            const advisorData = await advisorService.getAdvisorByUser();
            setAdvisor(advisorData);
            setAdvisorId(advisorData.id);
        } catch (err) {
            setError('Fehler beim Laden der Beraterdaten. Bitte versuchen Sie es erneut.');
            console.error('Error loading advisor data:', err);
        } finally {
            setIsLoading(false);
        }
    }

    const loadAvailability = async () => {
        try {
            setIsLoading(true);
            setError('');

            // // TODO löschen
            // console.log("loadAdvisorAndAvailability with advisorId: " + advisorId);
            //
            // // Beraterdaten laden
            // const advisorData = await advisorService.getAdvisorById(advisorId!);
            // setAdvisor(advisorData);
            //
            // // TODO löschen
            // console.log("loadAdvisorAndAvailability with advisorData: " + advisorData);

            // Verfügbarkeit für heute laden
            const today = new Date().toISOString().split('T')[0];
            const availabilityDataResponse = await appointmentService.getAdvisorAvailability(advisorId!, today);

            // TODO löschen
            console.log("loadAdvisorAndAvailability with availabilityDataResponse: " + availabilityDataResponse);

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

    const updateBookingData = (updates: Partial<AppointmentBookingData>) => {
        setBookingData(prev => ({ ...prev, ...updates }));
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
                            onClick={() => navigate('/dashboard')}
                            className="btn btn-primary"
                        >
                            Zurück zum Dashboard
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="appointment-wizard">

            {error && (
                <div className="alert alert-error">
                    <i className="fas fa-exclamation-circle"></i>
                    {error}
                </div>
            )}

            <div className="wizard-content">
                <DateTimeStep
                    advisor={advisor}
                    availability={availability}
                    selectedDateTime={bookingData.scheduledAt || ''}
                    duration={bookingData.duration || 60}
                    onDateTimeSelect={(datetime) => updateBookingData({ scheduledAt: datetime })}
                    onAvailabilityUpdate={handleAvailabilityUpdate}
                />
            </div>
        </div>
    );
}