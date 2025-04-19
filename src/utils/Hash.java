package utils;

import java.security.MessageDigest;

/**
 * Utility class for hashing and verifying passwords using SHA-256.
 */
public class Hash {

    /**
     * Hashes a given input string using SHA-256.
     *
     * @param input the string to be hashed
     * @return the SHA-256 hashed value as a hexadecimal string
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
     * Verifies if the input password matches the hashed password.
     *
     * @param inputPassword the plain-text password to check
     * @param hashedPassword the previously hashed password to compare against
     * @return <code>true</code> if the password matches,
     * <code>false</code> if password does not match
     */
    public static boolean verifyPassword(String inputPassword, String hashedPassword) {
        return hash(inputPassword).equals(hashedPassword);
    }
}