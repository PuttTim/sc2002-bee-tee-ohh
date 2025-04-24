package models.enums;

/**
 * <p>Represents the different statuses of an application.</p>
 * <ul>
 * <li><strong>PENDING:</strong> The application is under review.</li>
 * <li><strong>SUCCESSFUL:</strong> The application was successful.</li>
 * <li><strong>UNSUCCESSFUL:</strong> The application was not successful.</li>
 * <li><strong>BOOKED:</strong> The application has been booked.</li>
 * <li><strong>WITHDRAWN:</strong> The application has been withdrawn by the applicant.</li>
 * </ul>
 */
public enum ApplicationStatus {
    PENDING,
    SUCCESSFUL,
    UNSUCCESSFUL,
    BOOKED,
    WITHDRAWN
}
