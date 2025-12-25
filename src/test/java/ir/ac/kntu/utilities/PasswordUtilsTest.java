package ir.ac.kntu.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PasswordUtilsTest - Unit tests for password validation utility
 */
@DisplayName("Password Utils Tests")
class PasswordUtilsTest {

    @Test
    @DisplayName("Valid password with all requirements")
    void testValidPassword() {
        assertTrue(PasswordUtils.isStrongPassword("Password123!"));
        assertTrue(PasswordUtils.isStrongPassword("MyP@ssw0rd"));
        assertTrue(PasswordUtils.isStrongPassword("Test123#"));
        assertTrue(PasswordUtils.isStrongPassword("Abc1234$"));
    }

    @Test
    @DisplayName("Password too short")
    void testShortPassword() {
        assertFalse(PasswordUtils.isStrongPassword("Pass1!"));
        assertFalse(PasswordUtils.isStrongPassword("Abc12#"));
        assertFalse(PasswordUtils.isStrongPassword(""));
    }

    @Test
    @DisplayName("Password missing uppercase")
    void testMissingUppercase() {
        assertFalse(PasswordUtils.isStrongPassword("password123!"));
        assertFalse(PasswordUtils.isStrongPassword("mypass123@"));
    }

    @Test
    @DisplayName("Password missing lowercase")
    void testMissingLowercase() {
        assertFalse(PasswordUtils.isStrongPassword("PASSWORD123!"));
        assertFalse(PasswordUtils.isStrongPassword("MYPASS123@"));
    }

    @Test
    @DisplayName("Password missing digit")
    void testMissingDigit() {
        assertFalse(PasswordUtils.isStrongPassword("Password!"));
        assertFalse(PasswordUtils.isStrongPassword("MyPass@"));
    }

    @Test
    @DisplayName("Password missing special character")
    void testMissingSpecialCharacter() {
        assertFalse(PasswordUtils.isStrongPassword("Password123"));
        assertFalse(PasswordUtils.isStrongPassword("MyPass123"));
    }

    @Test
    @DisplayName("Null password")
    void testNullPassword() {
        assertFalse(PasswordUtils.isStrongPassword(null));
    }

    @Test
    @DisplayName("Password with various special characters")
    void testSpecialCharacters() {
        assertTrue(PasswordUtils.isStrongPassword("Pass123!"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123@"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123#"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123$"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123%"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123^"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123&"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123*"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123("));
        assertTrue(PasswordUtils.isStrongPassword("Pass123)"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123_"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123+"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123-"));
        assertTrue(PasswordUtils.isStrongPassword("Pass123="));
    }

    @Test
    @DisplayName("Edge case - exactly 8 characters")
    void testExactly8Characters() {
        assertTrue(PasswordUtils.isStrongPassword("Pass123!"));
        assertFalse(PasswordUtils.isStrongPassword("Pass123")); // 7 characters
    }

    @Test
    @DisplayName("Long password")
    void testLongPassword() {
        assertTrue(PasswordUtils.isStrongPassword("VeryLongPassword123!"));
        assertTrue(PasswordUtils.isStrongPassword("ThisIsAVeryLongPasswordWithAllRequirements123!@#"));
    }
}

