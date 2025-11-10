// components/AppointmentTypeSelector.tsx
import React from 'react';
import type {AppointmentType as AppointmentTypeEnum} from '../../types/appointment/AppointmentTypes.ts';
import { appointmentService } from '../../services/appointmentService';
import AppointmentTypeCard from './AppointmentTypeCard';
import './AppointmentTypeSelector.css';

interface AppointmentTypeSelectorProps {
    selectedType: AppointmentTypeEnum | null;
    onTypeSelect: (type: AppointmentTypeEnum) => void;
    onBookAppointment: (type: AppointmentTypeEnum) => void;
    showBookButtons?: boolean;
    layout?: 'grid' | 'list';
}

const AppointmentTypeSelector: React.FC<AppointmentTypeSelectorProps> = ({
                                                                             selectedType,
                                                                             onTypeSelect,
                                                                             onBookAppointment,
                                                                             showBookButtons = true,
                                                                             layout = 'grid'
                                                                         }) => {
    const appointmentTypes = appointmentService.getAppointmentTypes();

    if (layout === 'list') {
        return (
            <div className="appointment-type-selector list-layout">
                {appointmentTypes.map((type) => (
                    <AppointmentTypeCard
                        key={type.type}
                        type={type}
                        selected={selectedType === type.type}
                        onSelect={onTypeSelect}
                        onBook={onBookAppointment}
                        showBookButton={showBookButtons}
                        compact={true}
                    />
                ))}
            </div>
        );
    }

    return (
        <div className="appointment-type-selector grid-layout">
            {appointmentTypes.map((type) => (
                <AppointmentTypeCard
                    key={type.type}
                    type={type}
                    selected={selectedType === type.type}
                    onSelect={onTypeSelect}
                    onBook={onBookAppointment}
                    showBookButton={showBookButtons}
                />
            ))}
        </div>
    );
};

export default AppointmentTypeSelector;