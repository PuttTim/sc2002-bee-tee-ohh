package interfaces;

import java.time.LocalDateTime;
import java.util.List;

import models.Filter;
import models.Manager;
import models.Officer;
import models.Project;

/**
 * Interface for managing project-related operations.
 */
public interface IProjectService {
    /**
     * Returns a list of projects that match the given filters.
     *
     * @param filters List of filters to apply.
     * @return Filtered list of projects.
     */
    List<Project> getProjects(List<Filter> filters);

    /**
     * Returns all projects in the system.
     *
     * @return List of all projects.
     */
    List<Project> getAllProjects();

    /**
     * Returns all projects that are currently marked as visible.
     *
     * @return List of visible projects.
     */
    List<Project> getVisibleProjects();

    /**
     * Retrieves a project by its name.
     *
     * @param projectName Name of the project.
     * @return The project with the given name, or null if not found.
     */
    Project getProjectByName(String projectName);

    /**
     * Retrieves the project assigned to a given officer.
     *
     * @param officer The officer whose project is to be retrieved.
     * @return The project assigned to the officer, or null if none.
     */
    Project getProjectByOfficer(Officer officer);

    /**
     * Retrieves all projects created by a specific manager.
     *
     * @param manager The manager whose projects are to be retrieved.
     * @return List of projects managed by the manager.
     */
    List<Project> getProjectsByManager(Manager manager);

    /**
     * Creates a new project with the specified details.
     *
     * @param managerNRIC   NRIC of the manager creating the project.
     * @param projectName   Name of the project.
     * @param location      Project location.
     * @param startDate     Application start date.
     * @param endDate       Application end date.
     * @param officerSlots  Number of officer slots for the project.
     * @param visible       Whether the project is visible to applicants.
     */
    void createProject(String managerNRIC, String projectName, String location, LocalDateTime startDate, LocalDateTime endDate, int officerSlots, boolean visible);

    /**
     * Updates the core project details such as location and application period.
     *
     * @param project    Project to update.
     * @param location   New location.
     * @param startDate  New application start date.
     * @param endDate    New application end date.
     */
    void updateProject(Project project, String location, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Persists all editable fields of a given project.
     *
     * @param project The project with updated details.
     */
    void updateProjectDetails(Project project);

    /**
     * Permanently deletes the project with the given name.
     *
     * @param projectName Name of the project to delete.
     */
    void deleteProject(String projectName);

    /**
     * Changes the visibility status of a project.
     *
     * @param projectName Name of the project.
     * @param visible     New visibility status.
     */
    void toggleProjectVisibility(String projectName, boolean visible);

    /**
     * Retrieves all projects assigned to a specific officer by NRIC.
     *
     * @param officerNRIC NRIC of the officer.
     * @return List of officer's assigned projects.
     */
    List<Project> getAllOfficersProjects(String officerNRIC);

    // Method from original IProjectService - keep or remove based on usage?
    // boolean hasOfficerSlots(Project project); 
    // List<Project> getHandledProjects(Officer officer);
}
