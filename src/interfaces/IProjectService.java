package interfaces;

import models.Officer;
import models.Project;

import java.util.List;

/**
 * Interface for services to manage projects and officer assignments.
 */
public interface IProjectService {

    /**
     * Checks if a project has available officer slots.
     *
     * @param project the project to be checked.
     * @return <code>true</code> if the project has officer slots available, <code>false</code> if not.
     */
    public boolean hasOfficerSlots(Project project);

    /**
     * Retrieves the projects handled by a specific officer.
     *
     * @param officer the officer whose projects are to be retrieved.
     * @return a list of projects handled by the officer.
     */
    public List<Project> getHandledProjects(Officer officer);

}
