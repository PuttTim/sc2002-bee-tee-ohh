package repositories;

import interfaces.ICsvConfig;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import models.Application;
import models.enums.ApplicationStatus;
import models.enums.FlatType;

import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;

public class ApplicationRepository {
    private static class ApplicationCsvConfig implements ICsvConfig {
        @Override
        public String getFilePath() {
            return "data/applications.csv";
        }

        @Override
        public List<String> getHeaders() {
            return List.of("ApplicationID", "ApplicantNRIC", "ProjectID", "SelectedFlatType", 
                          "ApplicationStatus", "IsWithdrawalRequested", "ApplicationDate", 
                          "ApprovedBy", "ApplicationStatusHistory");
        }
    }

    private static ApplicationRepository instance;
    private static List<Application> applications = new ArrayList<>();

    private ApplicationRepository() {} 

    public static ApplicationRepository getInstance() {
        if (instance == null) {
            instance = new ApplicationRepository();
            load();
        }
        return instance;
    }

    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Application application : applications) {
            Map<String, String> record = new HashMap<>();
            record.put("ApplicationID", application.getApplicationID());
            record.put("ApplicantNRIC", application.getApplicantNRIC());
            record.put("ProjectID", application.getProjectId());
            record.put("SelectedFlatType", application.getSelectedFlatType().toString());
            record.put("ApplicationStatus", application.getApplicationStatus().toString());
            record.put("IsWithdrawalRequested", String.valueOf(application.isWithdrawalRequested()));
            record.put("ApplicationDate", DateTimeUtils.formatDateTime(application.getApplicationDate()));
            record.put("ApprovedBy", application.getApprovedBy() != null ? application.getApprovedBy() : "");
            
            // Convert status history to string
            String statusHistory = application.getApplicationStatusHistory().entrySet().stream()
                .map(entry -> entry.getKey() + ";" + DateTimeUtils.formatDateTime(entry.getValue()))
                .collect(Collectors.joining("/"));
            record.put("ApplicationStatusHistory", statusHistory);
            
            records.add(record);
        }

        try {
            CsvWriter.write(new ApplicationCsvConfig(), records);
        } catch (IOException e) {
            System.err.println("Error saving applications: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ApplicationCsvConfig());
            applications = new ArrayList<>();

            for (Map<String, String> record : records) {
                // Parse status history
                Map<ApplicationStatus, LocalDateTime> statusHistory = new HashMap<>();
                if (record.get("ApplicationStatusHistory") != null && !record.get("ApplicationStatusHistory").isEmpty()) {
                    Arrays.stream(record.get("ApplicationStatusHistory").split("/"))
                        .forEach(entry -> {
                            String[] parts = entry.split(";");
                            statusHistory.put(
                                ApplicationStatus.valueOf(parts[0]),
                                DateTimeUtils.parseDateTime(parts[1])
                            );
                        });
                }

                Application application = new Application(
                    record.get("ApplicationID"),
                    record.get("ApplicantNRIC"),
                    record.get("ProjectID"),
                    FlatType.valueOf(record.get("SelectedFlatType")),
                    ApplicationStatus.valueOf(record.get("ApplicationStatus")),
                    Boolean.parseBoolean(record.get("IsWithdrawalRequested")),
                    DateTimeUtils.parseDateTime(record.get("ApplicationDate")),
                    record.get("ApprovedBy").isEmpty() ? null : record.get("ApprovedBy"),
                    statusHistory
                );
                applications.add(application);
            }
            System.out.println("Loaded " + applications.size() + " applications from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading applications: " + e.getMessage());
        }
    }

    public static List<Application> getAll() {
        return applications;
    }

    public static void add(Application application) {
        applications.add(application);
    }

    public static Application getById(String applicationId) {
        return applications.stream()
            .filter(app -> app.getApplicationID().equals(applicationId))
            .findFirst()
            .orElse(null);
    }

    public static List<Application> getByApplicant(String applicantNRIC) {
        return applications.stream()
            .filter(app -> app.getApplicantNRIC().equals(applicantNRIC))
            .collect(Collectors.toList());
    }

    public static List<Application> getByProject(String projectId) {
        return applications.stream()
            .filter(app -> app.getProjectId().equals(projectId))
            .collect(Collectors.toList());
    }

    public static List<Application> getByStatus(ApplicationStatus status) {
        return applications.stream()
            .filter(app -> app.getApplicationStatus() == status)
            .collect(Collectors.toList());
    }
}