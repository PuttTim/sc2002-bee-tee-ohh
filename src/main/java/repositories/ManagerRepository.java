package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Manager;
import models.User;

import utils.CsvReader;
import utils.CsvWriter;

/**
 * Repository class responsible for managing manager data.
 * <p>
 * This class handles the loading, saving, and retrieval of manager data from and to a CSV file.
 * It supports managing information about managers, including associating them with a project.
 * </p>
 */
public class ManagerRepository {

    /**
     * Config class for reading and writing manager data in CSV format.
     */
    private static class ManagerCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/manager.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("ManagerNRIC", "CurrentProjectID");
        }
    }

    // List to store managers
    private static List<Manager> managers = new ArrayList<>();

    // Private constructor to prevent instantiation
    private ManagerRepository() {}

    /**
     * Saves all managers to the CSV file.
     * <p>
     * Each manager is written with their NRIC and associated project ID.
     * </p>
     */
    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Manager manager : managers) {
            Map<String, String> record = new HashMap<>();
            record.put("ManagerNRIC", manager.getUserNRIC());
            record.put("CurrentProjectID", manager.getCurrentProjectID() != null ? manager.getCurrentProjectID() : "");
            records.add(record);
        }

        try {
            CsvWriter.write(new ManagerCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving managers: " + e.getMessage());
        }
    }

    /**
     * Loads managers from the CSV file and populates the managers list.
     * <p>
     * Each record in the CSV is used to create a manager object, associating the manager with a project.
     * </p>
     */
    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ManagerCsvConfig());
            managers = new ArrayList<>();

            for (Map<String, String> record : records) {
                // Get user data from UserRepository
                User userData = UserRepository.getByNRIC(record.get("ManagerNRIC"));
                if (userData == null) {
                    System.err.println("Manager NRIC not found in users.csv: " + record.get("ManagerNRIC"));
                    continue;
                }

                Manager manager = new Manager(
                        userData.getUserNRIC(),
                        userData.getName(),
                        userData.getPassword(),
                        userData.getAge(),
                        record.get("CurrentProjectID")
                );
                manager.setMaritalStatus(userData.getMaritalStatus());
                managers.add(manager);
            }
            System.out.println("Loaded " + managers.size() + " managers from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading managers: " + e.getMessage());
        }
    }

    /**
     * Retrieves all managers.
     *
     * @return a list of all managers
     */
    public static List<Manager> getAll() {
        return managers;
    }

    /**
     * Adds a new manager to the repository.
     *
     * @param manager the manager to be added
     */
    public static void add(Manager manager) {
        managers.add(manager);
    }

    /**
     * Retrieves a manager by their NRIC.
     *
     * @param nric the NRIC of the manager
     * @return the manager with the specified NRIC, or {@code null} if not found
     */
    public static Manager getByNRIC(String nric) {
        return managers.stream()
                .filter(manager -> manager.getUserNRIC().equals(nric))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a list of managers assigned to a specific project.
     *
     * @param projectId the project ID
     * @return a list of managers associated with the specified project
     */
    public static List<Manager> getByProject(String projectId) {
        return managers.stream()
                .filter(manager -> projectId.equals(manager.getCurrentProjectID()))
                .collect(Collectors.toList());
    }
}
