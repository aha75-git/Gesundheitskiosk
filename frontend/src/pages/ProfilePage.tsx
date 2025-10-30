// pages/ProfilePage.tsx
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContext';
import { profileService } from '../services/profileService';
import type {UserProfile, ProfileRequest, ProfileResponse} from '../types/types.ts';
import ProfileForm from '../components/forms/ProfileForm';
import './ProfilePage.css';

export default function ProfilePage() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [profile, setProfile] = useState<UserProfile | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        if (!user) {
            navigate('/login');
            return;
        }
        loadProfile();
    }, [user, navigate]);

    const loadProfile = async () => {
        try {
            setIsLoading(true);
            const profileResponse = await profileService.getProfile();
            const profileData = profileResponse.userProfile;
            setProfile(profileData);
        } catch (err) {
            console.error(err);
            console.log('Profil nicht gefunden, erstelle neues Profil');
            // Profil existiert noch nicht - das ist okay
        } finally {
            setIsLoading(false);
        }
    };

    const handleSaveProfile = async (profileData: ProfileRequest) => {
        try {
            setError('');
            setSuccess('');

            let updatedProfileResponse: ProfileResponse;
            if (profile) {
                // Bestehendes Profil aktualisieren
                updatedProfileResponse = await profileService.updateProfile(profileData);
            } else {
                // Neues Profil erstellen
                updatedProfileResponse = await profileService.createProfile(profileData);
            }

            setProfile(updatedProfileResponse.userProfile);
            setSuccess('Profil erfolgreich gespeichert!');

            // Erfolgsmeldung nach 3 Sekunden ausblenden
            setTimeout(() => setSuccess(''), 3000);
        } catch (err) {
            console.error(err);
            setError('Fehler beim Speichern des Profils. Bitte versuchen Sie es erneut.');
        }
    };

    if (!user) {
        return null;
    }

    if (isLoading) {
        return (
            <div className="profile-page">
                <div className="container">
                    <div className="loading-spinner">
                        <i className="fas fa-spinner fa-spin"></i>
                        <p>Lade Profil...</p>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="profile-page">
            <div className="container">
                <div className="profile-header">
                    <h1>Profil bearbeiten</h1>
                    <p>Verwalten Sie Ihre persönlichen Daten und Einstellungen</p>
                </div>

                {error && (
                    <div className="alert alert-error">
                        <i className="fas fa-exclamation-circle"></i>
                        {error}
                    </div>
                )}

                {success && (
                    <div className="alert alert-success">
                        <i className="fas fa-check-circle"></i>
                        {success}
                    </div>
                )}

                <ProfileForm
                    profile={profile}
                    user={user}
                    onSave={handleSaveProfile}
                />
            </div>
        </div>
    );
};
