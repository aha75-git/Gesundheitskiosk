import { Navigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContext';
import LoginForm from '../components/forms/LoginForm.tsx';
import './AuthPages.css';

export default function LoginPage() {
    const { user } = useAuth();

    if (user) {
        return <Navigate to="/dashboard" replace />;
    }

    return (
        <div className="auth-page">
            <div className="auth-container">
                <div className="auth-card">
                    <div className="card-header">
                        <h1>Willkommen zurück</h1>
                        <p>Melden Sie sich an, um auf Ihr Konto zuzugreifen</p>
                    </div>
                    <div className="card-body">
                        <LoginForm />
                    </div>
                </div>
            </div>
        </div>
    );
};
