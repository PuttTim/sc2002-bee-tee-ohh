package models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import enums.FlatType;
import enums.ApplicationStatus;

public class Application {
    private static int lastApplicationID = 0;

    private String applicationID;
    private String applicantNRIC;
    private String projectId;
    private FlatType selectedFlatType;
    private ApplicationStatus applicationStatus;
    private boolean isWithdrawalRequested;
    private LocalDateTime applicationDate;
    private String approvedBy;
    private Map<ApplicationStatus, LocalDateTime> applicationStatusHistory;

    public Application(String applicantNRIC, String projectId, FlatType selectedFlatType) {
        this.applicationID = "A" + (++Application.lastApplicationID);
        this.applicantNRIC = applicantNRIC;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.isWithdrawalRequested = false;
        this.applicationDate = LocalDateTime.now();
        this.approvedBy = null;
        this.applicationStatusHistory = new HashMap<>();
        recordStatusChange(ApplicationStatus.PENDING);
    }

    public Application(String applicationID, String applicantNRIC, String projectId,
                       FlatType selectedFlatType, ApplicationStatus applicationStatus,
                       boolean isWithdrawalRequested, LocalDateTime applicationDate,
                       String approvedBy, Map<ApplicationStatus, LocalDateTime> statusHistory) {
        this.applicationID = applicationID;
        this.applicantNRIC = applicantNRIC;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = applicationStatus;
        this.isWithdrawalRequested = isWithdrawalRequested;
        this.applicationDate = applicationDate;
        this.approvedBy = approvedBy;
        this.applicationStatusHistory = statusHistory != null ? statusHistory : new HashMap<>();

        try {
            int numericId = Integer.parseInt(applicationID.replaceAll("\\D+", ""));
            if (numericId > Application.lastApplicationID) {
                Application.lastApplicationID = numericId;
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // Getters
    public String getApplicationID() {
        return applicationID;
    }

    public String getApplicantNRIC() {
        return applicantNRIC;
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

    public boolean isWithdrawalRequested() {
        return isWithdrawalRequested;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public Map<ApplicationStatus, LocalDateTime> getApplicationStatusHistory() {
        return applicationStatusHistory;
    }

    public LocalDateTime getApplicationStatusTimestamp(ApplicationStatus status) {
        return applicationStatusHistory.get(status);
    }

    // Setters
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setApplicantNRIC(String applicantNRIC) {
        this.applicantNRIC = applicantNRIC;
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

    // Status Update Methods
    public void approve(String manager) {
        this.applicationStatus = ApplicationStatus.SUCCESSFUL;
        this.approvedBy = manager;
        recordStatusChange(ApplicationStatus.SUCCESSFUL);
    }

    public void reject(String manager) {
        this.applicationStatus = ApplicationStatus.UNSUCCESSFUL;
        this.approvedBy = manager;
        recordStatusChange(ApplicationStatus.UNSUCCESSFUL);
    }

    public void book() {
        this.applicationStatus = ApplicationStatus.BOOKED;
        recordStatusChange(ApplicationStatus.BOOKED);
    }

    public void requestWithdrawal() {
        this.isWithdrawalRequested = true;
        this.applicationStatus = ApplicationStatus.WITHDRAWN;
        recordStatusChange(ApplicationStatus.WITHDRAWN);
    }

    // Helper
    private void recordStatusChange(ApplicationStatus status) {
        applicationStatusHistory.put(status, LocalDateTime.now());
    }
}