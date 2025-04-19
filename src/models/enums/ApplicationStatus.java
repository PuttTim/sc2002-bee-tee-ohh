package models.enums;

/**
 * Enum representing the possible application statuses.
 */
public enum ApplicationStatus {
    /**
     * Application is pending.
     */
    PENDING,

    /**
     * Application was successful.
     */
    SUCCESSFUL,

    /**
     * Application was unsuccessful.
     */
    UNSUCCESSFUL,

    /**
     * Application has been booked.
     */
    BOOKED,

    /**
     * Application has been withdrawn.
     */
    WITHDRAWN
}