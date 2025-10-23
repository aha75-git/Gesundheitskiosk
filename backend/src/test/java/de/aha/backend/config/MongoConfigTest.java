package de.aha.backend.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MongoConfigTest {
    @Test
    void canInstantiate() { assertDoesNotThrow(MongoConfig::new); }
}