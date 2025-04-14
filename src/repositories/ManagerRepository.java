package repositories;

import models.Manager;
import models.User;
import utils.CsvReader;
import utils.CsvWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import interfaces.ICsvConfig;

public class ManagerRepository {
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

    private ManagerRepository() {} // private constructor

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

    public static List<Manager> getAll() {
        return managers;
    }

    public static void add(Manager manager) {
        managers.add(manager);
    }

    public static Manager getByNRIC(String nric) {
        return managers.stream()
            .filter(manager -> manager.getUserNRIC().equals(nric))
            .findFirst()
            .orElse(null);
    }

    public static List<Manager> getByProject(String projectId) {
        return managers.stream()
            .filter(manager -> projectId.equals(manager.getCurrentProjectID()))
            .collect(Collectors.toList());
    }
}