package de.aha.backend.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MongoConfigTest {
    @Mock
    private MongoTemplate mongoTemplate;

    @Test
    void canInstantiate() {
        assertDoesNotThrow(() -> new MongoConfig(mongoTemplate));
    }
}