package interfaces;

import java.util.List;

import models.Project;

/**
 * <p>Interface for displaying project-related information to the user.</p>
 * <ul>
 * <li>Shows a list of available projects.</li>
 * <li>Displays a message when no projects are available.</li>
 * <li>Displays detailed information about a specific project.</li>
 * </ul>
 */
public interface IProjectView {

    /**
     * Displays a list of projects.
     *
     * @param projects The list of projects to display.
     */
    public void showProjectList(List<Project> projects);

    /**
     * Displays a message indicating that no projects are available.
     */
    public void displayEmptyMessage();

    /**
     * Displays detailed information about a specific project.
     *
     * @param project The project to display details of.
     */
    public void displayProjectDetails(Project project);
}
