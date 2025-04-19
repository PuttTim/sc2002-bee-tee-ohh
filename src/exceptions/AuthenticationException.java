package exceptions;

/**
 * Exception thrown when authentication fails.
 */
public class AuthenticationException extends RuntimeException {
    /**
     * Creates a new AuthenticationException with an included message.
     *
     * @param message the error message to be shown.
     */
    public AuthenticationException(String message) {
        super(message);
    }
}