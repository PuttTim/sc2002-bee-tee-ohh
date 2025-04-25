package interfaces;

import exceptions.AuthenticationException;
import models.User;

/**
 * Interface for handling user authentication and password management.
 */
public interface IAuthService {
    /**
     * Attempts to log in a user with the provided NRIC and password.
     *
     * @param nric     The user's NRIC.
     * @param password The user's password.
     * @return The authenticated User object.
     * @throws AuthenticationException if authentication fails.
     */
    User login(String nric, String password) throws AuthenticationException;

    /**
     * Logs out the currently logged-in user.
     */
    void logout();

    /**
     * Changes the password for a given user.
     *
     * @param user        The user requesting the password change.
     * @param oldPassword The user's current password.
     * @param newPassword The new password to set.
     * @throws AuthenticationException if the old password is incorrect.
     */
    void changePassword(User user, String oldPassword, String newPassword) throws AuthenticationException;

}
