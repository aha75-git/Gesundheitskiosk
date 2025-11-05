import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../api/AuthContext';
import { advisorService } from '../services/advisorService';
import type {Advisor, SearchFilters} from '../types/advisor/AdvisorTypes.ts';
import AdvisorCard from '../components/advisorSearch/AdvisorCard.tsx';
import AdvisorFilter from '../components/advisorSearch/AdvisorFilter.tsx';
import './AdvisorSearchPage.css';

export default function AdvisorSearchPage () {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [advisors, setAdvisors] = useState<Advisor[]>([]);
    const [filteredAdvisors, setFilteredAdvisors] = useState<Advisor[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [searchQuery, setSearchQuery] = useState('');
    const [filters, setFilters] = useState<SearchFilters>({
        searchQuery: '',
        specialization: '',
        language: '',
        minRating: 0,
        maxFee: 500,
        availableToday: false
    });
    const [specializations, setSpecializations] = useState<string[]>([]);
    const [languages, setLanguages] = useState<string[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalCount, setTotalCount] = useState(0);

    useEffect(() => {
        loadInitialData();
    }, []);

    useEffect(() => {
        handleSearch();
    }, [filters, currentPage]);

    const loadInitialData = async () => {
        try {
            setIsLoading(true);
            const [specializationsData, languagesData] = await Promise.all([
                advisorService.getSpecializations(),
                advisorService.getLanguages()
            ]);
            setSpecializations(specializationsData);
            setLanguages(languagesData);
            await loadAdvisors();
        } catch (err) {
            setError('Fehler beim Laden der Daten. Bitte versuchen Sie es erneut.');
            console.error('Error loading initial data:', err);
        } finally {
            setIsLoading(false);
        }
    };

    const loadAdvisors = async (searchFilters: Partial<SearchFilters> = filters) => {
        try {
            setIsLoading(true);
            setError('');

            const response = await advisorService.searchAdvisors(searchFilters, currentPage, 12);
            setAdvisors(response.advisors);
            setFilteredAdvisors(response.advisors);
            setTotalPages(response.totalPages);
            setTotalCount(response.totalCount);
        } catch (err) {
            setError('Fehler beim Laden der Berater. Bitte versuchen Sie es erneut.');
            console.error('Error loading advisors:', err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSearchOnClick = () => {
        handleFilterChange({searchQuery: searchQuery});
    }

    const handleSearch = () => {
        loadAdvisors(filters);
    };

    const handleFilterChange = (newFilters: Partial<SearchFilters>) => {
        setFilters(prev => ({ ...prev, ...newFilters }));
        setCurrentPage(0); // Zurück zur ersten Seite bei Filteränderung
    };

    const handleSearchQueryChange = (query: string) => {
        setSearchQuery(query);

        if (query.trim() === '') {
            setFilteredAdvisors(advisors);
        } else {
            const filtered = advisors.filter(advisor =>
                advisor.name.toLowerCase().includes(query.toLowerCase()) ||
                advisor.specialization.toLowerCase().includes(query.toLowerCase()) ||
                advisor.bio.toLowerCase().includes(query.toLowerCase())
            );
            setFilteredAdvisors(filtered);
        }
    };

    const handleBookAppointment = (advisorId: string) => {
        if (!user) {
            // Wenn nicht eingeloggt, zum Login weiterleiten
            navigate('/login');
            return;
        }
        navigate(`/appointment/${advisorId}`);
    };

    const handleViewProfile = (advisorId: string) => {
        navigate(`/advisor/${advisorId}`);
    };

    const handlePageChange = (newPage: number) => {
        setCurrentPage(newPage);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const clearFilters = () => {
        setFilters({
            searchQuery: '',
            specialization: '',
            language: '',
            minRating: 0,
            maxFee: 500,
            availableToday: false
        });
        setSearchQuery('');
        setCurrentPage(0);
    };

    if (!user) {
        navigate('/login');
        return null;
    }

    return (
        <div className="advisor-search-page">
            <div className="container">
                <div className="search-header">
                    <h1>Berater finden</h1>
                    <p>Entdecken Sie qualifizierte Berater für Ihre Bedürfnisse</p>
                </div>

                {error && (
                    <div className="alert alert-error">
                        <i className="fas fa-exclamation-circle"></i>
                        {error}
                    </div>
                )}

                <div className="search-content">
                    <div className="search-sidebar">
                        <AdvisorFilter
                            filters={filters}
                            specializations={specializations}
                            languages={languages}
                            onFilterChange={handleFilterChange}
                            onClearFilters={clearFilters}
                        />
                    </div>

                    <div className="search-results">
                        <div className="results-header">
                            <div className="search-bar">
                                <div className="search-input-container">
                                    <i className="fas fa-search"></i>
                                    <input
                                        type="text"
                                        placeholder="Nach Namen, Spezialisierung oder Beschreibung suchen..."
                                        value={searchQuery}
                                        onChange={(e) => handleSearchQueryChange(e.target.value)}
                                    />
                                </div>
                                <button
                                    onClick={handleSearchOnClick}
                                    className="btn btn-primary"
                                >
                                    <i className="fas fa-search"></i>
                                    Suchen
                                </button>
                            </div>

                            <div className="results-info">
                <span className="results-count">
                  {totalCount} Berater gefunden
                </span>
                                <div className="sort-options">
                                    <label>Sortieren nach:</label>
                                    <select>
                                        <option value="rating">Bewertung</option>
                                        <option value="experience">Erfahrung</option>
                                        <option value="fee">Honorar</option>
                                        <option value="name">Name</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        {isLoading ? (
                            <div className="loading-container">
                                <div className="loading-spinner">
                                    <i className="fas fa-spinner fa-spin"></i>
                                    <p>Lade Berater...</p>
                                </div>
                            </div>
                        ) : filteredAdvisors.length === 0 ? (
                            <div className="empty-state">
                                <i className="fas fa-user-md"></i>
                                <h3>Keine Berater gefunden</h3>
                                <p>
                                    {advisors.length === 0
                                        ? "Es wurden keine Berater gefunden, die den Suchkriterien entsprechen."
                                        : "Keine Berater entsprechen den aktuellen Filtereinstellungen."
                                    }
                                </p>
                                <button
                                    onClick={clearFilters}
                                    className="btn btn-primary"
                                >
                                    <i className="fas fa-times btn-advisor-reset-filters"></i>
                                    Filter zurücksetzen
                                </button>
                            </div>
                        ) : (
                            <>
                                <div className="advisors-grid">
                                    {filteredAdvisors.map(advisor => (
                                        <AdvisorCard
                                            key={advisor.id}
                                            advisor={advisor}
                                            onBookAppointment={handleBookAppointment}
                                            onViewProfile={handleViewProfile}
                                        />
                                    ))}
                                </div>

                                {totalPages > 1 && (
                                    <div className="pagination">
                                        <button
                                            onClick={() => handlePageChange(currentPage - 1)}
                                            disabled={currentPage === 0}
                                            className="pagination-btn"
                                        >
                                            <i className="fas fa-chevron-left"></i>
                                            Vorherige
                                        </button>

                                        <div className="pagination-numbers">
                                            {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                                                let pageNum;
                                                if (totalPages <= 5) {
                                                    pageNum = i;
                                                } else if (currentPage <= 2) {
                                                    pageNum = i;
                                                } else if (currentPage >= totalPages - 3) {
                                                    pageNum = totalPages - 5 + i;
                                                } else {
                                                    pageNum = currentPage - 2 + i;
                                                }

                                                return (
                                                    <button
                                                        key={pageNum}
                                                        onClick={() => handlePageChange(pageNum)}
                                                        className={`pagination-number ${currentPage === pageNum ? 'active' : ''}`}
                                                    >
                                                        {pageNum + 1}
                                                    </button>
                                                );
                                            })}
                                        </div>

                                        <button
                                            onClick={() => handlePageChange(currentPage + 1)}
                                            disabled={currentPage === totalPages - 1}
                                            className="pagination-btn"
                                        >
                                            Nächste
                                            <i className="fas fa-chevron-right"></i>
                                        </button>
                                    </div>
                                )}
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};
