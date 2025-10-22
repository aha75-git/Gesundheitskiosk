package de.aha.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Configuration class for enabling MongoDB auditing.
 * Auditing allows automatic population of creation and modification dates in entities.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {}