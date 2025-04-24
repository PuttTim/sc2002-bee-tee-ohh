package models.enums;

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