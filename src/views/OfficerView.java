package views;

import models.*;
import models.enums.ApplicationStatus;
import models.enums.FlatType;
import repositories.ProjectRepository;
import repositories.UserRepository;
import services.ApplicantApplicationService;
import utils.DateTimeUtils;
import java.util.List;

/**
 * A view class that handles displaying information related to officer operations, such as:
 * <ul>
 *     <li>Managing projects</li>
 *     <li>Managing applications</li>
 *     <li>Managing enquiries</li>
 * </ul>
 */
public class OfficerView {

    /**
     * Displays a menu for selecting operations on a project handled by an officer.
     *
     * @param projects the project that the officer is handling.
     * @return the choice made by the officer, to view applications or enquiries
     */
    public static int showSelectHandledProjectMenu(Project projects) {
        List<String> options = List.of(
                "View Applications",
                "View Enquiries"
        );

        // Displays the menu and returns the user's choice
        int choice = CommonView.displayMenuWithBacking("Select Officer Operation", options);

        return choice;
    }

    /**
     * Displays a list of projects that are handled by the officer.
     *
     * @param projects the list of projects handled by the officer.
     */
    public static void displayOfficerHandledProjects(List<Project> projects) {
        CommonView.displayHeader("Projects Handled by You");

        if (projects.isEmpty()) {
            System.out.println("No projects handled by you.");
            return;
        }

        // Iterates through each project and displays relevant information
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
