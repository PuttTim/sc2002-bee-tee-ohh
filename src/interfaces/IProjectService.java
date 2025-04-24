package interfaces;

import java.util.List;

import models.Officer;
import models.Project;

/**
 * <p>Interface for managing project-related operations, such as checking officer slots and retrieving handled projects.</p>
 * <ul>
 * <li>Checks if a project has officer slots available.</li>
 * <li>Gets the list of projects handled by a specific officer.</li>
 * </ul>
 */
public interface IProjectService {

    /**
     * Checks if a project has available officer slots.
     *
     * @param project The project to check.
     * @return true if the project has officer slots, false otherwise.
     */
    public boolean hasOfficerSlots(Project project);

    /**
     * Gets a list of projects handled by the officer.
     *
     * @param officer The officer whose handled projects are to be retrieved.
     * @return A list of projects handled by the officer.
     */
    public List<Project> getHandledProjects(Officer officer);
}
