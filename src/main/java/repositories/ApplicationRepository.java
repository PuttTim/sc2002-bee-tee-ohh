package repositories;

import models.Applicant;
import models.Application;
import models.enums.ApplicationStatus;
import models.enums.FlatType;
import utils.CsvReader;
import utils.CsvWriter;
import utils.DateTimeUtils;
import interfaces.ICsvConfig;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository class responsible for managing application data.
 * <p>
 * This class provides methods for loading, saving, and accessing application data from and to CSV files.
 * It allows searching applications by various criteria, such as applicant NRIC, project ID, or application status.
 * </p>
 */
public class ApplicationRepository {

    /**
     * Config class for reading and writing application data in CSV format.
     */
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

    // Singleton instance of the repository
    private static ApplicationRepository instance;

    // List to store applications
    private static List<Application> applications = new ArrayList<>();

    // Private constructor to prevent instantiation
    private ApplicationRepository() {}

    /**
     * Returns the singleton instance of the ApplicationRepository.
     * <p>
     * If the instance does not exist, it is created and the repository data is loaded.
     * </p>
     *
     * @return the singleton instance of the ApplicationRepository
     */
    public static ApplicationRepository getInstance() {
        if (instance == null) {
            instance = new ApplicationRepository();
            load();
        }
        return instance;
    }

    /**
     * Saves all applications to the CSV file.
     * <p>
     * Each application is written in CSV format, including details like application ID, applicant NRIC,
     * selected flat type, status, and status history.
     * </p>
     */
    public static void saveAll() {
        List<Map<String, String>> records = new ArrayList<>();
        for (Application application : applications) {
            Map<String, String> record = new HashMap<>();
            record.put("ApplicationID", application.getApplicationID());
            record.put("ApplicantNRIC", application.getApplicantNRIC());
            record.put("ProjectID", application.getProjectId());
            record.put("SelectedFlatType", application.getSelectedFlatType().toString());
            record.put("ApplicationStatus", application.getApplicationStatus().getKey());
            record.put("IsWithdrawalRequested", String.valueOf(application.isWithdrawalRequested()));
            record.put("ApplicationDate", DateTimeUtils.formatDateTime(application.getApplicationDate()));
            record.put("ApprovedBy", application.getApprovedBy() != null ? application.getApprovedBy() : "");
            
            String statusHistory = application.getApplicationStatusHistory().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().getKey() + ";" + DateTimeUtils.formatDateTime(entry.getValue()))
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

    /**
     * Loads applications from the CSV file and populates the applications list.
     * <p>
     * Each record in the CSV is used to create an application object, including parsing status history and other fields.
     * </p>
     */
    public static void load() {
        try {
            List<Map<String, String>> records = CsvReader.read(new ApplicationCsvConfig());
            applications = new ArrayList<>();

            for (Map<String, String> record : records) {
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

    /**
     * Retrieves all applications.
     *
     * @return a list of all applications
     */
    public static List<Application> getAll() {
        return applications;
    }

    /**
     * Adds a new application to the repository.
     *
     * @param application the application to be added
     */
    public static void add(Application application) {
        applications.add(application);
    }

    /**
     * Retrieves an application by its ID.
     *
     * @param applicationId the ID of the application
     * @return the application with the specified ID, or {@code null} if not found
     */
    public static Application getById(String applicationId) {
        return applications.stream()
                .filter(app -> app.getApplicationID().equals(applicationId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a list of applications submitted by a specific applicant.
     *
     * @param applicantNRIC the NRIC of the applicant
     * @return a list of applications submitted by the specified applicant
     */
    public static List<Application> getByApplicant(String applicantNRIC) {
        return applications.stream()
                .filter(app -> app.getApplicantNRIC().equals(applicantNRIC))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of applications for a specific project.
     *
     * @param projectId the project ID
     * @return a list of applications for the specified project
     */
    public static List<Application> getByProject(String projectId) {
        return applications.stream()
                .filter(app -> app.getProjectId().equals(projectId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of applications with a specific application status.
     *
     * @param status the application status
     * @return a list of applications with the specified status
     */
    public static List<Application> getByStatus(ApplicationStatus status) {
        return applications.stream()
                .filter(app -> app.getApplicationStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a specific applicant has already submitted an application for a particular project.
     *
     * @param applicant the applicant
     * @param projectId the project ID
     * @return {@code true} if the applicant has applied for the project, {@code false} otherwise
     */
    public static boolean hasApplication(Applicant applicant, String projectId) {
        return getByProject(projectId).stream()
                .anyMatch(app -> app.getApplicantNRIC().equals(applicant.getUserNRIC()));
    }
}
