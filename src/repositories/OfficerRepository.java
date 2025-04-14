package repositories;

import models.Officer;
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

public class OfficerRepository {
    private static class OfficerCsvConfig implements ICsvConfig {
        @Override
        public List<String> getHeaders() {
            return List.of("NRIC", "Name", "Password", "Age", "MaritalStatus", "CurrentProjectID");
        }

        @Override
        public String getFilePath() {
            return "data/officers.csv";
        }
    }

    private static List<Officer> officers = new ArrayList<>();

    private OfficerRepository() {} // private constructor

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Officer officer : officers) {
            Map<String, String> record = new HashMap<>();
            record.put("NRIC", officer.getUserNRIC());
            record.put("Name", officer.getName());
            record.put("Password", officer.getPassword());
            record.put("Age", String.valueOf(officer.getAge()));
            record.put("MaritalStatus", officer.getMaritalStatus().toString());
            record.put("CurrentProjectID", officer.getCurrentProjectID());
            records.add(record);
        }

        try {
            CsvWriter.write(new OfficerCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving officers: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new OfficerCsvConfig());
            officers = new ArrayList<>();

            for (Map<String, String> record : records) {
                Officer officer = new Officer(
                    record.get("NRIC"),
                    record.get("Name"),
                    record.get("Password"),
                    Integer.parseInt(record.get("Age"))
                );
                officer.setMaritalStatus(MaritalStatus.valueOf(record.get("MaritalStatus")));
                officer.setCurrentProjectID(record.get("CurrentProjectID"));
                officers.add(officer);
            }
        } catch (IOException e) {
            System.err.println("Error loading officers: " + e.getMessage());
        }
    }

    public static List<Officer> getAll() {
        return officers;
    }

    public static void add(Officer officer) {
        officers.add(officer);
    }

    public static Officer getByNRIC(String nric) {
        return officers.stream()
            .filter(officer -> officer.getUserNRIC().equals(nric))
            .findFirst()
            .orElse(null);
    }

    public static List<Officer> getByProject(String projectId) {
        return officers.stream()
            .filter(officer -> projectId.equals(officer.getCurrentProjectID()))
            .collect(Collectors.toList());
    }

    public static boolean hasExistingProject(String officerNRIC) {
        return officers.stream()
            .anyMatch(officer -> officer.getUserNRIC().equals(officerNRIC) 
                && officer.getCurrentProjectID() != null);
    }
}