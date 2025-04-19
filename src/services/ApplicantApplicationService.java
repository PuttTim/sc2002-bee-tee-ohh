package services;

import models.*;
import models.enums.*;
import repositories.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handles application-related actions for applicants.
 * <p>Such actions include:</p>
 * <ul>
 *     <li>Viewing eligible projects</li>
 *     <li>Submitting applications</li>
 *     <li>Withdrawing applications</li>
 * </ul>
 */
public class ApplicantApplicationService {

    /**
     * Gets a list of projects that the user is eligible to apply for.
     *
     * A project is eligible if:
     * - it is marked as visible
     * - the current time is between the project's application open and close dates
     *
     * @param user the user checking for eligible projects
     * @param allProjects the list of all available projects
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
     * Returns all applications submitted by a specific applicant.
     *
     * @param applicant the applicant to search for
     * @return list of the applicant's applications
     */
    public static List<Application> getApplicationsByApplicant(Applicant applicant) {
        return ApplicationRepository.getAll().stream()
                .filter(app -> app.getApplicantNRIC().equals(applicant.getUserNRIC()))
                .collect(Collectors.toList());
    }

    /**
     * Submits a new application for an applicant,
     * if they do not already have a pending or successful application.
     *
     * @param applicant the applicant submitting the application
     * @param project the project the applicant is applying for
     * @param flatType the flat type the applicant is applying for
     * @return <code>true</code> if the application was submitted successfully,
     * <code>false</code> if application submission was unsuccessful
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
     * Only owner of the application can withdraw it, and only if the application is still pending.
     *
     * @param applicant the applicant who wants to withdraw the application
     * @param applicationId the ID of the application to withdraw
     * @throws IllegalArgumentException if the application does not exist
     * @throws IllegalStateException if the application does not belong to the applicant or is not pending
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
