import React, {useEffect, useState} from 'react';
import { useAuth } from '../api/AuthContext';
import type {LoginRequest, User} from '../types/types.ts';
import axios from "axios";

const Login: React.FC = () => {
    const { login } = useAuth();
    const [formData, setFormData] = useState<LoginRequest>({
        email: '',
        password: ''
    });
    const [error, setError] = useState('');
    const [user, setUser] = useState<User | undefined | null>(undefined);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            await login(formData);
        } catch (err) {
            console.log('Login failed. Please check your credentials. \n' + err);
            setError('Login failed. Please check your credentials.');
        }
    };

    const handleGitHubLogin = () => {
        //window.location.href = 'http://localhost:8080/oauth2/authorization/github';

        const host:string = window.location.host === "localhost:5173" ? "http://localhost:8080" : window.location.origin;
        window.open(host + "/oauth2/authorization/github", "_self" );
    };

    const loadUser = () => {
        axios.get("/api/v1/auth/me")
            .then((res) => {
                setUser(res.data);
                console.log(res.data);
                console.log(user);
            })
            .catch((err) => {
                setUser(null);
                console.log(err);
            })
    }

    useEffect(() => {
        loadUser();
    }, []);

    return (
        <div className="login-container">
            <h2>Login</h2>
            {error && <div className="error">{error}</div>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label>E-Mail:</label>
                    <input
                        type="text"
                        value={formData.email}
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
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