package de.aha.backend.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AbstractDocument base class.
 */
public class AbstractDocumentTest {

    private static class TestDocument extends AbstractDocument {
    }

    @Test
    void constructor_ShouldCreateEmptyDocument() {
        TestDocument document = new TestDocument();

        assertNull(document.getId());
        assertNull(document.getCreationDate());
        assertNull(document.getModifyDate());
    }

    @Test
    void setId_ShouldSetIdCorrectly() {
        TestDocument document = new TestDocument();
        String id = "test123";

        document.setId(id);

        assertEquals(id, document.getId());
    }

    @Test
    void setCreationDate_ShouldSetCreationDateCorrectly() {
        TestDocument document = new TestDocument();
        LocalDateTime now = LocalDateTime.now();

        document.setCreationDate(now);

        assertEquals(now, document.getCreationDate());
    }

    @Test
    void setModifyDate_ShouldSetModifyDateCorrectly() {
        TestDocument document = new TestDocument();
        LocalDateTime now = LocalDateTime.now();

        document.setModifyDate(now);

        assertEquals(now, document.getModifyDate());
    }

    @Test
    void setAllFields_ShouldWorkCorrectly() {
        TestDocument document = new TestDocument();
        String id = "test123";
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime modified = LocalDateTime.now();

        document.setId(id);
        document.setCreationDate(created);
        document.setModifyDate(modified);

        assertEquals(id, document.getId());
        assertEquals(created, document.getCreationDate());
        assertEquals(modified, document.getModifyDate());
    }

    @Test
    void setNullValues_ShouldWorkCorrectly() {
        TestDocument document = new TestDocument();
        document.setId("test");
        document.setCreationDate(LocalDateTime.now());
        document.setModifyDate(LocalDateTime.now());

        document.setId(null);
        document.setCreationDate(null);
        document.setModifyDate(null);

        assertNull(document.getId());
        assertNull(document.getCreationDate());
        assertNull(document.getModifyDate());
    }
}
