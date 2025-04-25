package interfaces;

import java.util.List;

import models.Applicant;
import models.Application;
import models.Project;
import models.User;
import models.enums.FlatType;

/**
 * Interface for applicant-related application logic.
 */
public interface IApplicantApplicationService {
    /**
     * Retrieves projects that the user is eligible to apply for.
     *
     * @param user The user to check eligibility for.
     * @return A list of eligible projects.
     */
    List<Project> getEligibleProjects(User user);

    /**
     * Filters a list of projects to those the user is eligible to apply for.
     *
     * @param user        The user to check eligibility for.
     * @param allProjects The list of all projects to filter.
     * @return A list of eligible projects.
     */
    List<Project> getEligibleProjects(User user, List<Project> allProjects);

    /**
     * Retrieves all applications submitted by a specific applicant.
     *
     * @param applicant The applicant whose applications to retrieve.
     * @return A list of the applicant’s applications.
     */
    List<Application> getApplicationsByApplicant(Applicant applicant);

    /**
     * Submits a new application for a project and flat type.
     *
     * @param applicant The applicant submitting the application.
     * @param project   The project to apply to.
     * @param flatType  The type of flat being applied for.
     * @return True if the application was submitted successfully, false otherwise.
     */
    boolean submitApplication(Applicant applicant, Project project, FlatType flatType);

    /**
     * Withdraws an existing application.
     *
     * @param applicant     The applicant withdrawing the application.
     * @param applicationId The ID of the application to withdraw.
     */
    void withdrawApplication(Applicant applicant, String applicationId);

}
