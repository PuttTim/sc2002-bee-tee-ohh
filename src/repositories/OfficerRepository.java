package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Officer;
import models.User;

import utils.CsvReader;
import utils.CsvWriter;

/**
 * A repository class for managing officers in the system.
 * Supports loading and saving data to CSV and searching officers by NRIC and project.
 */
public class OfficerRepository {

    /**
     * Configuration for reading and writing the CSV file containing officers.
     */
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

    private OfficerRepository() {}

    /**
     * Saves all officers to the CSV file.
     */
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

    /**
     * Loads all officers from the CSV file.
     */
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

    /**
     * Returns all officers in the system.
     *
     * @return list of all officers
     */
    public static List<Officer> getAll() {
        return officers;
    }

    /**
     * Adds a new officer to the repository.
     *
     * @param officer the officer to add
     */
    public static void add(Officer officer) {
        officers.add(officer);
    }

    /**
     * Finds an officer by NRIC.
     *
     * @param nric the NRIC of the officer to search for
     * @return the matching officer, or <code>null</code> if not found
     */
    public static Officer getByNRIC(String nric) {
        return officers.stream()
                .filter(officer -> officer.getUserNRIC().equals(nric))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a list of officers working on a specific project.
     *
     * @param projectId the ID of the project
     * @return list of officers working on the given project
     */
    public static List<Officer> getByProject(String projectId) {
        return officers.stream()
                .filter(officer -> projectId.equals(officer.getCurrentProjectID()))
                .collect(Collectors.toList());
    }

    /**
     * Checks if an officer has an existing project.
     *
     * @param officer the officer to check
     * @return <code>true</code> if the officer is assigned to a project,
     * <code>false</code> if officer is not assigned to a project
     */
    public static boolean hasExistingProject(Officer officer) {
        String officerNRIC = officer.getUserNRIC();
        boolean withinProjects = ProjectRepository.getAll().stream()
                .anyMatch(project -> project.getOfficers().contains(officerNRIC));

        boolean withinOfficer = getByNRIC(officerNRIC) != null && getByNRIC(officerNRIC).getCurrentProjectID() == null;

        return withinProjects || withinOfficer;
    }

    /**
     * Checks if an officer has an active application.
     *
     * @param officer the officer to check
     * @return <code>true</code> if the officer has an active application,
     * <code>false</code> if officer has no active application
     */
    public static boolean hasActiveApplication(Officer officer) {
        boolean withinApplications = ApplicationRepository.getAll().stream()
                .anyMatch(application -> application.getApplicantNRIC().equals(officer.getUserNRIC()));
        return withinApplications;
    }
}