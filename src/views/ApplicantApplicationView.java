package views;

import models.*;
import models.enums.ApplicationStatus;
import models.enums.FlatType;
import repositories.ProjectRepository;
import repositories.UserRepository;
import services.ApplicantApplicationService;
import utils.DateTimeUtils;
import java.util.List;

public class ApplicantApplicationView {
    public static void showApplicationMenu(Applicant applicant, List<Project> allProjects) {
        List<String> options = List.of(
            "View Available Projects",
            "Submit New Application",
            "Check Application Status",
            "Withdraw Application",
            "Back to Main Menu"
        );

        while (true) {
            int choice = CommonView.displayMenu("Application Management", options);
            switch (choice) {
                case 1 -> displayEligibleProjects(applicant, allProjects);
                case 2 -> promptApplication(applicant, allProjects);
                case 3 -> displayApplicationStatus(applicant);
                case 4 -> handleWithdraw(applicant);
                case 5 -> {return;}
            }
        }
    }

    public static void displayEligibleProjects(Applicant applicant, List<Project> allProjects) {
        List<Project> eligibleProjects = ApplicantApplicationService.getEligibleProjects(applicant, allProjects);
        if (eligibleProjects.isEmpty()) {
            CommonView.displayMessage("No eligible projects found.");
            return;
        }

        CommonView.displayHeader("Eligible Projects");
        for (int i = 0; i < eligibleProjects.size(); i++) {
            Project project = eligibleProjects.get(i);
            CommonView.displayMessage(String.format("%d. %s (Location: %s)", 
                i + 1, project.getProjectName(), project.getLocation()));
            CommonView.displayMessage("   Application Period: " + 
                DateTimeUtils.formatDateTime(project.getApplicationOpenDate()) +
                " to " + DateTimeUtils.formatDateTime(project.getApplicationCloseDate()));
        }
    }

    public static void promptApplication(Applicant applicant, List<Project> allProjects) {
        List<Project> eligibleProjects = ApplicantApplicationService.getEligibleProjects(applicant, allProjects);
        if (eligibleProjects.isEmpty()) {
            CommonView.displayMessage("No eligible projects available for application.");
            return;
        }

        displayEligibleProjects(applicant, allProjects);
        int choice = CommonView.promptInt("\nSelect a project number (or 0 to cancel): ", 0, eligibleProjects.size());
        if (choice == 0) return;

        Project selectedProject = eligibleProjects.get(choice - 1);
        CommonView.displayHeader("Available Flat Types");
        List<FlatType> flatTypes = selectedProject.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage(String.format("%d. %s", i + 1, flatTypes.get(i)));
        }

        int flatTypeChoice = CommonView.promptInt("Select a flat type number: ", 1, flatTypes.size());
        FlatType selectedFlatType = flatTypes.get(flatTypeChoice - 1);
        if (ApplicantApplicationService.submitApplication(applicant, selectedProject, selectedFlatType)) {
            CommonView.displaySuccess("Application submitted successfully.");
        } else {
            CommonView.displayError("You have an existing BTO application. Please try again later.");
            return;
        }
    }

    public static void displayApplicationStatus(Applicant applicant) {
        List<Application> applications = ApplicantApplicationService.getApplicationsByApplicant(applicant);
        if (applications.isEmpty()) {
            CommonView.displayMessage("No applications found.");
            return;
        }

        CommonView.displayHeader("Your Applications");
        for (Application app : applications) {
            CommonView.displayMessage("Project: " + ProjectRepository.getById(app.getProjectId()).getProjectName());
            CommonView.displayMessage("Flat Type: " + app.getSelectedFlatType());
            CommonView.displayMessage("Status: " + getStatusDisplay(app.getApplicationStatus()));
            CommonView.displayMessage("Application Date: " + DateTimeUtils.formatDateTime(app.getApplicationDate()));
            if (app.getApprovedBy() != null) {
                if (app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL) {
                    CommonView.displayMessage("Approved By: " + UserRepository.getByNRIC(app.getApprovedBy()).getName());
                } else {
                    CommonView.displayMessage("Rejected By: " + UserRepository.getByNRIC(app.getApprovedBy()).getName());
                }
            }
            CommonView.displaySeparator();
        }
    }

    public static void handleWithdraw(Applicant applicant) {
        List<Application> applications = ApplicantApplicationService.getApplicationsByApplicant(applicant);
        if (applications.isEmpty()) {
            CommonView.displayMessage("No applications available to withdraw.");
            return;
        }

        CommonView.displayHeader("Your Active Applications");
        int activeCount = 0;
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            if (app.getApplicationStatus() == ApplicationStatus.PENDING) {
                CommonView.displayMessage(String.format("%d. Project: %s, Flat Type: %s", 
                    i + 1, 
                    ProjectRepository.getById(app.getProjectId()).getProjectName(),
                    app.getSelectedFlatType()));
                activeCount++;
            }
        }

        if (activeCount == 0) {
            CommonView.displayMessage("No active applications available to withdraw.");
            return;
        }

        int choice = CommonView.promptInt("Select application to withdraw (or 0 to cancel): ", 0, applications.size());
        if (choice > 0) {
            Application selectedApp = applications.get(choice - 1);
            ApplicantApplicationService.withdrawApplication(applicant, selectedApp.getApplicationID());
            CommonView.displaySuccess("Application withdrawal requested successfully.");
        }
    }

    private static String getStatusDisplay(ApplicationStatus status) {
        return switch (status) {
            case PENDING -> "Pending";
            case SUCCESSFUL -> "Approved";
            case UNSUCCESSFUL -> "Rejected";
            case WITHDRAWN -> "Withdrawn";
            case BOOKED -> "Booked";
            default -> status.toString();
        };
    }
}
