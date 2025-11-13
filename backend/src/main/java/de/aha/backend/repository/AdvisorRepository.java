package de.aha.backend.repository;

import de.aha.backend.model.advisor.Advisor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvisorRepository extends MongoRepository<Advisor, String> {
    List<Advisor> findBySpecializationContainingIgnoreCase(String specialization);
    List<Advisor> findByLanguagesContaining(String language);
    List<Advisor> findBySpecializationContainingIgnoreCaseAndLanguagesContaining(
            String specialization, String language);
    Optional<Advisor> findByNameContainingIgnoreCase(String name);

    // Erweiterte Suchmethoden mit Pagination
    Page<Advisor> findBySpecializationContainingIgnoreCase(String specialization, Pageable pageable);
    Page<Advisor> findByLanguagesContaining(String language, Pageable pageable);
    Page<Advisor> findByRatingGreaterThanEqual(Double minRating, Pageable pageable);
    Page<Advisor> findByConsultationFeeLessThanEqual(Double maxFee, Pageable pageable);
    Page<Advisor> findByAvailableTrue(Pageable pageable);

    // Komplexe Suchabfragen
    @Query("{" +
            "  '$and': [" +
            "    { '$or': [ " +
            "      { 'name': { '$regex': ?0, '$options': 'i' } }," +
            "      { 'specialization': { '$regex': ?0, '$options': 'i' } }," +
            "      { 'bio': { '$regex': ?0, '$options': 'i' } }" +
            "    ] }," +
            "    { '$or': [ " +
            "      { 'specialization': { '$regex': ?1, '$options': 'i' } }," +
            "      { ?1: null }" +
            "    ] }," +
            "    { '$or': [ " +
            "      { 'languages': ?2 }," +
            "      { ?2: null }" +
            "    ] }," +
            "    { 'rating': { '$gte': ?3 } }," +
            "    { 'consultationFee': { '$lte': ?4 } }," +
            "    { '$or': [ " +
            "      { 'available': ?5 }," +
            "      { ?5: null }" +
            "    ] }" +
            "  ]" +
            "}")
    Page<Advisor> searchAdvisors(
            String searchQuery,
            String specialization,
            String language,
            Double minRating,
            Double maxFee,
            Boolean available,
            Pageable pageable);

    // Text-Suche über mehrere Felder
    @Query("{ '$text': { '$search': ?0 } }")
    Page<Advisor> fullTextSearch(String searchQuery, Pageable pageable);

    // Aggregations für Statistiken
    @Query(value = "{}", fields = "{ 'specialization' : 1 }")
    List<Advisor> findAllSpecializations();

    @Query(value = "{}", fields = "{ 'languages' : 1 }")
    List<Advisor> findAllLanguages();

    // Verfügbarkeitsprüfung
    @Query("{ 'available': true, 'rating': { '$gte': ?0 }, 'consultationFee': { '$lte': ?1 } }")
    List<Advisor> findAvailableAdvisors(Double minRating, Double maxFee);

    List<Advisor> findByAvailableTrue();

    // Top-Berater
    List<Advisor> findTop5ByOrderByRatingDesc();
    List<Advisor> findTop5ByOrderByReviewCountDesc();

    // Berater mit bestimmten Qualifikationen
    @Query("{ 'qualifications': { '$in': ?0 } }")
    List<Advisor> findByQualificationsIn(List<String> qualifications);

    @Query("{ 'languages': { $in: ?0 } }")
    List<Advisor> findByLanguages(List<String> languages);

    Optional<Advisor> findByUserId(String userId);

    Optional<Advisor> findByEmail(String email);

    @Query("{ 'online': true }")
    List<Advisor> findOnlineAdvisors();
}