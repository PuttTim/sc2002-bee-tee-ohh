package interfaces;

import exceptions.AuthenticationException;
import models.User;

/**
 * Interface for handling user authentication and password management.
 */
public interface IAuthService {
    User login(String nric, String password) throws AuthenticationException;
    void logout();
    void changePassword(User user, String oldPassword, String newPassword) throws AuthenticationException;
}
