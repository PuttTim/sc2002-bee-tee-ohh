package views;

import java.util.List;

import models.*;
import models.enums.ApplicationStatus;
import models.enums.FlatType;

import repositories.ProjectRepository;
import repositories.UserRepository;
import services.ApplicantApplicationService;
import utils.DateTimeUtils;

public class ApplicantApplicationView {
    private static final ApplicantApplicationService applicantApplicationService = ApplicantApplicationService.getInstance();

    public static int showMainMenu() {
        List<String> options = List.of(
            "View My Applications",
            "View Available Projects",
            "Submit New Application",
            "Withdraw Application"
        );
        return CommonView.displayMenuWithBacking("Application Management", options);
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
            CommonView.displayMessage(String.format("%d. Project: %s | Flat Type: %s | Status: %s | Applied: %s", 
                i + 1,
                project != null ? project.getProjectName() : "N/A",
                app.getSelectedFlatType().getDescription(),
                app.getApplicationStatus().getDescription(),
                DateTimeUtils.formatDateTime(app.getApplicationDate())
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
        CommonView.displayHeader("Application Details");
        CommonView.displayMessage("Project: " + (project != null ? project.getProjectName() : "N/A"));
        CommonView.displayMessage("Flat Type: " + application.getSelectedFlatType().getDescription());
        CommonView.displayMessage("Status: " + application.getApplicationStatus().getDescription());
        CommonView.displayMessage("Application Date: " + DateTimeUtils.formatDateTime(application.getApplicationDate()));
        
        if (application.getApprovedBy() != null) {
            String action = application.getApplicationStatus() == ApplicationStatus.SUCCESSFUL ? "Approved" : "Processed";
            User processor = UserRepository.getByNRIC(application.getApprovedBy());
            CommonView.displayMessage(action + " By: " + (processor != null ? processor.getName() : application.getApprovedBy()));
        }

        if (project != null) {
            CommonView.displayMessage("\nProject Details:");
            CommonView.displayMessage("Location: " + project.getLocation());
            CommonView.displayMessage("Price: $" + project.getFlatPrice(application.getSelectedFlatType()));
            CommonView.displayMessage("Available Units: " + project.getAvailableUnits(application.getSelectedFlatType()));
        }

        CommonView.displaySeparator();
    }

    public static void displayEligibleProjects(List<Project> eligibleProjects) {
        if (eligibleProjects.isEmpty()) {
            CommonView.displayMessage("No eligible projects found.");
            return;
        }

        CommonView.displayHeader("Eligible Projects");
        ProjectView.displayAvailableProjects(eligibleProjects);
    }

    public static int promptProjectSelection(List<Project> eligibleProjects) {
        displayEligibleProjects(eligibleProjects);
        return CommonView.promptInt("\nSelect a project number (or 0 to cancel): ", 0, eligibleProjects.size());
    }

    public static int promptFlatTypeSelection(Project project) {
        CommonView.displayHeader("Available Flat Types");
        List<FlatType> flatTypes = project.getFlatTypes();
        for (int i = 0; i < flatTypes.size(); i++) {
            CommonView.displayMessage(String.format("%d. %s - $%d", 
                i + 1, 
                flatTypes.get(i).getDescription(),
                project.getFlatPrice(flatTypes.get(i))
            ));
        }
        return CommonView.promptInt("Select a flat type number: ", 1, flatTypes.size());
    }

    public static void displaySubmissionSuccess() {
        CommonView.displaySuccess("Application submitted successfully.");
    }

    public static void displaySubmissionError(String error) {
        CommonView.displayError(error);
    }

    public static void displayWithdrawalSuccess() {
        CommonView.displaySuccess("Application withdrawal requested successfully.");
    }

    public static void displayWithdrawalError(String error) {
        CommonView.displayError(error);
    }

    public static void handleWithdraw(Applicant applicant) {
        List<Application> applications = applicantApplicationService.getApplicationsByApplicant(applicant);
        if (applications.isEmpty()) {
            CommonView.displayMessage("No applications available to withdraw.");
            return;
        }

        List<Application> withdrawableApplications = applications.stream()
            .filter(Application::canWithdraw)
            .toList();

        CommonView.displayHeader("Applications Available for Withdrawal");
        if (withdrawableApplications.isEmpty()) {
            CommonView.displayMessage("No applications available for withdrawal.");
            CommonView.prompt("Press Enter to continue...");
            return;
        }

        displayApplicationList(withdrawableApplications, "Your Applications");

        int choice = CommonView.promptInt("Select an application to withdraw (or 0 to go back): ", 0, withdrawableApplications.size());
        if (choice > 0) {
            Application selectedApp = withdrawableApplications.get(choice - 1);
            if (CommonView.promptWordConfirmation(
                    "Are you sure you want to request withdrawal for application ID " + selectedApp.getApplicationID() + "?", "WITHDRAW")) {
                try {
                    applicantApplicationService.withdrawApplication(applicant, selectedApp.getApplicationID());
                    displayWithdrawalSuccess();
                } catch (Exception e) {
                    displayWithdrawalError(e.getMessage());
                }
            }
        }
        CommonView.prompt("Press Enter to continue...");
    }
}