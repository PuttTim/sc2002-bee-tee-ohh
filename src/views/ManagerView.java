package views;

import java.util.List;
import java.util.stream.Collectors;

import models.Application;
import models.Project;
import models.Registration;
import models.enums.RegistrationStatus;
import utils.DateTimeUtils;

/**
 * View class that handles the display of manager-related functionalities.
 * It provides methods for displaying project operations, officer registrations,
 * applicant applications, and project details.
 */
public class ManagerView {

    /**
     * Displays a menu for selecting manager operations for a specific project.
     *
     * @param project The project for which manager operations are being selected
     * @return The choice selected by the manager
     */
    public static int showSelectHandledProjectMenu(Project project) {
        List<String> options = List.of(
                "Manage Officer Registrations",
                "Manage Applicant Applications",
                "Manage Project Details",
                "View Enquiries"
        );

        int choice = CommonView.displayMenuWithBacking("Select Manager Operation for " + project.getProjectName(), options);
        return choice;
    }

    /**
     * Displays officer registrations pending approval or rejection for a given project.
     *
     * @param registrations The list of registrations for the project
     * @param project The project associated with the registrations
     * @return The selected registration number to approve/reject, or 0 to go back
     */
    public static int displayOfficerRegistrationsForApproval(List<Registration> registrations, Project project) {
        CommonView.displayHeader("Officer Registrations for Project: " + project.getProjectName());

        List<Registration> pendingRegistrations = registrations.stream()
                .filter(r -> r.getRegistrationStatus() == RegistrationStatus.PENDING)
                .collect(Collectors.toList());

        List<Registration> otherRegistrations = registrations.stream()
                .filter(r -> r.getRegistrationStatus() != RegistrationStatus.PENDING)
                .collect(Collectors.toList());

        if (registrations.isEmpty()) {
            CommonView.displayMessage("No officer registrations found for this project.");
            return 0;
        }

        int displayIndex = 1;
        CommonView.displayMessage("--- Pending Registrations ---");
        if (pendingRegistrations.isEmpty()) {
            CommonView.displayMessage("No pending registrations.");
        } else {
            for (Registration reg : pendingRegistrations) {
                CommonView.displayMessage(String.format("%d. Officer: %s (%s) | Status: %s | Registered: %s",
                        displayIndex++,
                        reg.getOfficer().getName(),
                        reg.getOfficer().getUserNRIC(),
                        reg.getRegistrationStatus(),
                        DateTimeUtils.formatDateTime(reg.getRegistrationDate())
                ));
            }
        }

        CommonView.displayMessage("\n--- Approved/Rejected Registrations ---");
        if (otherRegistrations.isEmpty()) {
            CommonView.displayMessage("No approved registrations.");
        } else {
            for (Registration reg : otherRegistrations) {
                CommonView.displayMessage(String.format("   Officer: %s (%s) | Status: %s | Registered: %s | Updated: %s | By: %s",
                        reg.getOfficer().getName(),
                        reg.getOfficer().getUserNRIC(),
                        reg.getRegistrationStatus(),
                        DateTimeUtils.formatDateTime(reg.getRegistrationDate()),
                        DateTimeUtils.formatDateTime(reg.getLastUpdated()),
                        reg.getApprovedBy() != null ? reg.getApprovedBy().getName() : "N/A"
                ));
            }
        }
        CommonView.displaySeparator();

        if (pendingRegistrations.isEmpty()) {
            CommonView.displayMessage("No pending registrations to approve/reject.");
            CommonView.prompt("Press Enter to go back...");
            return 0;
        }

        return CommonView.promptInt("Select a PENDING registration number to approve/reject (or 0 to go back): ", 0, pendingRegistrations.size());
    }

    /**
     * Prompts the manager to approve or reject a registration.
     *
     * @return The action selected by the manager: 1 for approve, 2 for reject, 0 to cancel
     */
    public static int promptApproveReject() {
        return CommonView.promptInt("Action: [1] Approve [2] Reject [0] Cancel: ", 0, 2);
    }

    /**
     * Displays a success message when an officer registration is approved.
     *
     * @param officerName The name of the officer whose registration was approved
     */
    public static void displayRegistrationApprovedSuccess(String officerName) {
        CommonView.displaySuccess("Registration for " + officerName + " approved successfully.");
    }

    /**
     * Displays a success message when an officer registration is rejected.
     *
     * @param officerName The name of the officer whose registration was rejected
     */
    public static void displayRegistrationRejectedSuccess(String officerName) {
        CommonView.displaySuccess("Registration for " + officerName + " rejected successfully.");
    }

    /**
     * Displays an error message when a registration action fails.
     *
     * @param action The action (approve/reject) that failed
     */
    public static void displayRegistrationActionFailed(String action) {
        CommonView.displayError("Failed to " + action + " registration.");
    }

    /**
     * Displays a list of officer registrations and prompts the manager to approve/reject/withdraw registrations.
     *
     * @param registrations The list of officer registrations to be managed
     * @return The selected registration number for action
     */
    public static int displayOfficerRegistrations(List<Registration> registrations) {
        // TODO display all officer registrations and prompt for the manager to approve/reject/withdraw the officer registration
        return 0;
    }

    /**
     * Displays a list of applicant applications and prompts the manager to approve/reject/withdraw applications.
     *
     * @param applications The list of applications to be managed
     * @return The selected application number for action
     */
    public static int displayApplicantApplications(List<Application> applications) {
        // TODO display all applicant applications and prompt for the manager to approve/reject/withdraw the application
        return 0;
    }

    /**
     * Displays project details and prompts the manager to edit project details such as name, location, visibility, and flat types.
     *
     * @param project The project whose details are to be managed
     * @return The action selected for editing the project details
     */
    public static int displayProjectDetails(Project project) {
        // TODO display project details and prompt for the manager to edit the project details, such as edit project name, location, etc, toggle visibility, 
        // and add/remove flat types, etc.
        return 0;
    }
}
