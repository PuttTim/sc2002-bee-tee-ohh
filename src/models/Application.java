package models;

import java.time.LocalDate;

import enums.ApplicationStatus;

public class Application {
    private String applicationId;
    private Applicant applicantNric;
    private Project projectId;
    private String selectedFlatType;
    private ApplicationStatus applicationStatus;
    private Boolean withdrawalRequested;
    private LocalDate applicationDate;
    private String approvedBy; // Might need to connect this to manager


    // Minimal constructor
    public Application(String applicationId, Applicant applicantNric, Project projectId, String selectedFlatType,
    Boolean withdrawalRequested, String approvedBy) {
        this.applicationId = applicationId;
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.withdrawalRequested = false;
        this.applicationDate = LocalDate.now();
        this.approvedBy = null;
    }

    // Full constructor
    public Application(String applicationId, Applicant applicantNric, Project projectId, String selectedFlatType,
    ApplicationStatus applicationStatus, Boolean withdrawalRequested, LocalDate applicationDate, String approvedBy) {
        this.applicationId = applicationId;
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = applicationStatus;
        this.withdrawalRequested = withdrawalRequested;
        this.applicationDate = applicationDate;
        this.approvedBy = approvedBy;
    }

    // Getters
    public String getApplicationId() {
        return applicationId;
    }

    public Applicant getApplicantNric() {
        return applicantNric;
    }

    public Project getProjectId() {
        return projectId;
    }

    public String getSelectedFlatType() {
        return selectedFlatType;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public Boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    // Setters
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setApplicantNric(Applicant applicantNric) {
        this.applicantNric = applicantNric;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    public void setSelectedFlatType(String selectedFlatType) {
        this.selectedFlatType = selectedFlatType;
    }

    public void approve(String manager) {
        this.applicationStatus = ApplicationStatus.SUCCESSFUL;
        this.approvedBy = manager;
    }

    public void reject(String manager) {
        this.applicationStatus = ApplicationStatus.UNSUCCESSFUL;
        this.approvedBy = manager;
    }

    public void book() { // Edit this
        this.applicationStatus = ApplicationStatus.BOOKED;
    }

    public void requestWithdrawal() {
        this.withdrawalRequested = true;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }
}
