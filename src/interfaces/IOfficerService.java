package interfaces;

import java.util.List;
import models.Officer;
import models.Project;

/**
 * Interface for managing officer-related operations.
 */
public interface IOfficerService {

    /**
     * Checks if the officer has an existing project.
     *
     * @param officer The officer to check.
     * @return true if the officer has an existing project, false otherwise.
     */
    boolean hasExistingProject(Officer officer);

    /**
     * Checks if the officer has an existing registration.
     *
     * @param officer The officer to check.
     * @return true if the officer has an existing registration, false otherwise.
     */
    boolean hasExistingRegistration(Officer officer);

    /**
     * Sets the officer's registration.
     *
     * @param officer The officer whose registration is to be set.
     */
    void setOfficerRegistration(Officer officer);

    /**
     * Retrieves a list of available projects.
     *
     * @return A list of available projects.
     */
    List<Project> getAvailableProjects();

    /**
     * Checks if a project is eligible for registration for a given officer.
     *
     * @param project The project to check.
     * @param officer The officer to check.
     * @return true if the project is eligible for registration, false otherwise.
     */
    boolean isProjectEligibleForRegistration(Project project, Officer officer);

    /**
     * Registers an officer for a project.
     *
     * @param officer The officer to register.
     * @param project The project for registration.
     */
    void registerOfficerForProject(Officer officer, Project project);

    /**
     * Retrieves the project associated with a given officer.
     *
     * @param officer The officer whose project is to be retrieved.
     * @return The project associated with the officer.
     */
    Project getProjectByOfficer(Officer officer);

    /**
     * Retrieves a list of projects handled by a given officer.
     *
     * @param officer The officer whose handled projects are to be retrieved.
     * @return A list of projects handled by the officer.
     */
    List<Project> getHandledProjects(Officer officer);
}
