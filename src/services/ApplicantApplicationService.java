package services;

import models.*;
import models.enums.*;
import repositories.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicantApplicationService {
    public static List<Project> getEligibleProjects(Applicant applicant, List<Project> allProjects) {
        LocalDateTime now = LocalDateTime.now();
        return allProjects.stream()
                .filter(Project::isVisible)
                .filter(p -> now.isAfter(p.getApplicationOpenDate()) && now.isBefore(p.getApplicationCloseDate()))
                .collect(Collectors.toList());
    }

    public static List<Application> getApplicationsByApplicant(Applicant applicant) {
        return ApplicationRepository.getAll().stream()
                .filter(app -> app.getApplicantNRIC().equals(applicant.getUserNRIC()))
                .collect(Collectors.toList());
    }

    public static void submitApplication(Applicant applicant, Project project, FlatType flatType) {
        // Check if applicant already has a pending or approved application
        boolean hasActiveApplication = getApplicationsByApplicant(applicant).stream()
                .anyMatch(app -> app.getApplicationStatus() == ApplicationStatus.PENDING || 
                               app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL);
        
        if (hasActiveApplication) {
            throw new IllegalStateException("You already have an active application");
        }

        Application application = new Application(applicant.getUserNRIC(), project.getProjectID(), flatType);

        ApplicationRepository.add(application);
        ApplicationRepository.saveAll();
    }

    public static void withdrawApplication(Applicant applicant, String applicationId) {
        Application application = ApplicationRepository.getById(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Application not found");
        }

        if (!application.getApplicantNRIC().equals(applicant.getUserNRIC())) {
            throw new IllegalStateException("You can only withdraw your own applications");
        }

        if (application.getApplicationStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Can only withdraw pending applications");
        }

        application.requestWithdrawal();
        ApplicationRepository.saveAll();
    }
}
