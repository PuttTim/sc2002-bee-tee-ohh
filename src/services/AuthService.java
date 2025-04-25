package services;

import exceptions.AuthenticationException;
import models.User;
import repositories.UserRepository;
import utils.Hash;

/**
 * Service class for handling user authentication and password management.
 * <p>
 * Provides functionality for user login, logout, and password changes.
 * Interacts with {@code UserRepository} to manage user sessions and credentials.
 * </p>
 */
public class AuthService {

    /**
     * Authenticates a user with their NRIC and password.
     * Successful authentication will:
     * <ul>
     *   <li>Set the user as the active session user</li>
     *   <li>Return the authenticated user object</li>
     * </ul>
     *
     * @param nric the user's NRIC identifier
     * @param password the user's password
     * @return the authenticated {@code User} object
     * @throws AuthenticationException if credentials are invalid
     */
    public static User login(String nric, String password) throws AuthenticationException {
        User user = UserRepository.getByNRIC(nric);

        if (user == null || !Hash.verifyPassword(password, user.getPassword())) {
            throw new AuthenticationException("Invalid NRIC or password");
        }

        UserRepository.setActiveUser(user);

        return user;
    }

    /**
     * Terminates the current user session.
     * Clears the active user from the {@code UserRepository}.
     */
    public static void logout() {
        UserRepository.clearActiveUser();
    }

    /**
     * Changes a user's password after verifying current credentials.
     * Validates that:
     * <ul>
     *   <li>The current password matches</li>
     *   <li>The new password meets length requirements</li>
     * </ul>
     *
     * @param user the user changing their password
     * @param oldPassword the current password for verification
     * @param newPassword the desired new password
     * @throws AuthenticationException if validation fails
     */
    public static void changePassword(User user, String oldPassword, String newPassword) throws AuthenticationException {
        if (!Hash.verifyPassword(oldPassword, user.getPassword())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new AuthenticationException("New password must be at least 6 characters");
        }

        user.setPassword(newPassword.trim());
        UserRepository.updateUser(user);
    }
}