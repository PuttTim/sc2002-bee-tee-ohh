package views;

/**
 * A view class that handles displaying success or failure messages
 * related to project management actions for the manager.
 */
public class ManagerEditProjectView {

    /**
     * Displays a success message when a project is created successfully.
     */
    public void ProjectCreationSuccess() {
        CommonView.displaySuccess("Project created successfully.");
    }

    /**
     * Displays an error message when project creation fails.
     */
    public void ProjectCreationFailure() {
        CommonView.displayError("Project creation failed.");
    }

    /**
     * Displays a success message when a project is edited successfully.
     */
    public void ProjectEditSuccess() {
        CommonView.displaySuccess("Project edited successfully.");
    }

    /**
     * Displays an error message when project editing fails.
     */
    public void ProjectEditFailure() {
        CommonView.displayError("Project edit failed.");
    }

    /**
     * Displays a success message when a project is deleted successfully.
     */
    public void ProjectDeletionSuccess() {
        CommonView.displaySuccess("Project deleted successfully.");
    }

    /**
     * Displays an error message when project deletion fails.
     */
    public void ProjectDeletionFailure() {
        CommonView.displayError("Project deletion failed.");
    }
}
