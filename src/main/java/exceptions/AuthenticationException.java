package exceptions;

/**
 * <p>Exception thrown when authentication fails.</p>
 * <ul>
 *   <li>Extends <code>RuntimeException</code> to indicate an unchecked exception.</li>
 *   <li>Provides a constructor that accepts a custom error message.</li>
 * </ul>
 */
public class AuthenticationException extends RuntimeException {
    /**
     * <p>Constructor for <code>AuthenticationException</code> with a custom error message.</p>
     *
     * @param message The error message explaining the authentication failure.
     */
    public AuthenticationException(String message) {
        super(message);
    }
}