package controllers;

import models.Project;
import repositories.OfficerRepository;
import views.CommonView;

public class ManagerController {

    public static void viewOfficerRegistrations() {
        ProjectController.viewOfficerRegistrations();
    }

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
