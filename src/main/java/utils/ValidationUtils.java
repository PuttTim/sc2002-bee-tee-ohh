package utils;

/**
 * Utility class for input validation.
 */
public class ValidationUtils {
    /**
     * Validates a Singapore NRIC number.
     * Format: Starts with S or T, followed by 7 digits, and ends with a letter
     * 
     * @param nric The NRIC to validate
     * @return true if NRIC format is valid, false otherwise
     */
    public static boolean isValidNRIC(String nric) {
        if (nric == null || nric.length() != 9) {
            return false;
        }

        // First character should be S or T
        char firstChar = nric.charAt(0);
        if (firstChar != 'S' && firstChar != 'T') {
            return false;
        }

        // Last character should be a letter
        if (!Character.isLetter(nric.charAt(8))) {
            return false;
        }

        // Middle 7 characters should be digits
        String digits = nric.substring(1, 8);
        try {
            Integer.parseInt(digits);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}