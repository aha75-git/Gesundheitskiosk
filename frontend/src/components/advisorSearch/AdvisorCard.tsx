import type {Advisor} from '../../types/advisor/AdvisorTypes.ts';
import './AdvisorCard.css';

type AdvisorCardProps = {
    advisor: Advisor;
    onBookAppointment: (advisorId: string) => void;
    onViewProfile: (advisorId: string) => void;
}

export default function AdvisorCard(props: Readonly<AdvisorCardProps>){
    const {
        advisor,
        onBookAppointment,
        onViewProfile
    } = props;

    const renderStars = (rating: number) => {
        return Array.from({ length: 5 }, (_, i) => (
            <i
                key={i}
                className={`fas fa-star ${i < Math.floor(rating) ? 'active' : ''} ${
                    i === Math.floor(rating) && rating % 1 >= 0.5 ? 'half' : ''
                }`}
            ></i>
        ));
    };

    const formatLanguages = (languages: string[]) => {
        return languages.slice(0, 2).join(', ') + (languages.length > 2 ? '...' : '');
    };

    return (
        <div className="advisor-card">
            <div className="advisor-header">
                <div className="advisor-image">
                    {advisor.image ? (
                        <img src={advisor.image} alt={advisor.name} />
                    ) : (
                        <div className="advisor-avatar">
                            <i className="fas fa-user-md"></i>
                        </div>
                    )}
                </div>

                <div className="advisor-info">
                    <h3 className="advisor-name">{advisor.name}</h3>
                    <p className="advisor-specialization">{advisor.specialization}</p>

                    <div className="advisor-rating">
                        <div className="stars">
                            {renderStars(advisor.rating)}
                        </div>
                        <span className="rating-value">{advisor.rating.toFixed(1)}</span>
                        {/*<span className="reviews-count">({advisor.reviews.length} Bewertungen)</span>*/}
                    </div>
                </div>
            </div>

            <div className="advisor-details">
                <div className="detail-item">
                    <i className="fas fa-language"></i>
                    <span>{formatLanguages(advisor.languages)}</span>
                </div>

                <div className="detail-item">
                    <i className="fas fa-briefcase"></i>
                    <span>{advisor.experience} Jahre Erfahrung</span>
                </div>

                <div className="detail-item">
                    <i className="fas fa-graduation-cap"></i>
                    <span>{advisor.qualifications.length} Qualifikationen</span>
                </div>

                {/*<div className="detail-item">*/}
                {/*    <i className="fas fa-euro-sign"></i>*/}
                {/*    <span>{advisor.consultationFee} €/Stunde</span>*/}
                {/*</div>*/}
            </div>

            <p className="advisor-bio">
                {advisor.bio.length > 120
                    ? `${advisor.bio.substring(0, 120)}...`
                    : advisor.bio
                }
            </p>

            <div className="advisor-availability">
                <div className={`availability-status ${advisor.available ? 'available' : 'unavailable'}`}>
                    <i className={`fas fa-circle ${advisor.available ? 'available' : 'unavailable'}`}></i>
                    {advisor.available ? 'Jetzt verfügbar' : 'Zurzeit nicht verfügbar'}
                </div>
            </div>

            <div className="advisor-actions">
                <button
                    onClick={() => onViewProfile(advisor.id)}
                    className="btn btn-secondary"
                >
                    <i className="fas fa-user"></i>
                    Profil
                </button>

                <button
                    onClick={() => onBookAppointment(advisor.id)}
                    disabled={!advisor.available}
                    className="btn btn-primary"
                >
                    <i className="fas fa-calendar-plus"></i>
                    Termin
                </button>
            </div>
        </div>
    );
};
