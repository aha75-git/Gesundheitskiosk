import React from 'react';
import type {WorkingHours} from '../../types/advisor/AdvisorTypes.ts';
import './WorkingHoursDisplay.css';

interface WorkingHoursDisplayProps {
    workingHours: WorkingHours[];
}

const WorkingHoursDisplay: React.FC<WorkingHoursDisplayProps> = ({ workingHours }) => {
    const dayOrder = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

    const getGermanDayLabel = (dayOfWeek: string) => {
        const days: { [key: string]: string } = {
            'MONDAY': 'Montag',
            'TUESDAY': 'Dienstag',
            'WEDNESDAY': 'Mittwoch',
            'THURSDAY': 'Donnerstag',
            'FRIDAY': 'Freitag',
            'SATURDAY': 'Samstag',
            'SUNDAY': 'Sonntag'
        };
        return days[dayOfWeek] || dayOfWeek;
    };

    const formatTime = (time: string) => {
        return time.replace(/^0/, '');
    };

    const sortedWorkingHours = [...workingHours].sort((a, b) =>
        dayOrder.indexOf(a.dayOfWeek) - dayOrder.indexOf(b.dayOfWeek)
    );

    return (
        <div className="working-hours-display">
            <div className="working-hours-grid">
                {sortedWorkingHours.map((wh) => (
                    <div
                        key={wh.dayOfWeek}
                        className={`working-hour-item ${wh.available ? 'available' : 'unavailable'}`}
                    >
                        <div className="day-info">
                            <span className="day-name">{getGermanDayLabel(wh.dayOfWeek)}</span>
                            {wh.available ? (
                                <span className="time-range">
                  {formatTime(wh.start)} - {formatTime(wh.end)} Uhr
                </span>
                            ) : (
                                <span className="not-available">Nicht verfügbar</span>
                            )}
                        </div>
                        <div className="availability-status">
                            <i className={`fas fa-circle ${wh.available ? 'available' : 'unavailable'}`}></i>
                            <span>{wh.available ? 'Verfügbar' : 'Nicht verfügbar'}</span>
                        </div>
                    </div>
                ))}

                {workingHours.length === 0 && (
                    <div className="no-working-hours">
                        <i className="fas fa-clock"></i>
                        <p>Keine Arbeitszeiten definiert</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default WorkingHoursDisplay;