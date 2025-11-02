import React, { useState } from 'react';
import type {
    UserProfile,
    ProfileRequest,
    PersonalData,
    // MedicalInfo,
    ContactInfo,
    Address,
    EmergencyContact,
    Medication, User
} from '../../types/user/UserTypes.ts';
import './ProfileForm.css';

interface ProfileFormProps {
    profile: UserProfile | null;
    user: User;
    onSave: (profileData: ProfileRequest) => void;
}

export default function ProfileForm(props: Readonly<ProfileFormProps>) {
    const {
        profile,
        user,
        onSave } = props;

    const [activeSection, setActiveSection] = useState<'personal' | 'contact' | 'medical' | 'professional'>('personal');
    const [isLoading, setIsLoading] = useState(false);

    // Initialdaten für das Formular
    const [formData, setFormData] = useState<ProfileRequest>({
        username: user.username,
        email: user.email,
        personalData: profile?.personalData || {
            firstName: '',
            lastName: '',
            dateOfBirth: '',
            gender: ''
        },
        medicalInfo: profile?.medicalInfo || {
            bloodType: '',
            allergies: [],
            chronicConditions: [],
            currentMedications: [],
            emergencyContact: {
                name: '',
                phone: '',
                relationship: ''
            }
        },
        contactInfo: profile?.contactInfo || {
            phone: '',
            address: {
                street: '',
                city: '',
                postalCode: '',
                country: '',
                houseNumber: ''
            },
            allowHouseVisits: false
        },
        languages: profile?.languages || [],
        specialization: profile?.specialization || '',
        bio: profile?.bio || '',
        qualification: profile?.qualification || ''
    });

    const [newAllergy, setNewAllergy] = useState('');
    const [newCondition, setNewCondition] = useState('');
    const [newLanguage, setNewLanguage] = useState('');
    const [newMedication, setNewMedication] = useState<Medication>({
        name: '',
        dosage: '',
        frequency: ''
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        await onSave(formData);
        setIsLoading(false);
    };

    // TODO
    const handleUserNameChange = (value: string) => {
        setFormData(prev => ({
            ...prev,
            username: value
        }));
    };

    const handleEmailChange = (value: string) => {
        setFormData(prev => ({
            ...prev,
            email: value
        }));
    };

    const handlePersonalDataChange = (field: keyof PersonalData, value: string) => {
        setFormData(prev => ({
            ...prev,
            personalData: {
                ...prev.personalData,
                [field]: value
            }
        }));
    };

    const handleContactInfoChange = (field: keyof ContactInfo, value: any) => {
        setFormData(prev => ({
            ...prev,
            contactInfo: {
                ...prev.contactInfo,
                [field]: value
            }
        }));
    };

    const handleAddressChange = (field: keyof Address, value: string) => {
        setFormData(prev => ({
            ...prev,
            contactInfo: {
                ...prev.contactInfo,
                address: {
                    ...prev.contactInfo.address,
                    [field]: value
                }
            }
        }));
    };

    const handleEmergencyContactChange = (field: keyof EmergencyContact, value: string) => {
        setFormData(prev => ({
            ...prev,
            medicalInfo: {
                ...prev.medicalInfo,
                emergencyContact: {
                    ...prev.medicalInfo.emergencyContact,
                    [field]: value
                }
            }
        }));
    };

    const addAllergy = () => {
        if (newAllergy.trim()) {
            setFormData(prev => ({
                ...prev,
                medicalInfo: {
                    ...prev.medicalInfo,
                    allergies: [...prev.medicalInfo.allergies, newAllergy.trim()]
                }
            }));
            setNewAllergy('');
        }
    };

    const removeAllergy = (index: number) => {
        setFormData(prev => ({
            ...prev,
            medicalInfo: {
                ...prev.medicalInfo,
                allergies: prev.medicalInfo.allergies.filter((_, i) => i !== index)
            }
        }));
    };

    const addCondition = () => {
        if (newCondition.trim()) {
            setFormData(prev => ({
                ...prev,
                medicalInfo: {
                    ...prev.medicalInfo,
                    chronicConditions: [...prev.medicalInfo.chronicConditions, newCondition.trim()]
                }
            }));
            setNewCondition('');
        }
    };

    const removeCondition = (index: number) => {
        setFormData(prev => ({
            ...prev,
            medicalInfo: {
                ...prev.medicalInfo,
                chronicConditions: prev.medicalInfo.chronicConditions.filter((_, i) => i !== index)
            }
        }));
    };

    const addLanguage = () => {
        if (newLanguage.trim()) {
            setFormData(prev => ({
                ...prev,
                languages: [...prev.languages, newLanguage.trim()]
            }));
            setNewLanguage('');
        }
    };

    const removeLanguage = (index: number) => {
        setFormData(prev => ({
            ...prev,
            languages: prev.languages.filter((_, i) => i !== index)
        }));
    };

    const addMedication = () => {
        if (newMedication.name.trim()) {
            setFormData(prev => ({
                ...prev,
                medicalInfo: {
                    ...prev.medicalInfo,
                    currentMedications: [...prev.medicalInfo.currentMedications, { ...newMedication }]
                }
            }));
            setNewMedication({ name: '', dosage: '', frequency: '' });
        }
    };

    const removeMedication = (index: number) => {
        setFormData(prev => ({
            ...prev,
            medicalInfo: {
                ...prev.medicalInfo,
                currentMedications: prev.medicalInfo.currentMedications.filter((_, i) => i !== index)
            }
        }));
    };

    return (
        <div className="profile-form-container">
            <div className="profile-sidebar">
                <button
                    className={`sidebar-item ${activeSection === 'personal' ? 'active' : ''}`}
                    onClick={() => setActiveSection('personal')}
                >
                    <i className="fas fa-user"></i>
                    Persönliche Daten
                </button>

                <button
                    className={`sidebar-item ${activeSection === 'contact' ? 'active' : ''}`}
                    onClick={() => setActiveSection('contact')}
                >
                    <i className="fas fa-address-book"></i>
                    Kontaktdaten
                </button>

                <button
                    className={`sidebar-item ${activeSection === 'medical' ? 'active' : ''}`}
                    onClick={() => setActiveSection('medical')}
                >
                    <i className="fas fa-heartbeat"></i>
                    Medizinische Informationen
                </button>

                {(user.role === 'ADVISOR' || user.role === 'ADMIN') && (
                    <button
                        className={`sidebar-item ${activeSection === 'professional' ? 'active' : ''}`}
                        onClick={() => setActiveSection('professional')}
                    >
                        <i className="fas fa-briefcase"></i>
                        Berufliche Informationen
                    </button>
                )}
            </div>

            <div className="profile-content">
                <form onSubmit={handleSubmit}>
                    {/* Persönliche Daten */}
                    {activeSection === 'personal' && (
                        <div className="form-section">
                            <h3>Persönliche Daten</h3>

                            <div className="form-grid">

                                {user.role === 'ADMIN' && (
                                    <>
                                        <div className="form-group">
                                            <label>Username *</label>
                                            <input
                                                type="text"
                                                value={formData.username}
                                                onChange={(e) => handleUserNameChange(e.target.value)}
                                                required/>
                                        </div>
                                        <div className="form-group">
                                            <label>Email *</label>
                                            <input
                                                type="text"
                                                value={formData.email}
                                                onChange={(e) => handleEmailChange(e.target.value)}
                                                required/>
                                        </div>
                                    </>
                                )}

                                <div className="form-group">
                                    <label>Vorname *</label>
                                    <input
                                        type="text"
                                        value={formData.personalData.firstName}
                                        onChange={(e) => handlePersonalDataChange('firstName', e.target.value)}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label>Nachname *</label>
                                    <input
                                        type="text"
                                        value={formData.personalData.lastName}
                                        onChange={(e) => handlePersonalDataChange('lastName', e.target.value)}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label>Geburtsdatum *</label>
                                    <input
                                        type="date"
                                        value={formData.personalData.dateOfBirth}
                                        onChange={(e) => handlePersonalDataChange('dateOfBirth', e.target.value)}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label>Geschlecht *</label>
                                    <select
                                        value={formData.personalData.gender}
                                        onChange={(e) => handlePersonalDataChange('gender', e.target.value)}
                                        required
                                    >
                                        <option value="">Bitte wählen</option>
                                        <option value="MALE">Männlich</option>
                                        <option value="FEMALE">Weiblich</option>
                                        <option value="DIVERS">Divers</option>
                                        <option value="NOT_SPECIFIED">Nicht angegeben</option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-group">
                                <label>Sprachen</label>
                                <div className="array-input-group">
                                    <div className="input-with-button">
                                        <input
                                            type="text"
                                            value={newLanguage}
                                            onChange={(e) => setNewLanguage(e.target.value)}
                                            placeholder="Neue Sprache hinzufügen"
                                        />
                                        <button type="button" onClick={addLanguage} className="btn-add">
                                            <i className="fas fa-plus"></i>
                                        </button>
                                    </div>

                                    <div className="array-items">
                                        {formData.languages.map((language, index) => (
                                            <div key={index} className="array-item">
                                                <span>{language}</span>
                                                <button
                                                    type="button"
                                                    onClick={() => removeLanguage(index)}
                                                    className="btn-remove"
                                                >
                                                    <i className="fas fa-times"></i>
                                                </button>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* Kontaktdaten */}
                    {activeSection === 'contact' && (
                        <div className="form-section">
                            <h3>Kontaktdaten</h3>

                            <div className="form-group">
                                <label>Telefon *</label>
                                <input
                                    type="tel"
                                    value={formData.contactInfo.phone}
                                    onChange={(e) => handleContactInfoChange('phone', e.target.value)}
                                    required
                                />
                            </div>

                            <div className="address-section">
                                <h4>Adresse</h4>
                                <div className="form-grid">
                                    <div className="form-group">
                                        <label>Straße *</label>
                                        <input
                                            type="text"
                                            value={formData.contactInfo.address.street}
                                            onChange={(e) => handleAddressChange('street', e.target.value)}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Hausnummer *</label>
                                        <input
                                            type="text"
                                            value={formData.contactInfo.address.houseNumber}
                                            onChange={(e) => handleAddressChange('houseNumber', e.target.value)}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Stadt *</label>
                                        <input
                                            type="text"
                                            value={formData.contactInfo.address.city}
                                            onChange={(e) => handleAddressChange('city', e.target.value)}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Postleitzahl *</label>
                                        <input
                                            type="text"
                                            value={formData.contactInfo.address.postalCode}
                                            onChange={(e) => handleAddressChange('postalCode', e.target.value)}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Land *</label>
                                        <input
                                            type="text"
                                            value={formData.contactInfo.address.country}
                                            onChange={(e) => handleAddressChange('country', e.target.value)}
                                            required
                                        />
                                    </div>
                                </div>
                            </div>

                            <div className="form-group checkbox-group">
                                <label className="checkbox-label">
                                    <input
                                        type="checkbox"
                                        checked={formData.contactInfo.allowHouseVisits}
                                        onChange={(e) => handleContactInfoChange('allowHouseVisits', e.target.checked)}
                                    />
                                    <span className="checkmark"></span>
                                    <span className="span-of-checkbox">Hausbesuche erlauben</span>
                                </label>
                            </div>
                        </div>
                    )}

                    {/* Medizinische Informationen */}
                    {activeSection === 'medical' && (
                        <div className="form-section">
                            <h3>Medizinische Informationen</h3>

                            <div className="form-group">
                                <label>Blutgruppe</label>
                                <select
                                    value={formData.medicalInfo.bloodType}
                                    onChange={(e) => setFormData(prev => ({
                                        ...prev,
                                        medicalInfo: {
                                            ...prev.medicalInfo,
                                            bloodType: e.target.value
                                        }
                                    }))}
                                >
                                    <option value="">Bitte wählen</option>
                                    <option value="A_POSITIVE">A+</option>
                                    <option value="A_NEGATIVE">A-</option>
                                    <option value="B_POSITIVE">B+</option>
                                    <option value="B_NEGATIVE">B-</option>
                                    <option value="AB_POSITIVE">AB+</option>
                                    <option value="AB_NEGATIVE">AB-</option>
                                    <option value="O_POSITIVE">O+</option>
                                    <option value="O_NEGATIVE">O-</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label>Allergien</label>
                                <div className="array-input-group">
                                    <div className="input-with-button">
                                        <input
                                            type="text"
                                            value={newAllergy}
                                            onChange={(e) => setNewAllergy(e.target.value)}
                                            placeholder="Neue Allergie hinzufügen"
                                        />
                                        <button type="button" onClick={addAllergy} className="btn-add">
                                            <i className="fas fa-plus"></i>
                                        </button>
                                    </div>

                                    <div className="array-items">
                                        {formData.medicalInfo.allergies.map((allergy, index) => (
                                            <div key={index} className="array-item">
                                                <span>{allergy}</span>
                                                <button
                                                    type="button"
                                                    onClick={() => removeAllergy(index)}
                                                    className="btn-remove"
                                                >
                                                    <i className="fas fa-times"></i>
                                                </button>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>

                            <div className="form-group">
                                <label>Chronische Erkrankungen</label>
                                <div className="array-input-group">
                                    <div className="input-with-button">
                                        <input
                                            type="text"
                                            value={newCondition}
                                            onChange={(e) => setNewCondition(e.target.value)}
                                            placeholder="Neue Erkrankung hinzufügen"
                                        />
                                        <button type="button" onClick={addCondition} className="btn-add">
                                            <i className="fas fa-plus"></i>
                                        </button>
                                    </div>

                                    <div className="array-items">
                                        {formData.medicalInfo.chronicConditions.map((condition, index) => (
                                            <div key={index} className="array-item">
                                                <span>{condition}</span>
                                                <button
                                                    type="button"
                                                    onClick={() => removeCondition(index)}
                                                    className="btn-remove"
                                                >
                                                    <i className="fas fa-times"></i>
                                                </button>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>

                            <div className="form-group">
                                <label>Aktuelle Medikamente</label>
                                <div className="medication-form">
                                    <div className="form-grid">
                                        <div className="form-group">
                                            <input
                                                type="text"
                                                value={newMedication.name}
                                                onChange={(e) => setNewMedication(prev => ({ ...prev, name: e.target.value }))}
                                                placeholder="Medikamentenname"
                                            />
                                        </div>

                                        <div className="form-group">
                                            <input
                                                type="text"
                                                value={newMedication.dosage}
                                                onChange={(e) => setNewMedication(prev => ({ ...prev, dosage: e.target.value }))}
                                                placeholder="Dosierung"
                                            />
                                        </div>

                                        <div className="form-group">
                                            <input
                                                type="text"
                                                value={newMedication.frequency}
                                                onChange={(e) => setNewMedication(prev => ({ ...prev, frequency: e.target.value }))}
                                                placeholder="Einnahmehäufigkeit"
                                            />
                                        </div>
                                    </div>

                                    <button type="button" onClick={addMedication} className="btn btn-secondary">
                                        <i className="fas fa-plus"></i>
                                        Medikament hinzufügen
                                    </button>

                                    <div className="medication-list">
                                        {formData.medicalInfo.currentMedications.map((medication, index) => (
                                            <div key={index} className="medication-item">
                                                <div className="medication-info">
                                                    <strong>{medication.name}</strong>
                                                    <span>{medication.dosage} - {medication.frequency}</span>
                                                </div>
                                                <button
                                                    type="button"
                                                    onClick={() => removeMedication(index)}
                                                    className="btn-remove"
                                                >
                                                    <i className="fas fa-times"></i>
                                                </button>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>

                            <div className="emergency-contact">
                                <h4>Notfallkontakt</h4>
                                <div className="form-grid">
                                    <div className="form-group">
                                        <label>Name *</label>
                                        <input
                                            type="text"
                                            value={formData.medicalInfo.emergencyContact.name}
                                            onChange={(e) => handleEmergencyContactChange('name', e.target.value)}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Telefon *</label>
                                        <input
                                            type="tel"
                                            value={formData.medicalInfo.emergencyContact.phone}
                                            onChange={(e) => handleEmergencyContactChange('phone', e.target.value)}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label>Beziehung *</label>
                                        <input
                                            type="text"
                                            value={formData.medicalInfo.emergencyContact.relationship}
                                            onChange={(e) => handleEmergencyContactChange('relationship', e.target.value)}
                                            required
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* Berufliche Informationen (nur für ADVISOR/ADMIN) */}
                    {activeSection === 'professional' && (user.role === 'ADVISOR' || user.role === 'ADMIN') && (
                        <div className="form-section">
                            <h3>Berufliche Informationen</h3>

                            <div className="form-group">
                                <label>Spezialisierung</label>
                                <input
                                    type="text"
                                    value={formData.specialization || ''}
                                    onChange={(e) => setFormData(prev => ({ ...prev, specialization: e.target.value }))}
                                    placeholder="Ihre Spezialisierung"
                                />
                            </div>

                            <div className="form-group">
                                <label>Qualifikation</label>
                                <input
                                    type="text"
                                    value={formData.qualification || ''}
                                    onChange={(e) => setFormData(prev => ({ ...prev, qualification: e.target.value }))}
                                    placeholder="Ihre Qualifikationen"
                                />
                            </div>

                            <div className="form-group">
                                <label>Über mich</label>
                                <textarea
                                    value={formData.bio || ''}
                                    onChange={(e) => setFormData(prev => ({ ...prev, bio: e.target.value }))}
                                    placeholder="Erzählen Sie etwas über sich..."
                                    rows={4}
                                />
                            </div>
                        </div>
                    )}

                    <div className="form-actions">
                        <button
                            type="submit"
                            className="btn btn-primary"
                            disabled={isLoading}
                        >
                            {isLoading ? (
                                <>
                                    <i className="fas fa-spinner fa-spin"></i>
                                    Wird gespeichert...
                                </>
                            ) : (
                                <>
                                    <i className="fas fa-save"></i>
                                    Profil speichern
                                </>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};
