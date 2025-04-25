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

public class ManagerService {
    public static List<Map<String, String>> generateApplicantReport(Project project, Map<String, String> filters) {
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

    public static void exportReportToCsv(List<Map<String, String>> reportData, String filename) {
        ICsvConfig config = new ICsvConfig() {
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
