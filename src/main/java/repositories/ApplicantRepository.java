package repositories;

import interfaces.ICsvConfig;
import models.Applicant;
import models.User;
import utils.CsvReader;
import utils.CsvWriter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository class responsible for managing applicants in the system.
 * <p>
 * This class handles loading, saving, and accessing applicants' data from and to CSV files.
 * It provides methods for adding, retrieving, and searching applicants based on NRIC or applied projects.
 * </p>
 */
public class ApplicantRepository {

    /**
     * Config class for reading and writing applicant data in CSV format.
     */
    private static class ApplicantCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/applicants.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("ApplicantNRIC", "AppliedProjects");
        }
    }

    // List to store applicants
    private static List<Applicant> applicants = new ArrayList<>();

    // Private constructor to prevent instantiation
    private ApplicantRepository() {}

    /**
     * Saves all applicants to the CSV file.
     * <p>
     * The applicants are written in CSV format with each applicant's NRIC and list of applied projects.
     * </p>
     */
    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Applicant applicant : applicants) {
            Map<String, String> record = new HashMap<>();
            record.put("ApplicantNRIC", applicant.getUserNRIC());
            record.put("AppliedProjects", String.join("/", applicant.getAppliedProjects()));
            records.add(record);
        }

        try {
            CsvWriter.write(new ApplicantCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving applicants: " + e.getMessage());
        }
    }

    /**
     * Loads applicants from the CSV file and populates the applicants list.
     * <p>
     * The method fetches user data from the `UserRepository` and combines it with applicants' applied projects
     * to create the full applicant objects.
     * </p>
     */
    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ApplicantCsvConfig());
            applicants = new ArrayList<>();

            for (Map<String, String> record : records) {
                User userData = UserRepository.getByNRIC(record.get("ApplicantNRIC"));
                if (userData == null) {
                    System.err.println("Applicant NRIC not found in users.csv: " + record.get("ApplicantNRIC"));
                    continue;
                }

                List<String> appliedProjects = new ArrayList<>();
                if (record.get("AppliedProjects") != null && !record.get("AppliedProjects").isEmpty()) {
                    appliedProjects = Arrays.asList(record.get("AppliedProjects").split("/"));
                }

                Applicant applicant = new Applicant(
                        userData.getUserNRIC(),
                        userData.getName(),
                        userData.getPassword(),
                        userData.getAge(),
                        appliedProjects
                );
                applicant.setMaritalStatus(userData.getMaritalStatus());
                applicants.add(applicant);
            }
            System.out.println("Loaded " + applicants.size() + " applicants from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading applicants: " + e.getMessage());
        }
    }

    /**
     * Retrieves all applicants.
     *
     * @return a list of all applicants
     */
    public static List<Applicant> getAll() {
        return applicants;
    }

    /**
     * Adds a new applicant to the repository.
     *
     * @param applicant the applicant to be added
     */
    public static void add(Applicant applicant) {
        applicants.add(applicant);
    }

    /**
     * Retrieves an applicant by their NRIC.
     *
     * @param nric the NRIC of the applicant
     * @return the applicant with the specified NRIC, or {@code null} if not found
     */
    public static Applicant getByNRIC(String nric) {
        return applicants.stream()
                .filter(applicant -> applicant.getUserNRIC().equals(nric))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a list of applicants who have applied for a specific project.
     *
     * @param projectId the project ID
     * @return a list of applicants who applied for the specified project
     */
    public static List<Applicant> getByProject(String projectId) {
        return applicants.stream()
                .filter(applicant -> applicant.getAppliedProjects().contains(projectId))
                .collect(Collectors.toList());
    }
}
