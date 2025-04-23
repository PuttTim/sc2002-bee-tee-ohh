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
 * A repository class for managing managers in the system.
 * Supports loading and saving data to CSV and searching managers by NRIC and project.
 */
public class ManagerRepository {

    /**
     * Configuration for reading and writing the CSV file containing managers.
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

    private static List<Manager> managers = new ArrayList<>();

    private ManagerRepository() {}

    /**
     * Saves all managers to the CSV file.
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
     * Loads all managers from the CSV file.
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
     * Returns all managers in the system.
     *
     * @return list of all managers
     */
    public static List<Manager> getAll() {
        return managers;
    }

    /**
     * Adds a new manager to the repository.
     *
     * @param manager the manager to add
     */
    public static void add(Manager manager) {
        managers.add(manager);
    }

    /**
     * Finds a manager by NRIC.
     *
     * @param nric the NRIC of the manager to search for
     * @return the matching manager, or <code>null</code> if not found
     */
    public static Manager getByNRIC(String nric) {
        return managers.stream()
                .filter(manager -> manager.getUserNRIC().equals(nric))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a list of managers working on a specific project.
     *
     * @param projectId the ID of the project
     * @return list of managers working on the given project
     */
    public static List<Manager> getByProject(String projectId) {
        return managers.stream()
                .filter(manager -> projectId.equals(manager.getCurrentProjectID()))
                .collect(Collectors.toList());
    }
}