package models;

import java.time.LocalDateTime;

import enums.ApplicationStatus;

public class Application {
    private String applicationId;
    private String applicantNric;
    private String projectId;
    private FlatType selectedFlatType;
    private ApplicationStatus applicationStatus;
    private Boolean isWithdrawalRequested;
    private LocalDateTime applicationDate;
    private String approvedBy;

    // Minimal constructor
    public Application(String applicationId, String applicantNric, String projectId, FlatType selectedFlatType) {
        this.applicationId = applicationId;
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.isWithdrawalRequested = false;
        this.applicationDate = LocalDateTime.now();
        this.approvedBy = null;
    }

    // Full constructor
    public Application(String applicationId, String applicantNric, String projectId, FlatType selectedFlatType, ApplicationStatus applicationStatus, Boolean isWithdrawalRequested, LocalDateTime applicationDate, String approvedBy) {
        this.applicationId = applicationId;
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = applicationStatus;
        this.isWithdrawalRequested = isWithdrawalRequested;
        this.applicationDate = applicationDate;
        this.approvedBy = approvedBy;
    }

    // Getters
    public String getApplicationId() {
        return applicationId;
    }

    public String getApplicantNric() {
        return applicantNric;
    }

    public String getProjectId() {
        return projectId;
    }

    public FlatType getSelectedFlatType() {
        return selectedFlatType;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public Boolean isWithdrawalRequested() {
        return isWithdrawalRequested;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    // Setters
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setApplicantNric(String applicantNric) {
        this.applicantNric = applicantNric;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setSelectedFlatType(FlatType selectedFlatType) {
        this.selectedFlatType = selectedFlatType;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void approve(String manager) {
        this.applicationStatus = ApplicationStatus.SUCCESSFUL;
        this.approvedBy = manager;
    }

    public void reject(String manager) {
        this.applicationStatus = ApplicationStatus.UNSUCCESSFUL;
        this.approvedBy = manager;
    }

    public void book() {
        this.applicationStatus = ApplicationStatus.BOOKED;
    }

    public void requestWithdrawal() {
        this.isWithdrawalRequested = true;
    }
}