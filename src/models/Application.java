package models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import enums.FlatType;
import enums.ApplicationStatus;

public class Application {
    private static int lastApplicationId = 0;

    private String applicationId;
    private String applicantNric;
    private String projectId;
    private FlatType selectedFlatType;
    private ApplicationStatus applicationStatus;
    private boolean isWithdrawalRequested;
    private LocalDateTime applicationDate;
    private String approvedBy;
    private Map<ApplicationStatus, LocalDateTime> applicationStatusHistory;

    public Application(String applicantNric, String projectId, FlatType selectedFlatType) {
        this.applicationId = "A" + (++Application.lastApplicationId);
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.isWithdrawalRequested = false;
        this.applicationDate = LocalDateTime.now();
        this.approvedBy = null;
        this.applicationStatusHistory = new HashMap<>();
        recordStatusChange(ApplicationStatus.PENDING);
    }

    public Application(String applicationId, String applicantNric, String projectId,
                       FlatType selectedFlatType, ApplicationStatus applicationStatus,
                       boolean isWithdrawalRequested, LocalDateTime applicationDate,
                       String approvedBy, Map<ApplicationStatus, LocalDateTime> statusHistory) {
        this.applicationId = applicationId;
        this.applicantNric = applicantNric;
        this.projectId = projectId;
        this.selectedFlatType = selectedFlatType;
        this.applicationStatus = applicationStatus;
        this.isWithdrawalRequested = isWithdrawalRequested;
        this.applicationDate = applicationDate;
        this.approvedBy = approvedBy;
        this.applicationStatusHistory = statusHistory != null ? applicationStatusHistory : new HashMap<>();

        // Update the static ID counter to prevent duplicates
        try {
            int numericId = Integer.parseInt(applicationId.replaceAll("\\D+", ""));
            if (numericId > Application.lastApplicationId) {
                Application.lastApplicationId = numericId;
            }
        } catch (NumberFormatException ignored) {
        }
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