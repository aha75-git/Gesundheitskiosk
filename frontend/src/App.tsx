import './App.css'
import {BrowserRouter} from "react-router-dom";
import {AuthProvider} from "./api/AuthContext.tsx";
import {useState} from "react";
import type {AuthResponse, User} from "./types/types.ts";
import LoginForm from "./components/LoginForm.tsx";
import RegisterForm from "./components/RegisterForm.tsx";

/*
import Login from "./components/Login.tsx";
import Register from "./components/Register.tsx";
import OAuthRedirect from "./components/OAuthRedirect.tsx";
import Dashboard from "./components/Dashboard.tsx";



const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const { user, isLoading } = useAuth();

    if (isLoading) {
        return <div>Loading...</div>;
    }

    return user ? <>{children}</> : <Navigate to="/login" />;
};
*/


function App() {
    const [activeTab, setActiveTab] = useState<'login' | 'register'>('login');
    const [currentUser, setCurrentUser] = useState<User | null>(null);

    console.log('token: ' + localStorage.getItem('token'));
    console.log('user: ' + localStorage.getItem('user'));
    console.log('currentUser: ' + currentUser);

    const handleLoginSuccess = (authResponse: AuthResponse) => {
        setCurrentUser(authResponse.user);
        localStorage.setItem('token', authResponse.token);
        localStorage.setItem('user', JSON.stringify(authResponse.user));
    };

    const handleLogout = () => {
        setCurrentUser(null);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    };

    if (currentUser) {
        return (
            <AuthProvider>
                <BrowserRouter>
                    <div className="app dashboard">
                        <div className="dashboard-container">
                            <div className="welcome-card">
                                <h1>Willkommen, {currentUser.username}!</h1>
                                <p>Sie sind erfolgreich angemeldet.</p>
                                <div className="user-info">
                                    <p><strong>E-Mail:</strong> {currentUser.email}</p>
                                    <p><strong>Rolle:</strong> {currentUser.role}</p>
                                    <p><strong>Mitglied seit:</strong> {new Date(currentUser.createdAt).toLocaleDateString('de-DE')}</p>
                                </div>
                                <button onClick={handleLogout} className="btn btn-secondary">
                                    Abmelden
                                </button>
                            </div>
                        </div>
                    </div>
                </BrowserRouter>
            </AuthProvider>
        );
    }

  return (
      <AuthProvider>
          <BrowserRouter>
              <div className="app">
                  <div className="auth-container">
                      <div className="auth-card">
                          <div className="card-header">
                              <h1>Willkommen zurück</h1>
                              <p>Melden Sie sich an oder erstellen Sie ein Konto</p>
                          </div>

                          <div className="card-body">
                              <div className="tabs">
                                  <button
                                      className={`tab ${activeTab === 'login' ? 'active' : ''}`}
                                      onClick={() => setActiveTab('login')}
                                  >
                                      Anmelden
                                  </button>
                                  <button
                                      className={`tab ${activeTab === 'register' ? 'active' : ''}`}
                                      onClick={() => setActiveTab('register')}
                                  >
                                      Registrieren
                                  </button>
                              </div>

                              <div className="form-container">
                                  {activeTab === 'login' ? (
                                      <LoginForm onLoginSuccess={handleLoginSuccess} />
                                  ) : (
                                      <RegisterForm onRegisterSuccess={handleLoginSuccess} />
                                  )}
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
          </BrowserRouter>
      </AuthProvider>
  )
}

export default App
