package models.enums;

/**
 * <p>Represents the different statuses of an application.</p>
 */
public enum ApplicationStatus {
    /**
     * The application is under review.
     */
    PENDING ("Pending"),

    /**
     * The application was successful.
     */
    SUCCESSFUL ("Approved"),

    /**
     * The application was not successful.
     */
    UNSUCCESSFUL ("Rejected"),

    /**
     * The application has been booked.
     */
    BOOKED ("Booked"),

    /**
     * The application has been requested for withdrawal.
     */
    WITHDRAWAL_REQUESTED ("Withdrawal Requested"),

    /**
     * The application withdrawal request has been approved
     */
    WITHDRAWN ("Withdrawn");

    private final String description;

    /**
     * The status of the current application.
     *
     * @param description of the status of the application.
     */
    ApplicationStatus(String description) {
        this.description = description;
    }

    /**
     * Gets a description of the application status.
     *
     * @return the description of the application status.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the name of the enum constant as a string.
     *
     * @return The name of the enum constant.
     */
    public String getKey () {
        return name();
    }
}
