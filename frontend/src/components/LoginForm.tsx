// components/LoginForm.tsx
import React, { useState} from 'react';
import {type AuthResponse} from "../types/types.ts";
// import axios from "axios";

interface LoginFormProps {
    onLoginSuccess: (authResponse: AuthResponse) => void;
}

interface LoginData {
    email: string;
    password: string;
}

const LoginForm: React.FC<LoginFormProps> = ({ onLoginSuccess }) => {
    const [formData, setFormData] = useState<LoginData>({
        email: '',
        password: ''
    });
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            // Simulate API call
            // await new Promise(resolve => setTimeout(resolve, 1500));

            // Mock successful login
            // const mockAuthResponse: AuthResponse = {
            //     token: 'mock-jwt-token',
            //     type: 'bearer',
            //     user: {
            //         id: '1',
            //         username: formData.username,
            //         email: `${formData.username}@example.com`,
            //         role: UserRole.USER,
            //         createdAt: new Date().toISOString()
            //     }
            // };

            // onLoginSuccess(mockAuthResponse);

            // In a real application, this would be an API call to your backend
            const response = await fetch('/api/v1/users/login', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(formData)
            });

            if (!response.ok) {
                throw new Error('Login failed');
            }
            const authResponse: AuthResponse = await response.json();
            console.log(authResponse);

            //localStorage.removeItem('auth_mode')
            //localStorage.setItem('token', authResponse.token)

            onLoginSuccess(authResponse);
        } catch (err) {
            console.error(err);
            setError('Anmeldung fehlgeschlagen. Bitte überprüfen Sie Ihre Daten.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleGitHubLogin = () => {
        // In a real application, this would redirect to your backend OAuth endpoint
        //window.location.href = 'http://localhost:8080/oauth2/authorization/github';

        const host:string = window.location.host === "localhost:5173" ? "http://localhost:8080" : window.location.origin;
        window.open(host + "/oauth2/authorization/github", "_self" );
    };

    // const loadUser = () => {
    //     axios.get("/api/v1/auth/me")
    //         .then(res => {
    //             onLoginSuccess(res.data);
    //             // setUser(res.data);
    //             console.log(res.data);
    //         })
    //         .catch(err => {
    //             // setUser(null);
    //             console.log(err);
    //         });
    // }
    //
    // useEffect(() => {
    //     loadUser();
    // }, []);

    return (
        <div className="login-form">
            {error && (
                <div className="alert alert-error">
                    <i className="fas fa-exclamation-circle"></i>
                    {error}
                </div>
            )}

            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="login-username">Benutzername oder E-Mail</label>
                    <input
                        type="text"
                        id="login-username"
                        className="form-control"
                        placeholder="Ihr Benutzername oder E-Mail"
                        value={formData.email}
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                        required
                        disabled={isLoading}
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

                <div className="form-options">
                    <div className="remember-me">
                        <input type="checkbox" id="remember-me" />
                        <label htmlFor="remember-me">Angemeldet bleiben</label>
                    </div>
                    <a href="#" className="forgot-password">Passwort vergessen?</a>
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
                        'Anmelden'
                    )}
                </button>
            </form>

            <div className="divider">
                <span>oder</span>
            </div>

            <button
                className="btn btn-oauth"
                onClick={handleGitHubLogin}
                disabled={isLoading}
            >
                <i className="fab fa-github"></i>
                Mit GitHub anmelden
            </button>

            <div className="form-footer">
                Noch kein Konto?{' '}
                <a href="#" className="switch-link">
                    Jetzt registrieren
                </a>
            </div>
        </div>
    );
};

export default LoginForm;