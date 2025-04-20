package controllers;

import models.Project;
import repositories.OfficerRepository;
import views.CommonView;

/**
 * Controller class that is responsible for handling manager-related actions.
 * <p>Manager-related actions include:</p>
 * <ul>
 *     <li>Handling viewing and managing officer registrations for projects</li>
 * </ul>
 */
public class ManagerController {

    /**
     * Displays a list of officers that have registered for projects.
     * Calls the ProjectController class to show officer registrations.
     */
    public static void viewOfficerRegistrations() {
        ProjectController.viewOfficerRegistrations();
    }

    /**
     * Allows the manager to approve or reject an officer's registration for a specific project.
     *
     * @param project the project for which the officer registration is being managed.
     * @param approve <code>True</code> to approve the registration.
     *                <code>False</code> to reject and remove the officer from the project.
     */
    public static void approveOfficerRegistration(Project project, boolean approve) {
        if (project == null) {
            CommonView.displayError("Project not found.");
            return;
        }

        String officerNRIC = CommonView.prompt("Enter officer NRIC: ").trim();
        
        if (approve) {
            ProjectController.handleOfficerRegistration(OfficerRepository.getByNRIC(officerNRIC), project.getProjectName());
        } else {
            ProjectController.removeOfficerFromProject(project.getProjectName(), officerNRIC);
        }
    }
}
