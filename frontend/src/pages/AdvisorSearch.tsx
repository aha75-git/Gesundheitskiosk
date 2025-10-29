import React from 'react';
import './AdvisorSearch.css';

const AdvisorSearch: React.FC = () => {
    return (
        <div className="advisor-search">
            <div className="container">
                <div className="search-header">
                    <h1>Berater finden</h1>
                    <p>Entdecken Sie unsere qualifizierten Berater</p>
                </div>

                <div className="search-content">
                    <div className="search-filters">
                        <div className="filter-group">
                            <label>Fachgebiet</label>
                            <select>
                                <option>Alle Fachgebiete</option>
                                <option>Allgemeinmedizin</option>
                                <option>Kardiologie</option>
                                <option>Psychologie</option>
                                <option>Pädiatrie</option>
                                <option>Neurologie</option>
                            </select>
                        </div>

                        <div className="filter-group">
                            <label>Sprache</label>
                            <select>
                                <option>Alle Sprachen</option>
                                <option>Deutsch</option>
                                <option>Türkçe</option>
                                <option>العربية</option>
                                <option>English</option>
                                <option>Русский</option>
                            </select>
                        </div>

                        <div className="filter-group">
                            <label>Standort</label>
                            <input type="text" placeholder="Stadt oder PLZ" />
                        </div>

                        <div className="filter-group">
                            <label>Verfügbarkeit</label>
                            <select>
                                <option>Alle Termine</option>
                                <option>Diese Woche</option>
                                <option>Nächste Woche</option>
                            </select>
                        </div>

                        <button className="btn btn-primary">Filter anwenden</button>
                    </div>

                    <div className="search-results">
                        <div className="no-results">
                            <i className="fas fa-search"></i>
                            <h3>Noch keine Berater gefunden</h3>
                            <p>Verwenden Sie die Filter, um Berater in Ihrer Nähe zu finden.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdvisorSearch;