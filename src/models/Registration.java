package models;

import java.time.LocalDateTime;

import models.enums.RegistrationStatus;

/**
 * Represents a registration for a housing project.
 *
 * <p>This class handles registration details:</p>
 * <ul>
 *     <li>Officer that registered</li>
 *     <li>Registration status</li>
 *     <li>Approval/rejection</li>
 *     <li>ID of the project the officer registered for</li>
 *     <li>Last updated timestamp for registration</li>
 * </ul>
 * This class encapsulates the details of the registration, including the officer
 * responsible, the project being registered, the status of the registration, and
 * approval/rejection details. The registration can be approved or rejected by a manager.
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
     * Constructs a new registration for a project with a pending status.
     * The registration ID is automatically generated.
     *
     * @param officer the officer handling the registration
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
     * Constructs a new registration with the provided details, typically used for loading from a repository.
     *
     * @param registrationID the registration ID
     * @param officer the officer handling the registration
     * @param projectID the ID of the project
     * @param registrationStatus the status of the registration
     * @param registrationDate the date the registration was created
     * @param lastUpdated the last updated timestamp of the registration
     * @param approvedBy the manager who approved or rejected the registration
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
     * Gets the registration ID.
     *
     * @return the registration ID
     */
    public String getRegistrationID() {
        return registrationID;
    }

    /**
     * Gets the officer handling the registration.
     *
     * @return the officer
     */
    public User getOfficer() {
        return officer;
    }

    /**
     * Gets the project ID associated with the registration.
     *
     * @return the project ID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the status of the registration.
     *
     * @return the registration status
     */
    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    /**
     * Gets the date when the registration was created.
     *
     * @return the registration date
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Gets the last updated timestamp for the registration.
     *
     * @return the last updated timestamp
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Gets the manager who approved or rejected the registration.
     *
     * @return the manager who approved/rejected the registration
     */
    public User getApprovedBy() {
        return approvedBy;
    }

    // Setters

    /**
     * Sets the status of the registration.
     *
     * @param registrationStatus the new registration status
     */
    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    /**
     * Sets the last updated timestamp for the registration.
     *
     * @param lastUpdated the new last updated timestamp
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Sets the manager who approved or rejected the registration.
     *
     * @param approvedBy the manager who approved/rejected the registration
     */
    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    // Helpers

    /**
     * Approves the registration by a manager.
     *
     * @param manager the manager who approves the registration
     */
    public void approve(User manager) {
        this.registrationStatus = RegistrationStatus.APPROVED;
        this.approvedBy = manager;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Rejects the registration by a manager.
     *
     * @param manager the manager who rejects the registration
     */
    public void reject(User manager) {
        this.registrationStatus = RegistrationStatus.REJECTED;
        this.approvedBy = manager;
        this.lastUpdated = LocalDateTime.now();
    }
}
