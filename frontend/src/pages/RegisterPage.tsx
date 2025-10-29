import { Navigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContext';
import RegisterForm from '../components/RegisterForm';
import './AuthPages.css';

export default function RegisterPage(){
    const { user } = useAuth();

    if (user) {
        return <Navigate to="/dashboard" replace />;
    }

    return (
        <div className="auth-page">
            <div className="auth-container">
                <div className="auth-card">
                    <div className="card-header">
                        <h1>Konto erstellen</h1>
                        <p>Registrieren Sie sich für ein neues Konto</p>
                    </div>
                    <div className="card-body">
                        <RegisterForm />
                    </div>
                </div>
            </div>
        </div>
    );
};
