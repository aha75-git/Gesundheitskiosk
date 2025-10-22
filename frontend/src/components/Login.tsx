import React, { useState } from 'react';
import { useAuth } from '../api/AuthContext';
import type {LoginRequest} from '../types/types.ts';

const Login: React.FC = () => {
    const { login } = useAuth();
    const [formData, setFormData] = useState<LoginRequest>({
        username: '',
        password: ''
    });
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            await login(formData);
        } catch (err) {
            setError('Login failed. Please check your credentials. ' + err);
        }
    };

    const handleGitHubLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/github';
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            {error && <div className="error">{error}</div>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={formData.username}
                        onChange={(e) => setFormData({...formData, username: e.target.value})}
                        required
                    />
                </div>

                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={formData.password}
                        onChange={(e) => setFormData({...formData, password: e.target.value})}
                        required
                    />
                </div>

                <button type="submit">Login</button>
            </form>

            <div className="oauth-section">
                <button onClick={handleGitHubLogin} className="github-btn">
                    Login with GitHub
                </button>
            </div>
        </div>
    );
};

export default Login;