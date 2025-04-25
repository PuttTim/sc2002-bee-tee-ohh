package views;

import models.*;
import models.enums.ApplicationStatus;
import repositories.ProjectRepository;
import repositories.UserRepository;
import utils.DateTimeUtils;
import java.util.List;

/**
 * View class that handles the display of officer-related functionalities.
 * It provides methods for displaying projects handled by the officer, managing applications,
 * and viewing application details and status histories.
 */
public class OfficerView {
    /**
     * Displays a menu for selecting officer operations for a given project.
     *
     * @param projects The project the officer is handling
     * @return The choice selected by the officer
     */
    public static int showSelectHandledProjectMenu(Project projects) {
        List<String> options = List.of(
                "View All Applications",
                "Manage Successful Applications",
                "View Enquiries"
        );

        int choice = CommonView.displayMenuWithBacking("Select Officer Operation", options);
        return choice;
    }

    /**
     * Displays a list of projects handled by the officer.
     *
     * @param projects The list of projects handled by the officer
     */
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

    /**
     * Displays a list of applications with relevant information and their status.
     *
     * @param applications The list of applications to be displayed
     * @param header The header for the application list
     */
    public static void displayApplicationList(List<Application> applications, String header) {
        if (applications.isEmpty()) {
            CommonView.displayMessage("No applications found.");
            return;
        }

        CommonView.displayHeader(header);
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            Project project = ProjectRepository.getById(app.getProjectId());
            User applicant = UserRepository.getByNRIC(app.getApplicantNRIC());
            CommonView.displayMessage(String.format("%d. ID: %s | Applicant: %s (%s) | Project: %s | Flat: %s | Status: %s", 
                i + 1, 
                app.getApplicationID(),
                applicant != null ? applicant.getName() : "N/A",
                app.getApplicantNRIC(),
                project != null ? project.getProjectName() : "N/A",
                app.getSelectedFlatType().getDescription(), 
                app.getApplicationStatus().getDescription()
            ));
        }
        CommonView.displaySeparator();
    }

    /**
     * Displays detailed information about a specific application.
     *
     * @param application The application whose details are to be displayed
     */
    public static void displayApplicationDetails(Application application) {
        if (application == null) {
            CommonView.displayMessage("Application details not available.");
            return;
        }
        Project project = ProjectRepository.getById(application.getProjectId());
        User applicant = UserRepository.getByNRIC(application.getApplicantNRIC());

        CommonView.displayHeader("Application Details (ID: " + application.getApplicationID() + ")");
        CommonView.displayMessage("Applicant NRIC: " + application.getApplicantNRIC());
        CommonView.displayMessage("Applicant Name: " + (applicant != null ? applicant.getName() : "N/A"));
        CommonView.displayMessage("Project Name: " + (project != null ? project.getProjectName() : "N/A"));
        CommonView.displayMessage("Selected Flat Type: " + application.getSelectedFlatType().getDescription());
        CommonView.displayMessage("Application Date: " + DateTimeUtils.formatDateTime(application.getApplicationDate()));
        CommonView.displayMessage("Current Status: " + application.getApplicationStatus().getDescription());
        
        if (application.getApprovedBy() != null) {
            User approver = UserRepository.getByNRIC(application.getApprovedBy());
            String action = (application.getApplicationStatus() == ApplicationStatus.SUCCESSFUL || application.getApplicationStatus() == ApplicationStatus.BOOKED) ? "Approved" : "Processed";
            CommonView.displayMessage(action + " By: " + (approver != null ? approver.getName() : application.getApprovedBy()));
        }

        application.getApplicationStatusHistory().forEach((status, timestamp) -> 
            CommonView.displayMessage(String.format("  - %s: %s", status.getDescription(), DateTimeUtils.formatDateTime(timestamp)))
        );
        CommonView.displaySeparator();
    }
}
