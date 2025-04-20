package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;

import models.Applicant;
import models.User;
import models.enums.MaritalStatus;
import models.enums.Role;

import utils.CsvReader;
import utils.CsvWriter;

public class UserRepository {
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

    private UserRepository() {} // private constructor

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

    public static List<User> getAll() {
        return users;
    }

    public static void add(User user) {
        if (getByNRIC(user.getUserNRIC()) == null) {
            users.add(user);
            saveAll();
        } else {
            System.err.println("User with NRIC " + user.getUserNRIC() + " already exists.");
        }
    }

    public static void remove(String nric) {
        boolean removed = users.removeIf(user -> user.getUserNRIC().equals(nric));
        if (removed) {
            saveAll();
        }
    }

    public static User getByNRIC(String nric) {
        return users.stream()
            .filter(user -> user.getUserNRIC().equals(nric))
            .findFirst()
            .orElse(null);
    }

    public static User getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(User user) {
        activeUser = user;
    }

    public static boolean isActiveUser(User user) {
        return activeUser != null && activeUser.getUserNRIC().equals(user.getUserNRIC());
    }

    public static void clearActiveUser() {
        activeUser = null;
    }

    public static void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserNRIC().equals(user.getUserNRIC())) {
                users.set(i, user);
                saveAll();
                return;
            }
        }
    }

    public static Role getUserRole() {
        if (activeUser != null) {
            return activeUser.getRole();
        }
        return null;
    }

    public static Role getUserMode() {
        return userMode;
    }

    public static void setUserMode(Role mode) {
        userMode = mode;
    }
}
