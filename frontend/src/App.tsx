import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './api/AuthContext';
import Layout from './components/layout/Layout.tsx';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import Dashboard from './pages/Dashboard';

import OAuthRedirect from './components/oauth/OAuthRedirect.tsx';
import './App.css';
import ProfilePage from "./pages/ProfilePage.tsx";
import MyAppointmentsPage from "./pages/MyAppointmentsPage.tsx";
import AdvisorSearchPage from "./pages/AdvisorSearchPage.tsx";
import AppointmentBookingPage from "./pages/AppointmentBookingPage.tsx";
import AppointmentConfirmationPage from "./pages/AppointmentConfirmationPage.tsx";
import AdvisorProfilePage from "./pages/AdvisorProfilePage.tsx";
import Impressum from "./pages/Impressum.tsx";
import Datenschutz from "./pages/Datenschutz.tsx";

function App() {
    return (
        <AuthProvider>
            <Router>
                <Layout>
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/search" element={<AdvisorSearchPage />} />
                        <Route path="/appointment/:advisorId" element={<AppointmentBookingPage />} />
                        <Route path="/appointments" element={<MyAppointmentsPage />} />
                        <Route path="/dashboard" element={<Dashboard />} />
                        <Route path="/profile" element={<ProfilePage />} />
                        <Route path="/appointment-confirmation" element={<AppointmentConfirmationPage />} />
                        <Route path="/advisor/:advisorId" element={<AdvisorProfilePage />} />
                        <Route path="/impressum" element={<Impressum />} />
                        <Route path="/datenschutz" element={<Datenschutz />} />
                        <Route path="/oauth2/redirect" element={<OAuthRedirect />} />
                    </Routes>
                </Layout>
            </Router>
        </AuthProvider>
    );
}

export default App;