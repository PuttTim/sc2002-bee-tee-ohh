package repositories;

import models.User;
import models.enums.MaritalStatus;
import models.enums.Role;
import utils.CsvReader;
import utils.CsvWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.ICsvConfig;

/**
 * A repository class for managing users.
 * <p>This class handles user data such as:</p>
 * <ul>
 *     <li>Saving, loading, adding, removing users</li>
 *     <li>Managing active user and their role</li>
 * </ul>
 */
public class UserRepository {

    /**
     * Configuration for reading and writing the CSV file containing users.
     */
    private static class UserCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/users.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("UserNRIC", "Name", "Password", "Age", "MaritalStatus", "Role");
        }
    }

    private static List<User> users = new ArrayList<>();
    private static User activeUser = null;
    private static Role userMode = null;

    private UserRepository() {} // private constructor

    /**
     * Saves all users to the CSV file.
     */
    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (User user : users) {
            Map<String, String> record = new HashMap<>();
            record.put("UserNRIC", user.getUserNRIC());
            record.put("Name", user.getName());
            record.put("Password", user.getPassword());
            record.put("Age", String.valueOf(user.getAge()));
            record.put("MaritalStatus", user.getMaritalStatus().toString());
            record.put("Role", user.getRole().toString());
            records.add(record);
        }

        try {
            CsvWriter.write(new UserCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Loads all users from the CSV file.
     */
    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new UserCsvConfig());
            users = new ArrayList<>();

            for (Map<String, String> record : records) {
                User user = new User(
                        record.get("UserNRIC"),
                        record.get("Name"),
                        record.get("Password"),
                        Integer.parseInt(record.get("Age"))
                );
                user.setMaritalStatus(MaritalStatus.valueOf(record.get("MaritalStatus")));
                user.setRole(Role.valueOf(record.get("Role")));
                users.add(user);
            }
            System.out.println("Loaded " + users.size() + " users from CSV.");

        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    /**
     * Returns all users in the system.
     *
     * @return list of all users
     */
    public static List<User> getAll() {
        return users;
    }

    /**
     * Adds a new user to the repository.
     *
     * @param user the user to add
     */
    public static void add(User user) {
        users.add(user);
    }

    /**
     * Removes a user from the repository by their NRIC.
     *
     * @param nric the NRIC of the user to remove
     */
    public static void remove(String nric) {
        users.removeIf(user -> user.getUserNRIC().equals(nric));
    }

    /**
     * Finds a user by their NRIC.
     *
     * @param nric the NRIC of the user to search for
     * @return the matching user, or <code>null</code> if not found
     */
    public static User getByNRIC(String nric) {
        return users.stream()
                .filter(user -> user.getUserNRIC().equals(nric))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the currently active user.
     *
     * @return the active user, or <code>null</code> if no active user is set
     */
    public static User getActiveUser() {
        return activeUser;
    }

    /**
     * Sets the active user.
     *
     * @param user the user to set as the active user
     */
    public static void setActiveUser(User user) {
        activeUser = user;
    }

    /**
     * Checks if a user is the currently active user.
     *
     * @param user the user to check
     * @return <code>true</code> if the user is active, <code>false</code> if not
     */
    public static boolean isActiveUser(User user) {
        return activeUser != null && activeUser.getUserNRIC().equals(user.getUserNRIC());
    }

    /**
     * Clears the active user.
     */
    public static void clearActiveUser() {
        activeUser = null;
    }

    /**
     * Updates the details of an existing user in the repository.
     *
     * @param user the user with updated information
     */
    public static void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserNRIC().equals(user.getUserNRIC())) {
                users.set(i, user);
                saveAll();
                return;
            }
        }
    }

    /**
     * Returns the role of the active user.
     *
     * @return the role of the active user, or <code>null</code> if no active user is set
     */
    public static Role getUserRole() {
        if (activeUser != null) {
            return activeUser.getRole();
        }
        return null; // Return null if no active user is set
    }

    /**
     * Returns the current user mode (role).
     *
     * @return the current user mode
     */
    public static Role getUserMode() {
        return userMode;
    }

    /**
     * Sets the user mode (role).
     *
     * @param mode the role to set as the user mode
     */
    public static void setUserMode(Role mode) {
        userMode = mode;
    }
}