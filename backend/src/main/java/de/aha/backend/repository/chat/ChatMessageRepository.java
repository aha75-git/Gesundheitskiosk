package de.aha.backend.repository.chat;

import de.aha.backend.model.chat.ChatMessage;
import de.aha.backend.model.chat.SenderType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);

    @Query("{ 'sessionId': ?0, 'read': false, 'senderType': ?1 }")
    List<ChatMessage> findUnreadMessagesBySessionAndSenderType(String sessionId, SenderType senderType);

    @Query(value = "{ 'sessionId': ?0, 'read': false, 'senderType': ?1 }", count = true)
    long countUnreadMessagesBySessionAndSenderType(String sessionId, SenderType senderType);

    @Query("{ 'sessionId': ?0, 'senderType': 'ADVISOR', 'read': false }")
    List<ChatMessage> findUnreadAdvisorMessages(String sessionId);

    @Query("{ 'sessionId': ?0, 'senderType': 'USER', 'read': false }")
    List<ChatMessage> findUnreadUserMessages(String sessionId);
}