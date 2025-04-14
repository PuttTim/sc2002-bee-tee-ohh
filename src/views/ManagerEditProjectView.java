package views;

public class ManagerEditProjectView {
    public void ProjectCreationSuccess() {
        CommonView.displaySuccess("Project created successfully.");
    }

    public void ProjectCreationFailure() {
        CommonView.displayError("Project creation failed.");
    }

    public void ProjectEditSuccess() {
        CommonView.displaySuccess("Project edited successfully.");
    }

    public void ProjectEditFailure() {
        CommonView.displayError("Project edit failed.");
    }

    public void ProjectDeletionSuccess() {
        CommonView.displaySuccess("Project deleted successfully.");
    }

    public void ProjectDeletionFailure() {
        CommonView.displayError("Project deletion failed.");
    }
}
