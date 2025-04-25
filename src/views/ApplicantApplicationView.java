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

    /**
     * Displays the main menu for managing applications, offering options such as:
     * <ul>
     *     <li>Viewing current applications</li>
     *     <li>Viewing available projects</li>
     *     <li>Submitting a new application</li>
     *     <li>Withdrawing an application</li>
     * </ul>
     * The user is prompted to select one of the available options.
     *
     * @return The index of the selected option or a value corresponding
     *         to a back action if the user chooses to go back
     */
    public static int showMainMenu() {
        List<String> options = List.of(
            "View My Applications",
            "View Available Projects",
            "Submit New Application",
            "Withdraw Application"
        );
        return CommonView.displayMenuWithBacking("Application Management", options);
    }

    /**
     * Displays a list of applications with their relevant details such as:
     * <ul>
     *     <li>Project name</li>
     *     <li>Flat type</li>
     *     <li>Application status</li>
     *     <li>Application date</li>
     * </ul>
     * If the list is empty, a message indicating no
     * applications are found is displayed.
     *
     * @param applications A list of {@link Application} objects to display.
     * @param header The header text to display before the list of applications.
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

    /**
     * Displays the detailed information of a given application including:
     * <ul>
     *     <li>Project details</li>
     *     <li>Flat type</li>
     *     <li>Application status</li>
     *     <li>Application date</li>
     * </ul>
     * If the application has been approved
     * or processed, the details of the user
     * who performed the action will also be displayed.
     *
     * If the application is associated with a project,
     * project details will be shown.
     *
     * @param application The {@link Application} object containing the details to be displayed.
     *                   If the application is null, a message indicating that the details are
     *                   unavailable will be shown.
     */

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

    /**
     * Displays the list of eligible projects for the applicant. If no projects are found,
     * a message indicating this will be shown.
     *
     * @param eligibleProjects The list of eligible {@link Project} objects to be displayed.
     */
    public static void displayEligibleProjects(List<Project> eligibleProjects) {
        if (eligibleProjects.isEmpty()) {
            CommonView.displayMessage("No eligible projects found.");
            return;
        }

        CommonView.displayHeader("Eligible Projects");
        ProjectView.displayAvailableProjects(eligibleProjects);
    }

    /**
     * Prompts the user to select a project from a list of eligible projects. Displays the eligible
     * projects and then allows the user to select a project number, or cancel the selection.
     *
     * @param eligibleProjects The list of eligible {@link Project} objects for the user to select from.
     * @return The selected project number, or 0 if the user cancels the selection.
     */
    public static int promptProjectSelection(List<Project> eligibleProjects) {
        displayEligibleProjects(eligibleProjects);
        return CommonView.promptInt("\nSelect a project number (or 0 to cancel): ", 0, eligibleProjects.size());
    }

    /**
     * Prompts the user to select a flat type from the available options for a given project.
     * Displays available flat types along with prices, then allows the user to
     * select a flat type number.
     *
     * @param project The {@link Project} for which the available flat types are to be displayed.
     * @return The selected flat type number.
     */
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

    /**
     * Displays a success message when an application has been successfully submitted.
     */
    public static void displaySubmissionSuccess() {
        CommonView.displaySuccess("Application submitted successfully.");
    }

    /**
     * Displays an error message when there is an issue with submitting an application.
     *
     * @param error The error message to be displayed.
     */
    public static void displaySubmissionError(String error) {
        CommonView.displayError(error);
    }

    /**
     * Displays a success message when an application withdrawal has been successfully requested.
     */
    public static void displayWithdrawalSuccess() {
        CommonView.displaySuccess("Application withdrawal requested successfully.");
    }

    /**
     * Displays an error message when there is an issue with requesting an application withdrawal.
     *
     * @param error The error message to be displayed.
     */
    public static void displayWithdrawalError(String error) {
        CommonView.displayError(error);
    }

    /**
     * Allows the applicant to withdraw an application. Displays a list of applications that can be withdrawn,
     * and prompts the applicant to select one for withdrawal. A confirmation is required.
     * If the withdrawal is successful, a success message is displayed; otherwise, an error message is shown.
     *
     * @param applicant The {@link Applicant} whose applications are being managed.
     */
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