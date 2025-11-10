import { Link } from 'react-router-dom';
import './HomePage.css';
import FeaturesSection from "../components/features/FeaturesSection.tsx";
import {useAuth} from "../api/AuthContext.tsx";

export default function HomePage() {
    const { user } = useAuth();

    return (
        <div className="homepage">
            <section className="hero">
                <div className="container">
                    <div className="hero-content">
                        <h1>Finden Sie den perfekten Berater</h1>
                        <p>Entdecken Sie qualifizierte Berater und vereinbaren Sie mühelos Termine</p>
                        { user && user.role !== "ADVISOR" ? (
                            <div className="hero-buttons">

                                <Link to="/search" className="btn btn-primary">
                                    🔍 Berater finden
                                </Link>
                                <Link to="/register" className="btn btn-secondary">
                                📞 Jetzt starten
                                </Link>
                            </div>
                        ) : (
                            <div className="hero-buttons">
                                <Link to="/register" className="btn btn-primary">
                                    📞 Jetzt starten
                                </Link>
                            </div>
                        )}

                    </div>
                </div>
            </section>

            <FeaturesSection />

            <section className="stats">
                <div className="container">
                    <div className="stats-grid">
                        <div className="stat">
                            <h3>500+</h3>
                            <p>Zufriedene Kunden</p>
                        </div>
                        <div className="stat">
                            <h3>100+</h3>
                            <p>Qualifizierte Berater</p>
                        </div>
                        <div className="stat">
                            <h3>95%</h3>
                            <p>Positive Bewertungen</p>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
};
