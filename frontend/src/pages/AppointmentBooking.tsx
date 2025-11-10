import { useParams } from 'react-router-dom';
import './AppointmentBooking.css';

export default function AppointmentBooking() {
    const { advisorId } = useParams();

    return (
        <div className="appointment-booking">
            <div className="container">
                <div className="booking-header">
                    <h1>Termin buchen</h1>
                    <p>Buchen Sie einen Termin mit Berater #{advisorId}</p>
                </div>

                <div className="booking-content">
                    <div className="booking-form">
                        <div className="form-section">
                            <h3>Termin auswählen</h3>
                            <div className="date-picker">
                                <p>Datumsauswahl wird hier angezeigt</p>
                            </div>
                        </div>

                        <div className="form-section">
                            <h3>Ihre Daten</h3>
                            <div className="form-grid">
                                <div className="form-group">
                                    <label>Vollständiger Name</label>
                                    <input type="text" placeholder="Ihr Name" />
                                </div>
                                <div className="form-group">
                                    <label>E-Mail</label>
                                    <input type="email" placeholder="Ihre E-Mail" />
                                </div>
                                <div className="form-group">
                                    <label>Telefon</label>
                                    <input type="tel" placeholder="Ihre Telefonnummer" />
                                </div>
                                <div className="form-group">
                                    <label>Nachricht (optional)</label>
                                    <textarea placeholder="Ihre Nachricht an den Berater..."></textarea>
                                </div>
                            </div>
                        </div>

                        <button className="btn btn-primary btn-large">Termin buchen</button>
                    </div>

                    <div className="booking-summary">
                        <div className="summary-card">
                            <h3>Berater Informationen</h3>
                            <div className="advisor-info">
                                <div className="advisor-avatar">
                                    <i className="fas fa-user"></i>
                                </div>
                                <div className="advisor-details">
                                    <h4>Berater #{advisorId}</h4>
                                    <p>Spezialist für Business Consulting</p>
                                    <div className="rating">
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star-half-alt"></i>
                                        <span>4.5 (128 Bewertungen)</span>
                                    </div>
                                </div>
                            </div>

                            {/*<div className="price-info">*/}
                            {/*    <h4>Preis</h4>*/}
                            {/*    <div className="price">€89,00</div>*/}
                            {/*    <p>pro 60 Minuten</p>*/}
                            {/*</div>*/}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
