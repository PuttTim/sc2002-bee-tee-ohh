package models;

import java.time.LocalDateTime;

import models.enums.RegistrationStatus;

/**
 * Represents a registration by an officer for a project.
 * <p>Includes details about:</p>
 * <ul>
 *     <li>Officer</li>
 *     <li>Project</li>
 *     <li>Registration status</li>
 *     <li>Timestamps</li>
 * </ul>
 */
public class Registration {
    private static int lastRegistrationID = 0;

    private String registrationID;
    private User officer;
    private String projectID;
    private RegistrationStatus registrationStatus;
    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;
    private User approvedBy;

    /**
     * Creates a Registration object with status set to PENDING.
     *
     * @param officer the officer registering
     * @param projectID the ID of the project
     */
    public Registration(User officer, String projectID) {
        this.registrationID = "R" + (++Registration.lastRegistrationID);
        this.officer = officer;
        this.projectID = projectID;
        this.registrationStatus = RegistrationStatus.PENDING;
        this.registrationDate = LocalDateTime.now();
        this.lastUpdated = this.registrationDate;
        this.approvedBy = null;
    }

    /**
     * Creates a Registration object with full details.
     * Used when loading from repository.
     *
     * @param registrationID the registration ID
     * @param officer the officer who registered
     * @param projectID the project ID
     * @param registrationStatus the status of the registration
     * @param registrationDate the date of registration
     * @param lastUpdated the last update time
     * @param approvedBy the manager who approved or rejected
     */
    public Registration(String registrationID, User officer, String projectID,
                        RegistrationStatus registrationStatus, LocalDateTime registrationDate,
                        LocalDateTime lastUpdated, User approvedBy) {
        this.registrationID = registrationID;
        this.officer = officer;
        this.projectID = projectID;
        this.registrationStatus = registrationStatus;
        this.registrationDate = registrationDate;
        this.lastUpdated = lastUpdated;
        this.approvedBy = approvedBy;

        try {
            int numericId = Integer.parseInt(registrationID.replaceAll("\\D+", ""));
            if (numericId > Registration.lastRegistrationID) {
                Registration.lastRegistrationID = numericId;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // Getters
    /**
     * @return the registration ID
     */
    public String getRegistrationID() {
        return registrationID;
    }

    /**
     * @return the officer who registered
     */
    public User getOfficer() {
        return officer;
    }

    /**
     * @return the ID of the project
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * @return the registration status
     */
    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    /**
     * @return the date when the registration was made
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @return the date when the registration was last updated
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @return the manager who approved or rejected the registration
     */
    public User getApprovedBy() {
        return approvedBy;
    }

    // Setters
    /**
     * Sets the registration status.
     *
     * @param registrationStatus the new status
     */
    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    /**
     * Sets the date of the last update.
     *
     * @param lastUpdated the new last update date
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Sets the user who approved the registration.
     *
     * @param approvedBy the manager who approved
     */
    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    // Helpers
    /**
     * Approves the registration.
     *
     * @param manager the manager approving it
     */
    public void approve(User manager) {
        this.registrationStatus = RegistrationStatus.APPROVED;
        this.approvedBy = manager;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Rejects the registration.
     *
     * @param manager the manager rejecting it
     */
    public void reject(User manager) {
        this.registrationStatus = RegistrationStatus.REJECTED;
        this.approvedBy = manager;
        this.lastUpdated = LocalDateTime.now();
    }
}