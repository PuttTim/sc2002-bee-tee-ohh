package views;

import models.enums.RegistrationStatus;

public class OfficerRegistrationView {
    public static void displayRegistrationSuccess() {
        CommonView.displaySuccess("Officer registration submitted successfully.");
    }

    public static void displayRegistrationFailure(String reason) {
        CommonView.displayError("Registration failed: " + reason);
    }

    public static void displayRegistrationStatus(RegistrationStatus registrationStatus) {
        CommonView.displayMessage("\nRegistration Status: " + formatStatus(registrationStatus));
    }

    private static String formatStatus(RegistrationStatus status) {
        return switch (status) {
            case PENDING -> "Pending Approval";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
            default -> status.toString();
        };
    }
}
