package de.aha.backend.config;

import de.aha.backend.model.advisor.Advisor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;

/**
 * Configuration class for enabling MongoDB auditing.
 * Auditing allows automatic population of creation and modification dates in entities.
 */
@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfig {
    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void createTextIndex() {
        TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                .onField("name", 2F)    // Gewichtung 2
                .onField("specialization", 3F) // Gewichtung 3
                .onField("bio", 1F)     // Gewichtung 1
                .build();

        mongoTemplate.indexOps(Advisor.class).createIndex(textIndex);
    }
}