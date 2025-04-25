package services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import interfaces.IApplicantApplicationService;
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
public class ApplicantApplicationService implements IApplicantApplicationService {
    private static ApplicantApplicationService instance;
    private final ProjectService projectService;

    private ApplicantApplicationService() {
        this.projectService = ProjectService.getInstance();
    }

    public static ApplicantApplicationService getInstance() {
        if (instance == null) {
            instance = new ApplicantApplicationService();
        }
        return instance;
    }

    @Override
    public List<Project> getEligibleProjects(User user) {
        return getEligibleProjects(user, projectService.getVisibleProjects());
    }

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
    @Override
    public List<Project> getEligibleProjects(User user, List<Project> allProjects) {
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

    private boolean hasExistingApplication(User user, Project project) {
        return ApplicationRepository.getAll().stream()
                .filter(app -> app.getApplicantNRIC().equals(user.getUserNRIC()))
                .filter(app -> app.getProjectId().equals(project.getProjectID()))
                .anyMatch(app -> app.getApplicationStatus() != ApplicationStatus.UNSUCCESSFUL && 
                               app.getApplicationStatus() != ApplicationStatus.WITHDRAWN);
    }

    /**
     * Retrieves all applications submitted by a specific applicant.
     *
     * @param applicant the applicant whose applications are to be retrieved
     * @return a list of applications submitted by the applicant
     */
    @Override
    public List<Application> getApplicationsByApplicant(Applicant applicant) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }

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
    @Override
    public boolean submitApplication(Applicant applicant, Project project, FlatType flatType) {
        validateSubmissionParameters(applicant, project, flatType);
        
        boolean hasActiveApplication = hasActiveApplication(applicant);
        if (hasActiveApplication) {
            return false;
        }

        if (project.getAvailableUnits(flatType) <= 0) {
            throw new IllegalStateException("No available units left for this flat type");
        }

        if (applicant.getMaritalStatus() != MaritalStatus.MARRIED && flatType != FlatType.TWO_ROOM) {
            throw new IllegalArgumentException("Singles/Divorced can only apply for two room flats.");
        }

        Application application = new Application(applicant.getUserNRIC(), project.getProjectID(), flatType);
        ApplicationRepository.add(application);
        ApplicationRepository.saveAll();
        
        applicant.addAppliedProject(project.getProjectID());
        ApplicantRepository.update(applicant);
        
        return true;
    }

    private void validateSubmissionParameters(Applicant applicant, Project project, FlatType flatType) {
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

    private boolean hasActiveApplication(Applicant applicant) {
        return getApplicationsByApplicant(applicant).stream()
                .anyMatch(app -> app.getApplicationStatus() == ApplicationStatus.PENDING || 
                               app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL ||
                               app.getApplicationStatus() == ApplicationStatus.BOOKED);
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
    @Override
    public void withdrawApplication(Applicant applicant, String applicationId) {
        Application application = validateWithdrawalRequest(applicant, applicationId);
        
        application.requestWithdrawal();
        ApplicationRepository.saveAll();
    }

    private Application validateWithdrawalRequest(Applicant applicant, String applicationId) {
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
