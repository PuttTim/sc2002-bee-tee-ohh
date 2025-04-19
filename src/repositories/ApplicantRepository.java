package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Applicant;
import models.User;

import utils.CsvReader;
import utils.CsvWriter;

public class ApplicantRepository {
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

    private ApplicantRepository() {} // private constructor

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

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ApplicantCsvConfig());
            applicants = new ArrayList<>();

            for (Map<String, String> record : records) {
                // gets base user data from UserRepository
                User userData = UserRepository.getByNRIC(record.get("ApplicantNRIC"));
                if (userData == null) {
                    System.err.println("Applicant NRIC not found in users.csv: " + record.get("ApplicantNRIC"));
                    continue;
                }

                List<String> appliedProjects = new ArrayList<>();
                if (record.get("AppliedProjects") != null && !record.get("AppliedProjects").isEmpty()) {
                    appliedProjects = Arrays.asList(record.get("AppliedProjects").split("/"));
                }

                // Create applicant with user data and applied projects
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

    public static List<Applicant> getAll() {
        return applicants;
    }

    public static void add(Applicant applicant) {
        applicants.add(applicant);
    }

    public static Applicant getByNRIC(String nric) {
        return applicants.stream()
            .filter(applicant -> applicant.getUserNRIC().equals(nric))
            .findFirst()
            .orElse(null);
    }

    public static List<Applicant> getByProject(String projectId) {
        return applicants.stream()
            .filter(applicant -> applicant.getAppliedProjects().contains(projectId))
            .collect(Collectors.toList());
    }
}