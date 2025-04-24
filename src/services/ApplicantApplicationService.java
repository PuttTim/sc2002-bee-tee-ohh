package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import models.*;
import models.enums.*;

import repositories.*;

/**
 * Service class for handling applicant-related application logic.
 * <p>
 * This class provides methods for managing applications, including retrieving eligible projects,
 * submitting applications, and withdrawing applications.
 * </p>
 */
public class ApplicantApplicationService {

    /**
     * Retrieves a list of projects that the applicant is eligible to apply for.
     * <p>
     * A project is eligible if it is visible and the current time is within the application's open and close dates.
     * </p>
     *
     * @param user the applicant
     * @param allProjects the list of all projects
     * @return a list of eligible projects
     */
    public static List<Project> getEligibleProjects(User user, List<Project> allProjects) {
        LocalDateTime now = LocalDateTime.now();
        return allProjects.stream()
                .filter(Project::isVisible)
                .filter(p -> now.isAfter(p.getApplicationOpenDate()) && now.isBefore(p.getApplicationCloseDate()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all applications submitted by a specific applicant.
     *
     * @param applicant the applicant whose applications are to be retrieved
     * @return a list of applications submitted by the applicant
     */
    public static List<Application> getApplicationsByApplicant(Applicant applicant) {
        return ApplicationRepository.getAll().stream()
                .filter(app -> app.getApplicantNRIC().equals(applicant.getUserNRIC()))
                .collect(Collectors.toList());
    }

    /**
     * Submits an application for a specific project and flat type.
     * <p>
     * This method checks if the applicant already has a pending or successful application before allowing
     * them to submit a new application. If the applicant has an active application, the submission will fail.
     * </p>
     *
     * @param applicant the applicant submitting the application
     * @param project the project the applicant is applying to
     * @param flatType the flat type the applicant is applying for
     * @return true if the application was successfully submitted, false if the applicant already has an active application
     */
    public static boolean submitApplication(Applicant applicant, Project project, FlatType flatType) {
        // Check if applicant already has a pending or approved application
        boolean hasActiveApplication = getApplicationsByApplicant(applicant).stream()
                .anyMatch(app -> app.getApplicationStatus() == ApplicationStatus.PENDING ||
                        app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL);

        if (hasActiveApplication) {
            return false;
        }

        Application application = new Application(applicant.getUserNRIC(), project.getProjectID(), flatType);

        ApplicationRepository.add(application);
        ApplicationRepository.saveAll();
        return true;
    }

    /**
     * Withdraws an application submitted by the applicant.
     * <p>
     * The method allows an applicant to withdraw their own application, but only if the application is still pending.
     * If the application is already successful or rejected, it cannot be withdrawn.
     * </p>
     *
     * @param applicant the applicant who wishes to withdraw their application
     * @param applicationId the ID of the application to withdraw
     * @throws IllegalArgumentException if the application cannot be found
     * @throws IllegalStateException if the application is not pending or is owned by another applicant
     */
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
