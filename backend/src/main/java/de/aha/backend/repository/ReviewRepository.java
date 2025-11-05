package de.aha.backend.repository;

import de.aha.backend.model.advisor.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByAdvisorIdOrderByCreationDateDesc(String advisorId);
    List<Review> findByAdvisorIdAndRatingGreaterThanEqual(String advisorId, Integer minRating);
    Long countByAdvisorId(String advisorId);

    // Durchschnittliche Bewertung berechnen
    @org.springframework.data.mongodb.repository.Aggregation(pipeline = {
            "{ $match: { 'advisorId': ?0 } }",
            "{ $group: { _id: '$advisorId', averageRating: { $avg: '$rating' } } }"
    })
    Double findAverageRatingByAdvisorId(String advisorId);

    // Prüfen, ob Patient bereits eine Bewertung abgegeben hat
    boolean existsByAdvisorIdAndPatientId(String advisorId, String patientId);
}