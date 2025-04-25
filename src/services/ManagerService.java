package services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import interfaces.ICsvConfig;
import interfaces.IManagerService;
import models.Applicant;
import models.Application;
import models.Project;
import models.enums.ApplicationStatus;
import models.enums.FlatType;
import models.enums.MaritalStatus;
import repositories.ApplicantRepository;
import repositories.ApplicationRepository;
import repositories.ProjectRepository;
import utils.CsvWriter;
import utils.DateTimeUtils;
import views.ManagerView;

/**
 * Service class for managing operations related to managers,
 * such as generating reports and exporting data to CSV files.
 */
public class ManagerService implements IManagerService {
    private static ManagerService instance;
    
    private ManagerService() {}

    /**
     * Returns the singleton instance of the ManagerService class.
     * If an instance of ManagerService does not already exist, it creates a new instance.
     *
     * @return The singleton instance of ManagerService.
     */
    public static ManagerService getInstance() {
        if (instance == null) {
            instance = new ManagerService();
        }
        return instance;
    }


    /**
     * Generates a report of applicants for the specified project with optional filters.
     *
     * @param project The project for which the applicant report is generated.
     * @param filters A map of filter criteria for filtering the applicants (e.g., marital status, flat type).
     * @return A list of maps, where each map represents a row of the applicant report.
     */
    @Override
    public List<Map<String, String>> generateApplicantReport(Project project, Map<String, String> filters) {
        List<Application> bookedApplications = ApplicationRepository.getAll().stream()
                .filter(app -> app.getProjectId().equals(project.getProjectID())) // Filter by project ID
                .filter(app -> app.getApplicationStatus() == ApplicationStatus.BOOKED)
                .collect(Collectors.toList());

        Stream<Application> filteredStream = bookedApplications.stream();

        if (filters.containsKey("maritalStatus")) {
            MaritalStatus filterStatus = MaritalStatus.valueOf(filters.get("maritalStatus"));
            filteredStream = filteredStream.filter(app -> {
                Applicant applicant = ApplicantRepository.getByNRIC(app.getApplicantNRIC());
                return applicant != null && applicant.getMaritalStatus() == filterStatus;
            });
        }

        if (filters.containsKey("flatType")) {
            FlatType filterType = FlatType.valueOf(filters.get("flatType"));
            filteredStream = filteredStream.filter(app -> app.getSelectedFlatType() == filterType);
        }

        List<Application> finalFilteredApplications = filteredStream.collect(Collectors.toList());

        List<Map<String, String>> reportData = new ArrayList<>();
        for (Application app : finalFilteredApplications) {
            Applicant applicant = ApplicantRepository.getByNRIC(app.getApplicantNRIC());
            if (applicant != null) { 
                Map<String, String> row = new HashMap<>();
                row.put("applicantNRIC", applicant.getUserNRIC());
                row.put("applicantName", applicant.getName());
                row.put("age", String.valueOf(applicant.getAge()));
                row.put("maritalStatus", applicant.getMaritalStatus().name());
                row.put("projectName", project.getProjectName()); // Use the passed project name
                row.put("flatType", app.getSelectedFlatType().getDescription());
                reportData.add(row);
            }
        }
        return reportData;
    }

    /**
     * Exports the provided report data to a CSV file.
     *
     * @param reportData The data to be exported to a CSV file.
     * @param filename The name of the file to save the report to.
     */
    @Override
    public void exportReportToCsv(List<Map<String, String>> reportData, String filename) {
        ICsvConfig config = new ICsvConfig() {
            /**
             * Returns the file path for saving the CSV report.
             *
             * This method generates a unique file path by appending the current date and time to the provided filename.
             * It ensures the file path ends with a `.csv` extension and is stored in the "reports" directory.
             *
             * @return The file path, including the filename with timestamp, where the CSV report will be saved.
             */
            @Override
            public String getFilePath() {
                LocalDateTime currentTime = DateTimeUtils.getCurrentDateTime();
                String modifiedFilename = filename.concat(
                    String.format(" %d-%d-%d-%d-%d-%d", 
                    currentTime.getYear(), 
                    currentTime.getMonthValue(), 
                    currentTime.getDayOfMonth(), 
                    currentTime.getHour(), 
                    currentTime.getMinute(), 
                    currentTime.getSecond())
                );
                if (modifiedFilename.endsWith(".csv")) {
                    return "reports/" + modifiedFilename;
                } else {
                    return "reports/" + modifiedFilename + ".csv";
                }
            }

            /**
             * Returns the headers for the CSV report.
             *
             * This method provides a list of column headers to be used in the CSV file when exporting data.
             * These headers correspond to the key names in the report data and help format the CSV output appropriately.
             *
             * @return A list of strings representing the column headers for the CSV file.
             */
            @Override
            public List<String> getHeaders() {
                return List.of("applicantNRIC", "applicantName", "age", "maritalStatus", "projectName", "flatType");
            }
        };

        try {
            CsvWriter.write(config, reportData);
            ManagerView.displayExportSuccess(filename);
        } catch (IOException e) {
            ManagerView.displayExportError(filename, e.getMessage());
        }
    }
}
