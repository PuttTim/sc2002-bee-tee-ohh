package views;

import models.*;
import models.enums.ApplicationStatus;
import repositories.ProjectRepository;
import repositories.UserRepository;
import utils.DateTimeUtils;
import java.util.List;

public class OfficerView {
    public static int showSelectHandledProjectMenu(Project projects) { 
        List<String> options = List.of(
                "View All Applications",
                "Manage Successful Applications",
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
