import { useAuth } from '../api/AuthContext';
import { useNavigate } from 'react-router-dom';
import './Dashboard.css';

export default function Dashboard() {
    const { user } = useAuth();
    const navigate = useNavigate();

    if (!user) {
        navigate('/');
        return null;
    }

    return (
        <div className="dashboard">
            <div className="dashboard-header">
                <div className="container">
                    <h1>Willkommen, {user.username}!</h1>
                    <p>Sie haben sich erfolgreich angemeldet.</p>
                </div>
            </div>

            <div className="dashboard-content">
                <div className="container">
                    <div className="dashboard-grid">
                        <div className="dashboard-card">
                            <div className="card-header">
                                <h2>Kontoinformationen</h2>
                            </div>
                            <div className="card-body">
                                <div className="info-grid">
                                    <div className="info-item">
                                        <label>Benutzername:</label>
                                        <span>{user.username}</span>
                                    </div>
                                    <div className="info-item">
                                        <label>E-Mail:</label>
                                        <span>{user.email}</span>
                                    </div>
                                    <div className="info-item">
                                        <label>Rolle:</label>
                                        <span className={`role-badge role-${user.role.toLowerCase()}`}>
                      {user.role}
                    </span>
                                    </div>
                                    <div className="info-item">
                                        <label>Mitglied seit:</label>
                                        <span>{new Date(user.createdAt).toLocaleDateString('de-DE')}</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="dashboard-card">
                            <div className="card-header">
                                <h2>Schnellaktionen</h2>
                            </div>
                            <div className="card-body">
                                <div className="actions-grid">
                                    <button
                                        onClick={() => navigate('/search')}
                                        className="action-btn"
                                    >
                                        <i className="fas fa-search"></i>
                                        <span>Berater suchen</span>
                                    </button>
                                    <button className="action-btn">
                                        <i className="fas fa-calendar-alt"></i>
                                        <span>Meine Termine</span>
                                    </button>
                                    <button className="action-btn">
                                        <i className="fas fa-user-edit"></i>
                                        <span>Profil bearbeiten</span>
                                    </button>
                                    <button className="action-btn">
                                        <i className="fa-solid fa-headset"></i>
                                        <span>Chat</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
