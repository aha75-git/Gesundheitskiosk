import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../../api/AuthContext.tsx';
import './OAuthRedirect.css';
import {useEffect} from "react";

export default function OAuthRedirect() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { user } = useAuth();

    useEffect(() => {
        // Process OAuth response and redirect to dashboard
        const token = searchParams.get('token');
        const username = searchParams.get('username');
        const role = searchParams.get('role');
        const createdAt = searchParams.get('createdAt');
        const id = searchParams.get('id');

        if (token && username && role) {
            localStorage.setItem('token', token);
            localStorage.setItem('user', JSON.stringify({
                id,
                username,
                email: '', // OAuth might not provide email
                role,
                createdAt
            }));

            // If we have a token or user is already set, go to dashboard
            navigate('/dashboard', { replace: true });
        } else {
            // If OAuth failed, go to login
            navigate('/login', { replace: true });
        }
    }, [searchParams, navigate, user]);

    return (
        <div className="oauth-redirect">
            <div className="oauth-container">
                <div className="oauth-content">
                    <div className="spinner"></div>
                    <h2>OAuth-Anmeldung wird verarbeitet...</h2>
                    <p>Bitte warten Sie, während wir Sie anmelden.</p>
                </div>
            </div>
        </div>
    );
};
