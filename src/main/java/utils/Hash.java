package utils;

import java.security.MessageDigest;

/**
 * Utility class for hashing strings and verifying hashed values using SHA-256.
 */
public class Hash {

    /**
     * Generates a SHA-256 hash of the input string.
     *
     * @param input the string to hash
     * @return the hashed string in hexadecimal format
     */
    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(input.getBytes("UTF-8"));

            StringBuilder hexstring = new StringBuilder();
            for (byte b : encoded) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexstring.append('0');
                }
                hexstring.append(hex);
            }
            return hexstring.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies whether the hash of the input password matches the given hashed password.
     *
     * @param inputPassword the password to check
     * @param hashedPassword the expected hash
     * @return true if the input password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String inputPassword, String hashedPassword) {
        return hash(inputPassword).equals(hashedPassword);
    }
}
