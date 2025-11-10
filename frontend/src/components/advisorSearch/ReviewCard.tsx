import React, { useState } from 'react';
import './ReviewCard.css';

interface ReviewCardProps {
    author: string;
    rating: number;
    date: string;
    comment: string;
    helpful?: number;
}

const ReviewCard: React.FC<ReviewCardProps> = ({
                                                   author,
                                                   rating,
                                                   date,
                                                   comment,
                                                   helpful = 0
                                               }) => {
    const [isHelpful, setIsHelpful] = useState(false);
    const [helpfulCount, setHelpfulCount] = useState(helpful);

    const handleHelpfulClick = () => {
        if (isHelpful) {
            setHelpfulCount(prev => prev - 1);
        } else {
            setHelpfulCount(prev => prev + 1);
        }
        setIsHelpful(!isHelpful);
    };

    return (
        <div className="review-card">
            <div className="review-header">
                <div className="reviewer-info">
                    <div className="reviewer-avatar">
                        <i className="fas fa-user"></i>
                    </div>
                    <div className="reviewer-details">
                        <span className="reviewer-name">{author}</span>
                        <span className="review-date">{date}</span>
                    </div>
                </div>

                <div className="review-rating">
                    <div className="stars">
                        {[1, 2, 3, 4, 5].map((star) => (
                            <i
                                key={star}
                                className={`fas fa-star ${star <= rating ? 'filled' : 'empty'}`}
                            ></i>
                        ))}
                    </div>
                </div>
            </div>

            <div className="review-content">
                <p>{comment}</p>
            </div>

            <div className="review-actions">
                <button
                    onClick={handleHelpfulClick}
                    className={`helpful-btn ${isHelpful ? 'active' : ''}`}
                >
                    <i className="fas fa-thumbs-up"></i>
                    Hilfreich ({helpfulCount})
                </button>

                <button className="report-btn">
                    <i className="fas fa-flag"></i>
                    Melden
                </button>
            </div>
        </div>
    );
};

export default ReviewCard;