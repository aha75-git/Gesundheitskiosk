import React, { useState } from 'react';
import type {WorkingHours} from '../../types/advisor/AdvisorTypes.ts';
import './WorkingHoursEditor.css';

interface WorkingHoursEditorProps {
    workingHours: WorkingHours[];
    onChange: (workingHours: WorkingHours[]) => void;
}

const WorkingHoursEditor: React.FC<WorkingHoursEditorProps> = ({
                                                                   workingHours,
                                                                   onChange
                                                               }) => {
    const [newWorkingHour, setNewWorkingHour] = useState<Omit<WorkingHours, 'available'>>({
        dayOfWeek: 'MONDAY',
        start: '09:00',
        end: '17:00'
    });

    const dayOptions = [
        { value: 'MONDAY', label: 'Montag' },
        { value: 'TUESDAY', label: 'Dienstag' },
        { value: 'WEDNESDAY', label: 'Mittwoch' },
        { value: 'THURSDAY', label: 'Donnerstag' },
        { value: 'FRIDAY', label: 'Freitag' },
        { value: 'SATURDAY', label: 'Samstag' },
        { value: 'SUNDAY', label: 'Sonntag' }
    ];

    const timeOptions = [
        '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00',
        '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00'
    ];

    const addWorkingHour = () => {
        const workingHour: WorkingHours = {
            ...newWorkingHour,
            available: true
        };

        // Prüfen, ob dieser Tag bereits existiert
        const exists = workingHours.some(wh =>
            wh.dayOfWeek === workingHour.dayOfWeek
        );

        if (exists) {
            alert('Für diesen Tag sind bereits Arbeitszeiten definiert.');
            return;
        }

        onChange([...workingHours, workingHour]);
        // Zurücksetzen auf Standardwerte
        setNewWorkingHour({
            dayOfWeek: 'MONDAY',
            start: '09:00',
            end: '17:00'
        });
    };

    const removeWorkingHour = (dayOfWeek: string) => {
        onChange(workingHours.filter(wh => wh.dayOfWeek !== dayOfWeek));
    };

    const toggleWorkingHourAvailability = (dayOfWeek: string) => {
        onChange(
            workingHours.map(wh =>
                wh.dayOfWeek === dayOfWeek
                    ? { ...wh, available: !wh.available }
                    : wh
            )
        );
    };

    const getGermanDayLabel = (dayOfWeek: string) => {
        const day = dayOptions.find(d => d.value === dayOfWeek);
        return day ? day.label : dayOfWeek;
    };

    const formatTimeDisplay = (time: string) => {
        return time.replace(/^0/, ''); // Führende Null entfernen für bessere Darstellung
    };

    return (
        <div className="working-hours-editor">
            <h3>Arbeitszeiten</h3>

            {/* Hinzufügen-Bereich */}
            <div className="working-hours-add">
                <div className="add-row">
                    <div className="form-group">
                        <label>Wochentag</label>
                        <select
                            value={newWorkingHour.dayOfWeek}
                            onChange={(e) => setNewWorkingHour({
                                ...newWorkingHour,
                                dayOfWeek: e.target.value as WorkingHours['dayOfWeek']
                            })}
                            className="form-control"
                        >
                            {dayOptions.map(day => (
                                <option key={day.value} value={day.value}>
                                    {day.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Von</label>
                        <select
                            value={newWorkingHour.start}
                            onChange={(e) => setNewWorkingHour({
                                ...newWorkingHour,
                                start: e.target.value
                            })}
                            className="form-control"
                        >
                            {timeOptions.map(time => (
                                <option key={time} value={time}>
                                    {formatTimeDisplay(time)} Uhr
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Bis</label>
                        <select
                            value={newWorkingHour.end}
                            onChange={(e) => setNewWorkingHour({
                                ...newWorkingHour,
                                end: e.target.value
                            })}
                            className="form-control"
                        >
                            {timeOptions.map(time => (
                                <option key={time} value={time}>
                                    {formatTimeDisplay(time)} Uhr
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group checkbox-group" id="div-form-group-working-hours-available">
                        <label className="checkbox-label" id="label-form-group-working-hoursdiv-form-group-working-hours">
                            <input
                                type="checkbox"
                                checked={true}
                                readOnly
                                className="form-checkbox"
                            />
                            {/*<span className="checkmark" id="check-working-hours-available"></span>*/}
                            <span id="text-working-hours-available">Verfügbar</span>
                        </label>
                    </div>

                    <div className="form-group" id="div-button-add-working-hours-available">
                        <button
                            type="button"
                            onClick={addWorkingHour}
                            className="btn-add-working-hours-editor"
                            title="Arbeitszeit hinzufügen"
                        >
                            <i className="fas fa-plus"></i>
                        </button>
                    </div>
                </div>
            </div>

            {/* Angezeigte Arbeitszeiten als Chips */}
            <div className="working-hours-chips">
                {workingHours.length === 0 ? (
                    <div className="no-working-hours">
                        <i className="fas fa-clock"></i>
                        <span>Keine Arbeitszeiten definiert</span>
                    </div>
                ) : (
                    workingHours.map((wh) => (
                        <div
                            key={wh.dayOfWeek} 
                            className={`working-hour-chip ${wh.available ? 'available' : 'unavailable'}`}
                        >
                            <div className="chip-content">
                                <span className="day">{getGermanDayLabel(wh.dayOfWeek)}</span>
                                <span className="time">
                                    {formatTimeDisplay(wh.start)} - {formatTimeDisplay(wh.end)} Uhr
                                </span>
                                <span className="availability">
                                    {wh.available ? '✓ Verfügbar' : '✗ Nicht verfügbar'}
                                </span>
                            </div>
                            <div className="chip-actions">
                                <button
                                    type="button"
                                    onClick={() => toggleWorkingHourAvailability(wh.dayOfWeek)}
                                    className={`btn-toggle ${wh.available ? 'available' : 'unavailable'}`}
                                    title={wh.available ? 'Als nicht verfügbar markieren' : 'Als verfügbar markieren'}
                                >
                                    <i className={`fas ${wh.available ? 'fa-eye-slash' : 'fa-eye'}`}></i>
                                </button>
                                <button
                                    type="button"
                                    onClick={() => removeWorkingHour(wh.dayOfWeek)}
                                    className="btn-remove"
                                    title="Entfernen"
                                >
                                    <i className="fas fa-times"></i>
                                </button>
                            </div>
                        </div>
                    ))
                )}
            </div>

            {/* Hilfetext */}
            <div className="working-hours-help">
                <i className="fas fa-info-circle"></i>
                <span>
                    Definieren Sie hier Ihre regulären Arbeitszeiten. Kunden können nur innerhalb dieser Zeiten Termine buchen.
                </span>
            </div>
        </div>
    );
};

export default WorkingHoursEditor;