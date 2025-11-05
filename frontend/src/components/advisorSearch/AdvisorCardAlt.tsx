import { Link } from 'react-router-dom'

const AdvisorCardAlt = ({ advisor }) => {
    return (
        <div className="card advisor-card">
            <div className="card-body">
                <div className="text-center">
                    <div className="advisor-avatar">
                        {advisor.name.split(' ').map(n => n[0]).join('')}
                    </div>
                    <h3 className="advisor-name mb-1">{advisor.name}</h3>
                    <p className="advisor-specialization">{advisor.specialization}</p>
                </div>

                <div className="advisor-languages">
                    {advisor.languages.map(lang => (
                        <span key={lang} className="language-tag">
              {lang}
            </span>
                    ))}
                </div>

                <div className="advisor-rating mb-2">
                    <span>⭐ {advisor.rating}</span>
                    <span>{advisor.available ? '🔴 Jetzt verfügbar' : '⚪ Nicht verfügbar'}</span>
                </div>

                {advisor.bio && (
                    <p className="advisor-bio mb-2">{advisor.bio}</p>
                )}

                <div className="card-footer">
                    <Link
                        to={`/appointment/${advisor.id}`}
                        className="btn btn-primary"
                        style={{ width: '100%', justifyContent: 'center' }}
                    >
                        Termin vereinbaren
                    </Link>
                </div>
            </div>
        </div>
    )
}

export default AdvisorCardAlt