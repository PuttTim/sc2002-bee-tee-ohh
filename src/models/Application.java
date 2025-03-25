package models;

import java.time.LocalDate;

import enums.ApplicationStatus;

public class Application {
    private String applicationId;
    private Applicant applicant;
    private Project project;
    private String selectedFlatType;
    private ApplicationStatus applicationStatus;
    private Boolean withdrawalRequested;
    private LocalDate applicationDate;
    private String approvedBy;


    // Minimal constructor
    public Application(String applicationId, Applicant applicant, Project project, String selectedFlatType,
    Boolean withdrawalRequested, String approvedBy) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.project = project;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.withdrawalRequested = false;
        this.applicationDate = LocalDate.now();
        this.approvedBy = null;
    }

    // Full constructor
    public Application(String applicationId, Applicant applicant, Project project, String selectedFlatType,
    ApplicationStatus applicationStatus, Boolean withdrawalRequested, LocalDate applicationDate, String approvedBy) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.project = project;
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

    public Applicant getApplicant() {
        return applicant;
    }

    public Project getProject() {
        return project;
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
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setSelectedFlatType(String selectedFlatType) {
        this.selectedFlatType = selectedFlatType;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public void setWithdrawalRequested(Boolean withdrawalRequested) {
        this.withdrawalRequested = withdrawalRequested;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}
