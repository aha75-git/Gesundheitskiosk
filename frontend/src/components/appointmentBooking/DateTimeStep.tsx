import React, { useState } from 'react';
import type {AdvisorAvailability} from '../../types/appointment/AppointmentTypes.ts';
import type {Advisor} from "../../types/advisor/AdvisorTypes.ts";

import './DateTimeStep.css';

interface DateTimeStepProps {
    advisor: Advisor;
    availability: AdvisorAvailability | null;
    selectedDateTime: string;
    duration: number;
    onDateTimeSelect: (datetime: string) => void;
    onAvailabilityUpdate: (date: string) => void;
}

const DateTimeStep: React.FC<DateTimeStepProps> = ({
                                                       advisor,
                                                       availability,
                                                       selectedDateTime,
                                                       duration,
                                                       onDateTimeSelect,
                                                       onAvailabilityUpdate
                                                   }) => {

    // availability?.availableSlots?.map(slots => {
    //     console.log('slots.start: ', slots.start);
    //     console.log('slots.end: ', slots.end);
    // });

    //console.log("availability?.availableSlots: " + availability?.availableSlots);

    const [selectedDate, setSelectedDate] = useState<string>(
        new Date().toISOString().split('T')[0]
    );

    const handleDateChange = (date: string) => {
        setSelectedDate(date);
        onAvailabilityUpdate(date);
    };

    const formatTimeSlot = (start: Date, end: Date) => {
        const startTime = new Date(start).toLocaleTimeString('de-DE', {
            hour: '2-digit',
            minute: '2-digit'
        });
        const endTime = new Date(end).toLocaleTimeString('de-DE', {
            hour: '2-digit',
            minute: '2-digit'
        });
        return `${startTime} - ${endTime}`;
    };

    // const formatTimeSlotByString = (start: string, end: string) => {
    //     const startTime = new Date(start).toLocaleTimeString('de-DE', {
    //         hour: '2-digit',
    //         minute: '2-digit'
    //     });
    //     const endTime = new Date(end).toLocaleTimeString('de-DE', {
    //         hour: '2-digit',
    //         minute: '2-digit'
    //     });
    //     return `${startTime} - ${endTime}`;
    // };

    const isTimeSlotAvailable = (slot: any) => {
        return slot.available;
    };

    const generateDateOptions = () => {
        const dates = [];
        const today = new Date();

        for (let i = 0; i < 14; i++) { // Nächste 14 Tage
            const date = new Date(today);
            date.setDate(today.getDate() + i);

            dates.push({
                date: date.toISOString().split('T')[0],
                label: date.toLocaleDateString('de-DE', {
                    weekday: 'short',
                    day: '2-digit',
                    month: '2-digit'
                }),
                isToday: i === 0
            });
        }

        return dates;
    };

    return (
        <div className="date-time-step">
            <div className="step-header">
                <h2>Termin auswählen</h2>
                <p>Wählen Sie einen passenden Termin für Ihre Beratung</p>
            </div>

            <div className="date-selection">
                <h3>Datum wählen</h3>
                <div className="date-options">
                    {generateDateOptions().map((dateOption) => (
                        <button
                            key={dateOption.date}
                            onClick={() => handleDateChange(dateOption.date)}
                            className={`date-option ${selectedDate === dateOption.date ? 'selected' : ''}`}
                        >
                            <span className="day">{dateOption.label.split(', ')[0]}</span>
                            <span className="date">{dateOption.label.split(', ')[1]}</span>
                            {dateOption.isToday && <span className="today-badge">Heute</span>}
                        </button>
                    ))}
                </div>
            </div>

            <div className="time-selection">
                <h3>Verfügbare Termine am {new Date(selectedDate).toLocaleDateString('de-DE')}</h3>

                {!availability ? (
                    <div className="loading-slots">
                        <i className="fas fa-spinner fa-spin"></i>
                        <p>Lade verfügbare Termine...</p>
                    </div>
                ) : availability.availableSlots.length === 0 ? (
                    <div className="no-slots">
                        <i className="fas fa-calendar-times"></i>
                        <p>Keine verfügbaren Termine an diesem Tag</p>
                        <button
                            onClick={() => handleDateChange(selectedDate)}
                            className="btn btn-secondary"
                        >
                            Anderen Tag wählen
                        </button>
                    </div>
                ) : (
                    <div className="time-slots-grid">
                        {availability.availableSlots
                            .filter(slot => isTimeSlotAvailable(slot))
                            .map((slot, index) => (
                                <button
                                    key={index}
                                    onClick={() => onDateTimeSelect(new Date(slot.start).toISOString())}
                                    className={`time-slot ${selectedDateTime === new Date(slot.start).toISOString()? 'selected' : ''}`}
                                >
                  <span className="time-range">
                    {formatTimeSlot(slot.start, slot.end)}
                  </span>
                                    <span className="duration">({duration} min)</span>
                                </button>
                            ))
                        }
                    </div>
                )}
            </div>

            {selectedDateTime && (
                <div className="selected-appointment">
                    <div className="selection-summary">
                        <i className="fas fa-check-circle"></i>
                        <div>
                            <strong>Ausgewählter Termin:</strong>
                            <span>
                {new Date(selectedDateTime).toLocaleDateString('de-DE', {
                    weekday: 'long',
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                })}
                                {' um '}
                                {new Date(selectedDateTime).toLocaleTimeString('de-DE', {
                                    hour: '2-digit',
                                    minute: '2-digit'
                                })}
                                {' Uhr'}
              </span>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default DateTimeStep;