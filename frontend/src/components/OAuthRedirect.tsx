import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const OAuthRedirect: React.FC = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');
        const username = urlParams.get('username');
        const role = urlParams.get('role');

        if (token && username && role) {
            localStorage.setItem('token', token);
            localStorage.setItem('user', JSON.stringify({
                username,
                role,
                email: '' // OAuth might not provide email
            }));
            navigate('/dashboard');
        } else {
            navigate('/login');
        }
    }, [navigate]);

    return <div>Processing OAuth login...</div>;
};

export default OAuthRedirect;