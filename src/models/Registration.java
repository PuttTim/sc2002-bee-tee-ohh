package models;

import java.time.LocalDateTime;
import enums.RegistrationStatus;

public class Registration {
    private static int lastRegistrationID = 0;

    private String registrationID;
    private String officerNRIC;
    private String projectID;
    private RegistrationStatus registrationStatus;
    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;
    private String approvedBy;

    public Registration(String officerNRIC, String projectID) {
        this.registrationID = "R" + (++Registration.lastRegistrationID);
        this.officerNRIC = officerNRIC;
        this.projectID = projectID;
        this.registrationStatus = RegistrationStatus.PENDING;
        this.registrationDate = LocalDateTime.now();
        this.lastUpdated = this.registrationDate;
        this.approvedBy = null;
    }

    public Registration(String registrationID, String officerNRIC, String projectID,
                        RegistrationStatus registrationStatus, LocalDateTime registrationDate,
                        LocalDateTime lastUpdated, String approvedBy) {
        this.registrationID = registrationID;
        this.officerNRIC = officerNRIC;
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

    public String getOfficerNRIC() {
        return officerNRIC;
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

    public String getApprovedBy() {
        return approvedBy;
    }

    // Setters
    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    // Status Update Methods
    public void approve(String managerNRIC) {
        this.registrationStatus = RegistrationStatus.APPROVED;
        this.approvedBy = managerNRIC;
        this.lastUpdated = LocalDateTime.now();
    }

    public void reject(String managerNRIC) {
        this.registrationStatus = RegistrationStatus.REJECTED;
        this.approvedBy = managerNRIC;
        this.lastUpdated = LocalDateTime.now();
    }
}