import React, { useState } from 'react';
import { useAuth } from '../api/AuthContext';
import './LoginModal.css';

interface LoginModalProps {
    isOpen: boolean;
    onClose: () => void;
    onGitHubLogin: () => void;
}

const LoginModal: React.FC<LoginModalProps> = ({ isOpen, onClose, onGitHubLogin }) => {
    const { login, register } = useAuth();
    const [activeTab, setActiveTab] = useState<'login' | 'register'>('login');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const [loginData, setLoginData] = useState({
        email: '',
        password: ''
    });

    const [registerData, setRegisterData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: 'USER' as 'USER' | 'ADVISOR' | 'ADMIN'
    });

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            await login(loginData);
            onClose();
            setLoginData({ email: '', password: '' });
        } catch (err) {
            console.error(err);
            setError('Anmeldung fehlgeschlagen. Bitte überprüfen Sie Ihre Daten.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (registerData.password !== registerData.confirmPassword) {
            setError('Passwörter stimmen nicht überein');
            return;
        }

        if (registerData.password.length < 6) {
            setError('Passwort muss mindestens 6 Zeichen lang sein');
            return;
        }

        setIsLoading(true);

        try {
            await register(registerData);
            onClose();
            setRegisterData({
                username: '',
                email: '',
                password: '',
                confirmPassword: '',
                role: 'USER'
            });
        } catch (err) {
            console.error(err);
            setError('Registrierung fehlgeschlagen. Benutzername oder E-Mail bereits vergeben.');
        } finally {
            setIsLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                <button className="modal-close" onClick={onClose}>
                    <i className="fas fa-times"></i>
                </button>

                <div className="auth-header">
                    <h2>Willkommen zurück</h2>
                    <p>Melden Sie sich an oder erstellen Sie ein Konto</p>
                </div>

                <div className="auth-tabs">
                    <button
                        className={`tab ${activeTab === 'login' ? 'active' : ''}`}
                        onClick={() => setActiveTab('login')}
                    >
                        <i className="fas fa-sign-in-alt"></i>
                        Anmelden
                    </button>
                    <button
                        className={`tab ${activeTab === 'register' ? 'active' : ''}`}
                        onClick={() => setActiveTab('register')}
                    >
                        <i className="fas fa-user-plus"></i>
                        Registrieren
                    </button>
                </div>

                {error && (
                    <div className="alert alert-error">
                        <i className="fas fa-exclamation-circle"></i>
                        {error}
                    </div>
                )}

                <div className="auth-body">
                    {activeTab === 'login' ? (
                        <form onSubmit={handleLogin} className="auth-form">
                            <div className="form-group">
                                <label htmlFor="login-username">Benutzername oder E-Mail</label>
                                <input
                                    type="text"
                                    id="login-username"
                                    value={loginData.email}
                                    onChange={(e) => setLoginData({...loginData, email: e.target.value})}
                                    required
                                    disabled={isLoading}
                                    placeholder="Ihr Benutzername oder E-Mail"
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="login-password">Passwort</label>
                                <div className="password-input-container">
                                    <input
                                        type={showPassword ? "text" : "password"}
                                        id="login-password"
                                        className="form-control"
                                        placeholder="Ihr Passwort"
                                        value={loginData.password}
                                        onChange={(e) => setLoginData({...loginData, password: e.target.value})}
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
                    ) : (
                        <form onSubmit={handleRegister} className="auth-form">
                            <div className="form-group">
                                <label htmlFor="register-username">Benutzername</label>
                                <input
                                    type="text"
                                    id="register-username"
                                    value={registerData.username}
                                    onChange={(e) => setRegisterData({...registerData, username: e.target.value})}
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
                                    value={registerData.email}
                                    onChange={(e) => setRegisterData({...registerData, email: e.target.value})}
                                    required
                                    disabled={isLoading}
                                    placeholder="Ihre E-Mail-Adresse"
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="register-password">Passwort</label>
                                <div className="password-input-container">
                                    <input
                                        type={showPassword ? "text" : "password"}
                                        id="register-password"
                                        className="form-control"
                                        placeholder="Mindestens 6 Zeichen"
                                        value={registerData.password}
                                        onChange={(e) => setRegisterData({...registerData, password: e.target.value})}
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
                                        value={registerData.confirmPassword}
                                        onChange={(e) => setRegisterData({...registerData, confirmPassword: e.target.value})}
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
                                    value={registerData.role}
                                    onChange={(e) => setRegisterData({...registerData, role: e.target.value as any})}
                                    disabled={isLoading}
                                >
                                    <option value="USER">Benutzer</option>
                                    <option value="ADVISOR">Beauftragte</option>
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
                    )}

                    <div className="oauth-section">
                        <div className="divider">
                            <span>oder</span>
                        </div>

                        <button
                            onClick={onGitHubLogin}
                            className="btn btn-github btn-full"
                            disabled={isLoading}
                        >
                            <i className="fab fa-github"></i>
                            Mit GitHub {activeTab === 'login' ? 'anmelden' : 'registrieren'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginModal;