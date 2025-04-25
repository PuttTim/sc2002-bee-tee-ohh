package models.enums;

/**
 * <p>Represents the different statuses of an application.</p>
 * <ul>
 * <li><strong>PENDING:</strong> The application is under review.</li>
 * <li><strong>SUCCESSFUL:</strong> The application was successful.</li>
 * <li><strong>UNSUCCESSFUL:</strong> The application was not successful.</li>
 * <li><strong>BOOKED:</strong> The application has been booked.</li>
 * <li><strong>WITHDRAWAL_REQUESTED</strong> The application has been requested for withdrawal by the application</li>
 * <li><strong>WITHDRAWN:</strong> The application withdrawal request has been approved</li>
 * </ul>
 */
public enum ApplicationStatus {
    PENDING ("Pending"),
    SUCCESSFUL ("Approved"),
    UNSUCCESSFUL ("Rejected"),
    BOOKED ("Booked"),
    WITHDRAWAL_REQUESTED ("Withdrawal Requested"),
    WITHDRAWN ("Withdrawn");

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getKey () {
        return name();
    }
}
