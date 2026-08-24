package service.generator;

import org.example.service.generator.PasswordGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    private final PasswordGenerator passwordGenerator =
            new PasswordGenerator();

    @Test
    void shouldGeneratePasswordWithLengthTen() {
        String password = passwordGenerator.generate();

        assertEquals(10, password.length());
    }

    @Test
    void shouldGenerateAlphanumericPassword() {
        String password = passwordGenerator.generate();

        assertTrue(password.matches("[A-Za-z0-9]+"));
    }

}