package repositories;

import models.Officer;
import models.User;
import utils.CsvReader;
import utils.CsvWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import interfaces.ICsvConfig;

public class OfficerRepository {
    private static class OfficerCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/officers.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("OfficerNRIC", "CurrentProjectID");
        }
    }

    private static List<Officer> officers = new ArrayList<>();

    private OfficerRepository() {} // private constructor

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Officer officer : officers) {
            Map<String, String> record = new HashMap<>();
            record.put("OfficerNRIC", officer.getUserNRIC());
            record.put("CurrentProjectID", officer.getCurrentProjectID() != null ? officer.getCurrentProjectID() : "");
            record.put("AppliedProjects", String.join("/", officer.getAppliedProjects()));
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
                // Get user data from UserRepository
                User userData = UserRepository.getByNRIC(record.get("OfficerNRIC"));
                if (userData == null) {
                    System.err.println("Officer NRIC not found in users.csv: " + record.get("OfficerNRIC"));
                    continue;
                }

                List<String> appliedProjects = new ArrayList<>();
                if (record.get("AppliedProjects") != null && !record.get("AppliedProjects").isEmpty()) {
                    appliedProjects = Arrays.asList(record.get("AppliedProjects").split("/"));
                }

                Officer officer = new Officer(
                    userData.getUserNRIC(),
                    userData.getName(),
                    userData.getPassword(),
                    userData.getAge(),
                    appliedProjects
                );
                officer.setMaritalStatus(userData.getMaritalStatus());
                
                String projectId = record.get("CurrentProjectID");
                if (projectId != null && !projectId.trim().isEmpty()) {
                    officer.setCurrentProjectID(projectId);
                }
                
                officers.add(officer);
            }
            System.out.println("Loaded " + officers.size() + " officers from CSV.");
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
        boolean withinProjects = ProjectRepository.getAll().stream()
            .anyMatch(project -> project.getOfficers().contains(officerNRIC));

        boolean withinOfficer = getByNRIC(officerNRIC) != null && getByNRIC(officerNRIC).getCurrentProjectID() == null;

        return !withinProjects && !withinOfficer;
    }
}