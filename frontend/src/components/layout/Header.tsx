import React, {useState} from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../api/AuthContext.tsx';
import LoginModal from '../forms/LoginModal.tsx';
import './Header.css';

const Header: React.FC = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    const handleGitHubLogin = () => {
        // window.location.href = 'http://localhost:8080/oauth2/authorization/github';
        const host:string = window.location.host === "localhost:5173" ? "http://localhost:8080" : window.location.origin;
        window.open(host + "/oauth2/authorization/github", "_self" );
    };

    const isActiveLink = (path: string) => {
        return location.pathname === path ? 'active' : '';
    };

    return (
        <>
            <nav className="navbar">
                <div className="nav-container">
                    <Link to="/" className="nav-logo">
                        <i className="fa-solid fa-hand-holding-medical"></i>
                        <span>Gesundheitskiosk</span>
                    </Link>

                    <div className="nav-menu">
                        <Link to="/" className={`nav-link ${isActiveLink('/')}`}>
                            <i className="fas fa-home"></i>
                            Home
                        </Link>

                        {user && user.role !== "ADVISOR" && (
                        <Link to="/search" className={`nav-link ${isActiveLink('/search')}`}>
                            <i className="fas fa-search"></i>
                            Search
                        </Link>
                        )}

                        {user ? (
                            <>
                                <Link to="/dashboard" className={`nav-link ${isActiveLink('/dashboard')}`}>
                                    <i className="fas fa-tachometer-alt"></i>
                                    Dashboard
                                </Link>
                                <div className="user-menu">
                                    <span className="user-greeting">Hallo, {user.username}</span>
                                    <button onClick={handleLogout} className="btn-logout">
                                        <i className="fas fa-sign-out-alt"></i>
                                        Abmelden
                                    </button>
                                </div>
                            </>
                        ) : (
                            <button
                                onClick={() => setIsLoginModalOpen(true)}
                                className="btn-login"
                            >
                                <i className="fas fa-sign-in-alt"></i>
                                Anmelden
                            </button>
                        )}
                    </div>
                </div>
            </nav>

            <LoginModal
                isOpen={isLoginModalOpen}
                onClose={() => setIsLoginModalOpen(false)}
                onGitHubLogin={handleGitHubLogin}
            />
        </>
    );
};

export default Header;