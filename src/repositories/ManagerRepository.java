package repositories;

import models.Manager;
import models.enums.MaritalStatus;
import models.enums.Role;
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
        public List<String> getHeaders() {
            return List.of("NRIC", "Name", "Password", "Age", "MaritalStatus", "CurrentProjectID");
        }

        @Override
        public String getFilePath() {
            return "data/manager.csv";
        }
    }

    private static List<Manager> managers = new ArrayList<>();

    private ManagerRepository() {} // private constructor

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Manager manager : managers) {
            Map<String, String> record = new HashMap<>();
            record.put("NRIC", manager.getUserNRIC());
            record.put("Name", manager.getName());
            record.put("Password", manager.getPassword());
            record.put("Age", String.valueOf(manager.getAge()));
            record.put("MaritalStatus", manager.getMaritalStatus().toString());
            record.put("CurrentProjectID", manager.getCurrentProjectID());
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
                Manager manager = new Manager(
                    record.get("NRIC"),
                    record.get("Name"),
                    record.get("Password"),
                    Integer.parseInt(record.get("Age")),
                    record.get("CurrentProjectID")
                );
                manager.setMaritalStatus(MaritalStatus.valueOf(record.get("MaritalStatus")));
                managers.add(manager);
            }
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