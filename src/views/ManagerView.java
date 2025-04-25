package views;

import java.util.List;
import java.util.stream.Collectors;

import models.Application;
import models.Project;
import models.Registration;
import models.enums.RegistrationStatus;
import utils.DateTimeUtils;

public class ManagerView {
    public static int showSelectHandledProjectMenu(Project project) {
        List<String> options = List.of(
            "Manage Officer Registrations",
            "Manage Applicant Applications",
            "Edit Project Details",
            "View/Reply Enquiries"
            );
        
        int choice = CommonView.displayMenuWithBacking("Select Manager Operation for " + project.getProjectName(), options);

        return choice;
    }

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

    public static int promptApproveReject() {
        return CommonView.promptInt("Action: [1] Approve [2] Reject [0] Cancel: ", 0, 2);
    }

    public static void displayRegistrationApprovedSuccess(String officerName) {
        CommonView.displaySuccess("Registration for " + officerName + " approved successfully.");
    }

     public static void displayRegistrationRejectedSuccess(String officerName) {
        CommonView.displaySuccess("Registration for " + officerName + " rejected successfully.");
    }

    public static void displayRegistrationActionFailed(String action) {
         CommonView.displayError("Failed to " + action + " registration.");
    }

    public static int displayOfficerRegistrations(List<Registration> registrations) {
        // TODO display all officer registrations and prompt for the manager to approve/reject/withdraw the officer registration
        return 0;
    }

    public static int displayApplicantApplications(List<Application> applications) {
        // TODO display all applicant applications and prompt for the manager to approve/reject/withdraw the application
        return 0;
    }

    public static int displayProjectDetails(Project project) {
        // TODO display project details and prompt for the manager to edit the project details, such as edit project name, location, etc, toggle visibility, 
        // and add/remove flat types, etc.
        return 0;
    }

    public static int promptApproveRejectWithdrawal() {
        return CommonView.promptInt("Action: [1] Approve Withdrawal [2] Reject Withdrawal [0] Cancel: ", 0, 2);
    }

    public static void displayWithdrawalApprovedSuccess(String applicantName) {
        CommonView.displaySuccess("Withdrawal request for " + applicantName + " approved successfully.");
    }

    public static void displayWithdrawalRejectedSuccess(String applicantName) {
        CommonView.displaySuccess("Withdrawal request for " + applicantName + " rejected successfully.");
    }

    public static void displayWithdrawalActionFailed(String action) {
         CommonView.displayError("Failed to " + action + " withdrawal request.");
    }

    public static void displayApplicationList(List<Application> applications, String header) {
        OfficerView.displayApplicationList(applications, header); 
    }

    public static void displayApplicationDetails(Application application) {
        OfficerView.displayApplicationDetails(application);
    }
}
