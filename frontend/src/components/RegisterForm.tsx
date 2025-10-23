// components/RegisterForm.tsx
import React, {useState} from 'react';
import {type AuthResponse, UserRole} from "../types/types.ts";

interface RegisterFormProps {
    onRegisterSuccess: (authResponse: AuthResponse) => void;
}

interface RegisterData {
    username: string;
    email: string;
    password: string;
    confirmPassword: string;
    role: UserRole;
}

const RegisterForm: React.FC<RegisterFormProps> = ({ onRegisterSuccess }) => {
    const [formData, setFormData] = useState<RegisterData>({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: UserRole.USER
    });
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const validateForm = (): boolean => {
        if (formData.password !== formData.confirmPassword) {
            setError('Passwörter stimmen nicht überein');
            return false;
        }

        if (formData.password.length < 6) {
            setError('Passwort muss mindestens 6 Zeichen lang sein');
            return false;
        }

        if (!formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
            setError('Bitte geben Sie eine gültige E-Mail-Adresse ein');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (!validateForm()) return;

        setIsLoading(true);

        try {
            // Simulate API call
            await new Promise(resolve => setTimeout(resolve, 1500));

            // In a real application, this would be an API call to your backend
            // const response = await fetch('/api/auth/register', {
            //   method: 'POST',
            //   headers: { 'Content-Type': 'application/json' },
            //   body: JSON.stringify(formData)
            // });

            // if (!response.ok) throw new Error('Registration failed');
            // const authResponse: AuthResponse = await response.json();

            // Mock successful registration
            const mockAuthResponse: AuthResponse = {
                token: 'mock-jwt-token',
                type: 'bearer',
                user: {
                    id: '1',
                    username: formData.username,
                    email: formData.email,
                    role: formData.role,
                    createdAt: new Date().toISOString()
                }
            };

            onRegisterSuccess(mockAuthResponse);
        } catch (err) {
            console.error(err);
            setError('Registrierung fehlgeschlagen. Benutzername oder E-Mail bereits vergeben.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleGitHubRegister = () => {
        // In a real application, this would redirect to your backend OAuth endpoint
        window.location.href = 'http://localhost:8080/oauth2/authorization/github';
    };

    return (
        <div className="register-form">
            {error && (
                <div className="alert alert-error">
                    <i className="fas fa-exclamation-circle"></i>
                    {error}
                </div>
            )}

            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="register-username">Benutzername</label>
                    <input
                        type="text"
                        id="register-username"
                        className="form-control"
                        placeholder="Wählen Sie einen Benutzernamen"
                        value={formData.username}
                        onChange={(e) => setFormData({...formData, username: e.target.value})}
                        required
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="register-email">E-Mail</label>
                    <input
                        type="email"
                        id="register-email"
                        className="form-control"
                        placeholder="Ihre E-Mail-Adresse"
                        value={formData.email}
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                        required
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="register-password">Passwort</label>
                    <div className="password-input-container">
                        <input
                            type={showPassword ? "text" : "password"}
                            id="register-password"
                            className="form-control"
                            placeholder="Erstellen Sie ein sicheres Passwort"
                            value={formData.password}
                            onChange={(e) => setFormData({...formData, password: e.target.value})}
                            required
                            disabled={isLoading}
                        />
                        <button
                            type="button"
                            className="password-toggle"
                            onClick={() => setShowPassword(!showPassword)}
                        >
                            <i className={`far fa-${showPassword ? 'eye-slash' : 'eye'}`}></i>
                        </button>
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="register-confirm-password">Passwort bestätigen</label>
                    <div className="password-input-container">
                        <input
                            type={showConfirmPassword ? "text" : "password"}
                            id="register-confirm-password"
                            className="form-control"
                            placeholder="Wiederholen Sie Ihr Passwort"
                            value={formData.confirmPassword}
                            onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}
                            required
                            disabled={isLoading}
                        />
                        <button
                            type="button"
                            className="password-toggle"
                            onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                        >
                            <i className={`far fa-${showConfirmPassword ? 'eye-slash' : 'eye'}`}></i>
                        </button>
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="register-role">Rolle</label>
                    <select
                        id="register-role"
                        className="form-control"
                        value={formData.role}
                        onChange={(e) => setFormData({...formData, role: e.target.value as UserRole})}
                        disabled={isLoading}
                    >
                        <option value="USER">Benutzer</option>
                        <option value="ADVISOR">Beauftragte</option>
                        <option value="ADMIN">Administrator</option>
                    </select>
                </div>

                <button
                    type="submit"
                    className={`btn btn-primary ${isLoading ? 'loading' : ''}`}
                    disabled={isLoading}
                >
                    {isLoading ? (
                        <>
                            <i className="fas fa-spinner fa-spin"></i>
                            Wird verarbeitet...
                        </>
                    ) : (
                        'Konto erstellen'
                    )}
                </button>
            </form>

            <div className="divider">
                <span>oder</span>
            </div>

            <button
                className="btn btn-oauth"
                onClick={handleGitHubRegister}
                disabled={isLoading}
            >
                <i className="fab fa-github"></i>
                Mit GitHub registrieren
            </button>

            <div className="form-footer">
                Bereits ein Konto?{' '}
                <a href="#" className="switch-link">
                    Jetzt anmelden
                </a>
            </div>
        </div>
    );
};

export default RegisterForm;