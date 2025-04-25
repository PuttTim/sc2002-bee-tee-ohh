package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.Hash;

public class HashTest {
    @Test
    @DisplayName("Correct hashing results")
    void correctHashResults() {
        String password = "password";
        String hashedPassword = Hash.hash(password);
        String expectedHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        assertEquals(expectedHash, hashedPassword);
    }

    @Test
    @DisplayName("Verify pass when correct")
    void correctVerifyPassword() {
        String password = "password";
        String hashedPassword = Hash.hash(password);
        boolean success = Hash.verifyPassword(password, hashedPassword);

        assertTrue(success);
    }

    @Test
    @DisplayName("Verify fail when wrong")
    void wrongVerifyPassword() {
        String password = "password";
        String hashedPassword = Hash.hash("wrongpassword");
        boolean success = Hash.verifyPassword(password, hashedPassword);

        assertFalse(success);
    }
}
