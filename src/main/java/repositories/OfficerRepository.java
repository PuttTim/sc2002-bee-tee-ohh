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
 * Repository class responsible for managing officer data.
 * <p>
 * This class handles the loading, saving, and retrieval of officer data from and to a CSV file.
 * It supports managing information about officers, including the projects they are assigned to and the projects they have applied for.
 * </p>
 */
public class OfficerRepository {

    /**
     * Config class for reading and writing officer data in CSV format.
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

    // List to store officers
    private static List<Officer> officers = new ArrayList<>();

    // Private constructor to prevent instantiation
    private OfficerRepository() {}

    /**
     * Saves all officers to the CSV file.
     * <p>
     * Each officer is written with their NRIC, associated project ID, and applied projects.
     * </p>
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
     * Loads officers from the CSV file and populates the officers list.
     * <p>
     * Each record in the CSV is used to create an officer object, associating the officer with their applied projects and current project.
     * </p>
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
     * Retrieves all officers.
     *
     * @return a list of all officers
     */
    public static List<Officer> getAll() {
        return officers;
    }

    /**
     * Adds a new officer to the repository.
     *
     * @param officer the officer to be added
     */
    public static void add(Officer officer) {
        officers.add(officer);
    }

    /**
     * Retrieves an officer by their NRIC.
     *
     * @param nric the NRIC of the officer
     * @return the officer with the specified NRIC, or {@code null} if not found
     */
    public static Officer getByNRIC(String nric) {
        return officers.stream()
                .filter(officer -> officer.getUserNRIC().equals(nric))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a list of officers assigned to a specific project.
     *
     * @param projectId the project ID
     * @return a list of officers associated with the specified project
     */
    public static List<Officer> getByProject(String projectId) {
        return officers.stream()
                .filter(officer -> projectId.equals(officer.getCurrentProjectID()))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given officer is involved in any project, either through an existing project assignment or by applying to a project.
     *
     * @param officer the officer to check
     * @return {@code true} if the officer is involved in any project, {@code false} otherwise
     */
    public static boolean hasExistingProject(Officer officer) {
        String officerNRIC = officer.getUserNRIC();
        boolean withinProjects = ProjectRepository.getAll().stream()
                .anyMatch(project -> project.getOfficers().contains(officerNRIC));

        boolean withinOfficer = getByNRIC(officerNRIC) != null && getByNRIC(officerNRIC).getCurrentProjectID() == null;

        return withinProjects || withinOfficer;
    }

    /**
     * Checks if the given officer has an active application.
     *
     * @param officer the officer to check
     * @return {@code true} if the officer has an active application, {@code false} otherwise
     */
    public static boolean hasActiveApplication(Officer officer) {
        boolean withinApplications = ApplicationRepository.getAll().stream()
                .anyMatch(application -> application.getApplicantNRIC().equals(officer.getUserNRIC()));
        return withinApplications;
    }
}
