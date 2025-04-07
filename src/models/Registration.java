package models;

import java.time.LocalDateTime;

import enums.RegistrationStatus;

public class Registration {
    private static int lastRegistrationId = 0;

    private String registrationId;
    private String officerNric;
    private String projectId;
    private RegistrationStatus registrationStatus;
    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;
    private String approvedBy;

    public Registration(String officerNric, String projectId) {
        this.registrationId = "R" + (++Registration.lastRegistrationId);
        this.officerNric = officerNric;
        this.projectId = projectId;
        this.registrationStatus = RegistrationStatus.PENDING;
        this.registrationDate = LocalDateTime.now();
        this.lastUpdated = this.registrationDate;
        this.approvedBy = null;
    }

    public Registration(String registrationId, String officerNric, String projectId,
                        RegistrationStatus registrationStatus, LocalDateTime registrationDate,
                        LocalDateTime lastUpdated, String approvedBy) {
        this.registrationId = registrationId;
        this.officerNric = officerNric;
        this.projectId = projectId;
        this.registrationStatus = registrationStatus;
        this.registrationDate = registrationDate;
        this.lastUpdated = lastUpdated;
        this.approvedBy = approvedBy;

        try {
            int numericId = Integer.parseInt(registrationId.replaceAll("\\D+", ""));
            if (numericId > Registration.lastRegistrationId) {
                Registration.lastRegistrationId = numericId;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // Getters
    public String getRegistrationId() {
        return registrationId;
    }

    public String getOfficerNric() {
        return officerNric;
    }

    public String getProjectId() {
        return projectId;
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
    public void approve(String managerNric) {
        this.registrationStatus = RegistrationStatus.APPROVED;
        this.approvedBy = managerNric;
        this.lastUpdated = LocalDateTime.now();
    }

    public void reject(String managerNric) {
        this.registrationStatus = RegistrationStatus.REJECTED;
        this.approvedBy = managerNric;
        this.lastUpdated = LocalDateTime.now();
    }
}