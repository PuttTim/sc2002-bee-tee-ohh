package services;

import exceptions.AuthenticationException;
import models.User;
import repositories.UserRepository;

public class AuthService {
    public static User login(String nric, String password) throws AuthenticationException {
        User user = UserRepository.getByNRIC(nric);
        
        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid NRIC or password");
        }

        UserRepository.setActiveUser(user);

        return user;
    }

    public static void logout() {
        UserRepository.clearActiveUser();
    }
    
    public static void changePassword(User user, String oldPassword, String newPassword) throws AuthenticationException {
        if (!user.getPassword().equals(oldPassword)) {
            throw new AuthenticationException("Current password is incorrect");
        }
        
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new AuthenticationException("New password must be at least 6 characters");
        }
        
        user.setPassword(newPassword.trim());
        UserRepository.updateUser(user);
    }
}