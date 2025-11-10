import React from 'react';
import type {AppointmentStatus} from '../../types/appointment/AppointmentTypes.ts';
import './AppointmentFilter.css';

interface AppointmentFilterProps {
    filter: {
        status: AppointmentStatus | 'ALL';
        dateRange: 'upcoming' | 'past' | 'all';
        type: string;
    };
    onFilterChange: (filter: Partial<AppointmentFilterProps['filter']>) => void;
    appointmentCount: number;
}

const AppointmentFilter: React.FC<AppointmentFilterProps> = ({
                                                                 filter,
                                                                 onFilterChange,
                                                                 appointmentCount
                                                             }) => {
    const statusOptions: { value: AppointmentStatus | 'ALL'; label: string }[] = [
        { value: 'ALL', label: 'Alle Status' },
        { value: 'REQUESTED', label: 'Angefragt' },
        { value: 'SCHEDULED', label: 'Geplant' },
        { value: 'CONFIRMED', label: 'Bestätigt' },
        { value: 'IN_PROGRESS', label: 'In Bearbeitung' },
        { value: 'COMPLETED', label: 'Abgeschlossen' },
        { value: 'CANCELLED', label: 'Storniert' },
        { value: 'NO_SHOW', label: 'Nicht Erschienen' }
    ];

    const typeOptions: { value: string; label: string }[] = [
        { value: 'ALL', label: 'Alle Typen' },
        { value: 'VIDEO_CALL', label: 'Video-Call' },
        { value: 'PHONE_CALL', label: 'Telefonat' },
        { value: 'IN_PERSON', label: 'Persönlich' },
        { value: 'CHAT', label: 'Chat' }
    ];

    const dateRangeOptions: { value: 'upcoming' | 'past' | 'all'; label: string }[] = [
        { value: 'upcoming', label: 'Bevorstehende' },
        { value: 'past', label: 'Vergangene' },
        { value: 'all', label: 'Alle Termine' }
    ];

    return (
        <div className="appointment-filter">
            <div className="filter-header">
                <h3>Filter</h3>
                <span className="results-count appointment-filter-results-count">{appointmentCount} Termine gefunden</span>
            </div>

            <div className="filter-grid">
                <div className="filter-group">
                    <label>Zeitraum</label>
                    <select
                        value={filter.dateRange}
                        onChange={(e) => onFilterChange({ dateRange: e.target.value as any })}
                    >
                        {dateRangeOptions.map(option => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="filter-group">
                    <label>Status</label>
                    <select
                        value={filter.status}
                        onChange={(e) => onFilterChange({ status: e.target.value as any })}
                    >
                        {statusOptions.map(option => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="filter-group">
                    <label>Terminart</label>
                    <select
                        value={filter.type}
                        onChange={(e) => onFilterChange({ type: e.target.value })}
                    >
                        {typeOptions.map(option => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
            </div>
        </div>
    );
};

export default AppointmentFilter;