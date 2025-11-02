import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../api/AuthContext.tsx';
import './AuthForms.css';
import type {LoginRequest} from "../../types/user/UserTypes.ts";

export default function LoginForm() {
    const { login } = useAuth();
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const [formData, setFormData] = useState<LoginRequest>({
        email: '',
        password: ''
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            await login(formData);
            navigate('/dashboard');
        } catch (err) {
            console.log(err);
            setError('Anmeldung fehlgeschlagen. Bitte überprüfen Sie Ihre Daten.');
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
                    <label htmlFor="login-username">Benutzername oder E-Mail</label>
                    <input
                        type="text"
                        id="login-username"
                        value={formData.email}
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                        required
                        disabled={isLoading}
                        placeholder="Ihr Benutzername oder E-Mail"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="login-password">Passwort</label>
                    <input
                        type="password"
                        id="login-password"
                        value={formData.password}
                        onChange={(e) => setFormData({...formData, password: e.target.value})}
                        required
                        disabled={isLoading}
                        placeholder="Ihr Passwort"
                    />
                </div>

                <div className="form-options">
                    <label className="checkbox">
                        <input type="checkbox" />
                        <span>Angemeldet bleiben</span>
                    </label>
                    <Link to="/forgot-password" className="forgot-password">
                        Passwort vergessen?
                    </Link>
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
                            <i className="fas fa-sign-in-alt"></i>
                            Anmelden
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
                    Mit GitHub anmelden
                </button>
            </div>

            <div className="auth-footer">
                Noch kein Konto? <Link to="/register">Jetzt registrieren</Link>
            </div>
        </div>
    );
};
