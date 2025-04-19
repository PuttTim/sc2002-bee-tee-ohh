package services;

import exceptions.AuthenticationException;
import models.User;
import repositories.UserRepository;
import utils.Hash;

/**
 * Service class handles user authentication tasks like
 * <ul>
 *     <li>Login</li>
 *     <li>Logout</li>
 *     <li>Changing passwords</li>
 * </ul>
 */
public class AuthService {

    /**
     * To log in a user using their NRIC and password.
     *
     * @param nric the NRIC of the user
     * @param password the user's password (plain text)
     * @return the logged-in user if successful
     * @throws AuthenticationException if the NRIC is not found or password is incorrect
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
     * Logs out the current active user.
     */
    public static void logout() {
        UserRepository.clearActiveUser();
    }

    /**
     * Changes the password for the given user.
     *
     * @param user the user who wants to change their password
     * @param oldPassword the current password
     * @param newPassword the new password to set
     * @throws AuthenticationException if the current password is incorrect or the new password is too short
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