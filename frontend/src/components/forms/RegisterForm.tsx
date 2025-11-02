import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../api/AuthContext.tsx';
import './AuthForms.css';
import type {RegisterRequest} from "../../types/user/UserTypes.ts";

export default function  RegisterForm(){
    const { register } = useAuth();
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: 'USER' as 'USER' | 'ADVISOR' | 'ADMIN'
    });

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

        if (!validateForm()) {
            return;
        }

        setIsLoading(true);

        const registerRequest: RegisterRequest = {
            role: formData.role,
            email: formData.email,
            username: formData.username,
            password: formData.password
        };

        try {
            await register(registerRequest);
            navigate('/dashboard');
        } catch (err) {
            console.log(err);
            setError('Registrierung fehlgeschlagen. Benutzername oder E-Mail bereits vergeben.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleGitHubLogin = () => {
        // window.location.href = 'http://localhost:8080/oauth2/authorization/github';
        const host:string = window.location.host === "localhost:5173" ? "http://localhost:8080" : window.location.origin;
        window.open(host + "/oauth2/authorization/github", "_self" );
    };

    return (
        <div className="auth-form-container">
            {error && (
                <div className="alert alert-error">
                    <i className="fas fa-exclamation-circle"></i>
                    {error}
                </div>
            )}

            <form onSubmit={handleSubmit} className="auth-form">
                <div className="form-group">
                    <label htmlFor="register-username">Benutzername</label>
                    <input
                        type="text"
                        id="register-username"
                        value={formData.username}
                        onChange={(e) => setFormData({...formData, username: e.target.value})}
                        required
                        disabled={isLoading}
                        placeholder="Wählen Sie einen Benutzernamen"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="register-email">E-Mail</label>
                    <input
                        type="email"
                        id="register-email"
                        value={formData.email}
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                        required
                        disabled={isLoading}
                        placeholder="Ihre E-Mail-Adresse"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="register-password">Passwort</label>
                    <input
                        type="password"
                        id="register-password"
                        value={formData.password}
                        onChange={(e) => setFormData({...formData, password: e.target.value})}
                        required
                        disabled={isLoading}
                        placeholder="Mindestens 6 Zeichen"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="register-confirm-password">Passwort bestätigen</label>
                    <input
                        type="password"
                        id="register-confirm-password"
                        value={formData.confirmPassword}
                        onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}
                        required
                        disabled={isLoading}
                        placeholder="Wiederholen Sie Ihr Passwort"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="register-role">Rolle</label>
                    <select
                        id="register-role"
                        value={formData.role}
                        onChange={(e) => setFormData({...formData, role: e.target.value as any})}
                        disabled={isLoading}
                    >
                        <option value="USER">Benutzer</option>
                        <option value="MODERATOR">Moderator</option>
                        <option value="ADMIN">Administrator</option>
                    </select>
                </div>

                <button
                    type="submit"
                    disabled={isLoading}
                    className="btn btn-primary btn-full"
                >
                    {isLoading ? (
                        <>
                            <i className="fas fa-spinner fa-spin"></i>
                            Wird verarbeitet...
                        </>
                    ) : (
                        <>
                            <i className="fas fa-user-plus"></i>
                            Konto erstellen
                        </>
                    )}
                </button>
            </form>

            <div className="oauth-section">
                <div className="divider">
                    <span>oder</span>
                </div>

                <button
                    onClick={handleGitHubLogin}
                    className="btn btn-github btn-full"
                    disabled={isLoading}
                >
                    <i className="fab fa-github"></i>
                    Mit GitHub registrieren
                </button>
            </div>

            <div className="auth-footer">
                Bereits ein Konto? <Link to="/login">Jetzt anmelden</Link>
            </div>
        </div>
    );
};
