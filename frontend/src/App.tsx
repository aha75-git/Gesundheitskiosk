// import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './api/AuthContext';
import Layout from './components/Layout';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import Dashboard from './pages/Dashboard';
import AdvisorSearch from './pages/AdvisorSearch';
import AppointmentBooking from './pages/AppointmentBooking';
import OAuthRedirect from './components/OAuthRedirect';
import './App.css';

function App() {
    return (
        <AuthProvider>
            <Router>
                <Layout>
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/search" element={<AdvisorSearch />} />
                        <Route path="/appointment/:advisorId" element={<AppointmentBooking />} />
                        <Route path="/dashboard" element={<Dashboard />} />
                        <Route path="/oauth2/redirect" element={<OAuthRedirect />} />
                    </Routes>
                </Layout>
            </Router>
        </AuthProvider>
    );
}

export default App;