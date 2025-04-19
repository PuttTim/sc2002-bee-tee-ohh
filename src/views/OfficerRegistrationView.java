package views;

import models.enums.RegistrationStatus;

/**
 * A view class that handles displaying messages related to officer registration.
 */
public class OfficerRegistrationView {

    /**
     * Displays a success message when an officer registration is submitted successfully.
     */
    public static void displayRegistrationSuccess() {
        CommonView.displaySuccess("Officer registration submitted successfully.");
    }

    /**
     * Displays an error message when officer registration fails.
     *
     * @param reason the reason for the registration failure.
     */
    public static void displayRegistrationFailure(String reason) {
        CommonView.displayError("Registration failed: " + reason);
    }

    /**
     * Displays the current registration status of an officer.
     *
     * @param registrationStatus the status of the officer's registration.
     */
    public static void displayRegistrationStatus(RegistrationStatus registrationStatus) {
        CommonView.displayMessage("\nRegistration Status: " + formatStatus(registrationStatus));
    }

    /**
     * Formats the registration status into a more readable string.
     *
     * @param status the registration status.
     * @return a formatted string representing the registration status.
     */
    private static String formatStatus(RegistrationStatus status) {
        return switch (status) {
            case PENDING -> "Pending Approval";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
            default -> status.toString();
        };
    }
}
