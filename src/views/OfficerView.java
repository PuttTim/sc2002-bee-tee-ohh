package views;

import models.*;
import models.enums.ApplicationStatus;
import models.enums.FlatType;
import repositories.ProjectRepository;
import repositories.UserRepository;
import services.ApplicantApplicationService;
import utils.DateTimeUtils;
import java.util.List;

public class OfficerView {
    public static int showSelectHandledProjectMenu(Project projects) {
        List<String> options = List.of(
                "View Applications",
                "View Enquiries"
                );
        
        int choice = CommonView.displayMenuWithBacking("Select Officer Operation", options);

        return choice;
    }

    public static void displayOfficerHandledProjects(List<Project> projects) {
        CommonView.displayHeader("Projects Handled by You");

        if (projects.isEmpty()) {
            System.out.println("No projects handled by you.");
            return;
        }

        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            CommonView.displayMessage((i + 1) + ". " + project.getProjectName());
            CommonView.displayMessage("   " + "Project ID: " + project.getProjectID());
            CommonView.displayMessage("   " + "Location: " + project.getLocation());
            CommonView.displayMessage("   " + "Application Open Date: " + DateTimeUtils.formatDateTime(project.getApplicationOpenDate()));
            CommonView.displayMessage("   " + "Application Close Date: " + DateTimeUtils.formatDateTime(project.getApplicationCloseDate()));
            CommonView.displayMessage("   " + "Officer Slots: " + project.getOfficerSlots());
            CommonView.displayMessage("   " + "Visible to Applicants: " + (project.isVisible() ? "Yes" : "No"));
            CommonView.displayShortSeparator();
        }
    }
}
