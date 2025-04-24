package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import models.*;
import models.enums.*;
import repositories.*;

public class ApplicantApplicationService {
    public static List<Project> getEligibleProjects(User user) {
        return getEligibleProjects(user, ProjectService.getVisibleProjects());
    }

    public static List<Project> getEligibleProjects(User user, List<Project> allProjects) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        LocalDateTime now = LocalDateTime.now();
        return allProjects.stream()
                .filter(Project::isVisible)
                .filter(p -> now.isAfter(p.getApplicationOpenDate()) && now.isBefore(p.getApplicationCloseDate()))
                .filter(p -> !hasExistingApplication(user, p))
                .collect(Collectors.toList());
    }

    private static boolean hasExistingApplication(User user, Project project) {
        return ApplicationRepository.getAll().stream()
                .filter(app -> app.getApplicantNRIC().equals(user.getUserNRIC()))
                .filter(app -> app.getProjectId().equals(project.getProjectID()))
                .anyMatch(app -> app.getApplicationStatus() != ApplicationStatus.UNSUCCESSFUL && 
                               app.getApplicationStatus() != ApplicationStatus.WITHDRAWN);
    }

    public static List<Application> getApplicationsByApplicant(Applicant applicant) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }

        return ApplicationRepository.getAll().stream()
                .filter(app -> app.getApplicantNRIC().equals(applicant.getUserNRIC()))
                .collect(Collectors.toList());
    }

    public static boolean submitApplication(Applicant applicant, Project project, FlatType flatType) {
        validateSubmissionParameters(applicant, project, flatType);
        
        boolean hasActiveApplication = hasActiveApplication(applicant);
        if (hasActiveApplication) {
            return false;
        }

        if (project.getAvailableUnits(flatType) <= 0) {
            throw new IllegalStateException("No units available for selected flat type");
        }

        Application application = new Application(applicant.getUserNRIC(), project.getProjectID(), flatType);
        ApplicationRepository.add(application);
        ApplicationRepository.saveAll();
        
        applicant.addAppliedProject(project.getProjectID());
        ApplicantRepository.saveAll();
        
        return true;
    }

    private static void validateSubmissionParameters(Applicant applicant, Project project, FlatType flatType) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        if (flatType == null) {
            throw new IllegalArgumentException("Flat type cannot be null");
        }
        if (!project.isVisible()) {
            throw new IllegalStateException("Project is not available for applications");
        }
        if (!project.getFlatTypes().contains(flatType)) {
            throw new IllegalArgumentException("Selected flat type is not available in this project");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(project.getApplicationOpenDate()) || now.isAfter(project.getApplicationCloseDate())) {
            throw new IllegalStateException("Project is not open for applications at this time");
        }
    }

    private static boolean hasActiveApplication(Applicant applicant) {
        return getApplicationsByApplicant(applicant).stream()
                .anyMatch(app -> app.getApplicationStatus() == ApplicationStatus.PENDING || 
                               app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL ||
                               app.getApplicationStatus() == ApplicationStatus.BOOKED);
    }

    public static void withdrawApplication(Applicant applicant, String applicationId) {
        Application application = validateWithdrawalRequest(applicant, applicationId);
        
        application.requestWithdrawal();
        ApplicationRepository.saveAll();
    }

    private static Application validateWithdrawalRequest(Applicant applicant, String applicationId) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }
        if (applicationId == null || applicationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Application ID cannot be null or empty");
        }

        Application application = ApplicationRepository.getById(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Application not found");
        }

        if (!application.getApplicantNRIC().equals(applicant.getUserNRIC())) {
            throw new IllegalStateException("You can only withdraw your own applications");
        }

        if (!application.canWithdraw()) {
            throw new IllegalStateException("This application cannot be withdrawn in its current state");
        }

        return application;
    }
}
