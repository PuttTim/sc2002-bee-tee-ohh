package controllers;

import models.*;
import models.enums.MaritalStatus;
import repositories.OfficerRepository;
import views.CommonView;
import java.util.Scanner;

public class ManagerController {
    private static final Scanner scanner = new Scanner(System.in);

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
