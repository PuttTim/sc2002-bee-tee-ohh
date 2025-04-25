package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;

import models.User;
import models.enums.MaritalStatus;
import models.enums.Role;

import utils.CsvReader;
import utils.CsvWriter;

/**
 * Repository for managing users, including loading from and saving to a CSV file.
 * <p>
 * This class provides functionality for handling user data, such as adding, removing, and updating users.
 * It also manages the active user and their role within the application.
 * </p>
 */
public class UserRepository {

    /**
     * Configuration for reading and writing the user CSV file.
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
    private static final UserCsvConfig csvConfig = new UserCsvConfig();

    private UserRepository() {}

    /**
     * Saves all users to the CSV file.
     * <p>
     * The method iterates over all users, converts them to a map representation, and writes them
     * to the CSV file using the configured CSV writer.
     * </p>
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
     * Loads users from the CSV file.
     * <p>
     * This method reads the CSV file, parses each record, and converts it into a `User` object.
     * </p>
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
     * Retrieves all users.
     *
     * @return a list of all users
     */
    public static List<User> getAll() {
        return users;
    }

    /**
     * Adds a new user to the repository.
     * <p>
     * The method checks if a user with the same NRIC already exists before adding. If a user with the same
     * NRIC exists, an error message is displayed.
     * </p>
     *
     * @param user the user to add
     */
    public static void add(User user) {
        if (getByNRIC(user.getUserNRIC()) == null) {
            users.add(user);
            saveAll();
        } else {
            System.err.println("User with NRIC " + user.getUserNRIC() + " already exists.");
        }
    }

    /**
     * Removes a user by their NRIC.
     * <p>
     * The method removes the user from the list and saves the updated list to the CSV file.
     * </p>
     *
     * @param nric the NRIC of the user to remove
     */
    public static void remove(String nric) {
        boolean removed = users.removeIf(user -> user.getUserNRIC().equals(nric));
        if (removed) {
            saveAll();
        }
    }

    /**
     * Retrieves a user by their NRIC.
     *
     * @param nric the NRIC of the user
     * @return the user with the specified NRIC, or null if no such user exists
     */
    public static User getByNRIC(String nric) {
        return users.stream()
                .filter(user -> user.getUserNRIC().equals(nric))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the active user.
     *
     * @return the active user, or null if no active user is set
     */
    public static User getActiveUser() {
        return activeUser;
    }

    /**
     * Sets the active user.
     *
     * @param user the user to set as active
     */
    public static void setActiveUser(User user) {
        activeUser = user;
    }

    /**
     * Checks if the specified user is the active user.
     *
     * @param user the user to check
     * @return true if the user is the active user, false otherwise
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
     * Updates an existing user in the repository.
     * <p>
     * The method searches for the user by their NRIC and replaces the existing user with the new one.
     * The updated list of users is then saved to the CSV file.
     * </p>
     *
     * @param user the user to update
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
     * Retrieves the role of the active user.
     *
     * @return the role of the active user, or null if no active user is set
     */
    public static Role getUserRole() {
        if (activeUser != null) {
            return activeUser.getRole();
        }
        return null;
    }

    /**
     * Retrieves the current user mode (role).
     *
     * @return the current user mode (role)
     */
    public static Role getUserMode() {
        return userMode;
    }

    /**
     * Sets the user mode (role).
     *
     * @param mode the role to set as the current user mode
     */
    public static void setUserMode(Role mode) {
        userMode = mode;
    }
}
