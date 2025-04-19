package repositories;

import models.Applicant;
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

/**
 * A repository class for managing applicants.
 * Handles loading from and saving to CSV and searching applicants.
 */
public class ApplicantRepository {

    /**
     * Configuration for reading and writing the CSV file containing applicants.
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

    private static List<Applicant> applicants = new ArrayList<>();

    private ApplicantRepository() {}

    /**
     * Saves all applicants to the CSV file.
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
     * Loads applicants from the CSV file.
     * Fetches user data from the UserRepository.
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
     * Returns all applicants in the system.
     *
     * @return list of all applicants
     */
    public static List<Applicant> getAll() {
        return applicants;
    }

    /**
     * Adds a new applicant to the repository.
     *
     * @param applicant the applicant to add
     */
    public static void add(Applicant applicant) {
        applicants.add(applicant);
    }

    /**
     * Finds an applicant by NRIC.
     *
     * @param nric the NRIC to search for
     * @return the matching applicant, or <code>null</code> if applicant not found
     */
    public static Applicant getByNRIC(String nric) {
        return applicants.stream()
                .filter(applicant -> applicant.getUserNRIC().equals(nric))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all applicants who applied for a specific project.
     *
     * @param projectId the project ID to search for
     * @return list of applicants who applied for that project
     */
    public static List<Applicant> getByProject(String projectId) {
        return applicants.stream()
                .filter(applicant -> applicant.getAppliedProjects().contains(projectId))
                .collect(Collectors.toList());
    }
}
