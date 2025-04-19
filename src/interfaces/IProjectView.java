package interfaces;

import models.Project;

import java.util.List;

/**
 * Interface for views for displaying project information.
 */
public interface IProjectView {

    /**
     * Displays a list of projects.
     *
     * @param projects the list of projects to be displayed.
     */
    public void showProjectList(List<Project> projects);

    /**
     * Displays a message when no projects are available.
     */
    public void displayEmptyMessage();

    /**
     * Displays details about a project.
     *
     * @param project the project whose details are to be displayed.
     */
    public void displayProjectDetails(Project project);
}
