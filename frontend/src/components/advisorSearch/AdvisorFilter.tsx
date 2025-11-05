import type {SearchFilters} from '../../types/advisor/AdvisorTypes.ts';
import './AdvisorFilter.css';

type AdvisorFilterProps = {
    filters: SearchFilters;
    specializations: string[];
    languages: string[];
    onFilterChange: (filters: Partial<SearchFilters>) => void;
    onClearFilters: () => void;
}

export default function AdvisorFilter(props: Readonly<AdvisorFilterProps>){
    const {
         filters,
         specializations,
         languages,
         onFilterChange,
         onClearFilters
    } = props;

    const hasActiveFilters =
        filters.specialization !== '' ||
        filters.language !== '' ||
        filters.minRating > 0 ||
        filters.maxFee < 500 ||
        filters.availableToday;

    return (
        <div className="advisor-filter">
            <div className="filter-header">
                <h3>Filter</h3>
                {hasActiveFilters && (
                    <button onClick={onClearFilters} className="btn-clear">
                        <i className="fas fa-times"></i>
                        Zurücksetzen
                    </button>
                )}
            </div>

            <div className="filter-section">
                <h4>Spezialisierung</h4>
                <select
                    value={filters.specialization}
                    onChange={(e) => onFilterChange({ specialization: e.target.value })}
                >
                    <option value="">Alle Spezialisierungen</option>
                    {specializations.map(spec => (
                        <option key={spec} value={spec}>{spec}</option>
                    ))}
                </select>
            </div>

            <div className="filter-section">
                <h4>Sprache</h4>
                <select
                    value={filters.language}
                    onChange={(e) => onFilterChange({ language: e.target.value })}
                >
                    <option value="">Alle Sprachen</option>
                    {languages.map(lang => (
                        <option key={lang} value={lang}>{lang}</option>
                    ))}
                </select>
            </div>

            <div className="filter-section">
                <h4>Mindestbewertung</h4>
                <div className="rating-filter">
                    {[4, 3, 2, 1].map(rating => (
                        <button
                            key={rating}
                            onClick={() => onFilterChange({ minRating: rating })}
                            className={`rating-option ${filters.minRating === rating ? 'active' : ''}`}
                        >
                            {Array.from({ length: 5 }, (_, i) => (
                                <i
                                    key={i}
                                    className={`fas fa-star ${i < rating ? 'active' : ''}`}
                                ></i>
                            ))}
                            <span>ab {rating}.0</span>
                        </button>
                    ))}
                    <button
                        onClick={() => onFilterChange({ minRating: 0 })}
                        className={`rating-option ${filters.minRating === 0 ? 'active' : ''}`}
                    >
                        Alle Bewertungen
                    </button>
                </div>
            </div>

            <div className="filter-section">
                <h4>Maximales Honorar</h4>
                <div className="fee-filter">
                    <input
                        type="range"
                        min="0"
                        max="500"
                        step="10"
                        value={filters.maxFee}
                        onChange={(e) => onFilterChange({ maxFee: parseInt(e.target.value) })}
                    />
                    <div className="fee-value">
                        Bis {filters.maxFee} €
                    </div>
                </div>
            </div>

            <div className="filter-section">
                <label className="checkbox-label">
                    <input
                        type="checkbox"
                        checked={filters.availableToday}
                        onChange={(e) => onFilterChange({ availableToday: e.target.checked })}
                    />
                    <span className="checkmark"></span>
                    Heute verfügbar
                </label>
            </div>
        </div>
    );
};
