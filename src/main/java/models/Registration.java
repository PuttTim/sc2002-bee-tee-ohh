package models;

import java.time.LocalDateTime;

import models.enums.RegistrationStatus;

public class Registration {
    private static int lastRegistrationID = 0;

    private String registrationID;
    private User officer;
    private String projectID;
    private RegistrationStatus registrationStatus;
    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;
    private User approvedBy;

    public Registration(User officer, String projectID) {
        this.registrationID = "R" + (++Registration.lastRegistrationID);
        this.officer = officer;
        this.projectID = projectID;
        this.registrationStatus = RegistrationStatus.PENDING;
        this.registrationDate = LocalDateTime.now();
        this.lastUpdated = this.registrationDate;
        this.approvedBy = null;
    }

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
    public String getRegistrationID() {
        return registrationID;
    }

    public User getOfficer() {
        return officer;
    }

    public String getProjectID() {
        return projectID;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    // Setters
    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    // Helpers
    public void approve(User manager) {
        this.registrationStatus = RegistrationStatus.APPROVED;
        this.approvedBy = manager;
        this.lastUpdated = LocalDateTime.now();
    }

    public void reject(User manager) {
        this.registrationStatus = RegistrationStatus.REJECTED;
        this.approvedBy = manager;
        this.lastUpdated = LocalDateTime.now();
    }
}