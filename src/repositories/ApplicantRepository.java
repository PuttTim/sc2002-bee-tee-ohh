package repositories;

import models.Applicant;
import models.enums.MaritalStatus;
import models.enums.Role;
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

public class ApplicantRepository {
    private static class ApplicantCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/applicants.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("NRIC", "Name", "Password", "Age", "MaritalStatus", "AppliedProjects");
        }
    }

    private static List<Applicant> applicants = new ArrayList<>();

    private ApplicantRepository() {} // private constructor

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Applicant applicant : applicants) {
            Map<String, String> record = new HashMap<>();
            record.put("NRIC", applicant.getUserNRIC());
            record.put("Name", applicant.getName());
            record.put("Password", applicant.getPassword());
            record.put("Age", String.valueOf(applicant.getAge()));
            record.put("MaritalStatus", applicant.getMaritalStatus().toString());
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
                List<String> appliedProjects = new ArrayList<>();
                if (record.get("AppliedProjects") != null && !record.get("AppliedProjects").isEmpty()) {
                    appliedProjects = Arrays.asList(record.get("AppliedProjects").split("/"));
                }

                Applicant applicant = new Applicant(
                    record.get("NRIC"),
                    record.get("Name"),
                    record.get("Password"),
                    Integer.parseInt(record.get("Age")),
                    appliedProjects
                );
                applicant.setMaritalStatus(MaritalStatus.valueOf(record.get("MaritalStatus")));
                applicants.add(applicant);
            }
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