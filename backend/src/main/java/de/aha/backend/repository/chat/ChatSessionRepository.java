package de.aha.backend.repository.chat;

import de.aha.backend.model.chat.ChatSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {

    List<ChatSession> findByUserIdOrderByLastMessageTimeDesc(String userId);

    List<ChatSession> findByAdvisorIdOrderByLastMessageTimeDesc(String advisorId);

    Optional<ChatSession> findByUserIdAndAdvisorId(String userId, String advisorId);

    @Query("{ 'userId': ?0, 'isActive': true }")
    List<ChatSession> findActiveSessionsByUserId(String userId);

    @Query("{ 'advisorId': ?0, 'isActive': true }")
    List<ChatSession> findActiveSessionsByAdvisorId(String advisorId);

    boolean existsByUserIdAndAdvisorId(String userId, String advisorId);
}