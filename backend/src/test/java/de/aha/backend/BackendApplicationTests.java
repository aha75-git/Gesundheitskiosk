package de.aha.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethod_SetsTimezoneCorrectly() {
        BackendApplication.main(new String[]{});
        assertEquals("Europe/Berlin", TimeZone.getDefault().getID());
    }
}
